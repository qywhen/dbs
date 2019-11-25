package com.wisd.dbs.service.impl;

import com.wisd.dbs.bean.*;
import com.wisd.dbs.dao.WbpDao;
import com.wisd.dbs.live.LiveHolder;
import com.wisd.dbs.live.RecordorHolder;
import com.wisd.dbs.record.RecordReqHolder;
import com.wisd.dbs.record.RecordorServer;
import com.wisd.dbs.service.EventStatusService;
import com.wisd.dbs.session.SessionHolder;
import com.wisd.dbs.utils.JsonUtil;
import com.wisd.dbs.wbp.ParamContextHolder;
import com.wisd.dbs.wbp.WbpContextHolder;
import com.wisd.dbs.wbp.WbpResponse;
import com.wisd.net.wbp.packet.WBPRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;

/**
 * 接收媒体服务推送过来的直播连接建立信息
 *
 * @author scarlet
 * @time 2019/6/26 14:44
 */
@Service
@AllArgsConstructor
@Slf4j
public class EventStatusServiceImpl implements EventStatusService {
    private final SessionHolder sessionHolder;
    private final WbpDao wbpDao;

    /**
     * 处理媒体服务推送过来的状态改变信息
     *
     * @param device
     * @return
     */
    @Override
    public Response change(Device device) {
        val liveId = device.getLiveId();
        val lr = LiveHolder.get(liveId);
        if (lr == null) {
            return Response.build(ReturnCode.liveNotExist);
        }
        val sessionId = lr.getSessionId();
        val dev = ((LiveRoom) sessionHolder.get(sessionId));
        //设置其状态
        val liveStatus = device.getStatus();
        if (liveStatus != null) {
            lr.setStatus(liveStatus);
            dev.setStatus(liveStatus);
        }

        final DataCarrier<Session, Object> dc = ParamContextHolder.get();
        final WBPRequest request = WbpContextHolder.get();
        SharedPool.THESINGLEPOOL.getPoolExecutor().execute(() -> {
            final Response response = RecordorHolder.any().map(recordor -> {
                lr.setRecordor(recordor);
                dev.setRecordor(recordor);

                final Thread currentThread = Thread.currentThread();
                AtomicReference<RecordorServer> ar = new AtomicReference<>();
                log.info(">>>>>>>>>>>>>>>>record start send " +
                         "OK<<<<<<<<<<<<<<<<<<<<<");
                //如果发送通知成功则将待处理任务加入map
                //先将任务存放在map中，等待录制服务进行响应
                final Consumer<RecordorServer> consumer = ri -> {
                    log.info(">>>>>>>>>>>>>>>>>>received from record " +
                             "server<<<<<<<<<<<<<<<<<<<");
                    log.info(JsonUtil.toJsonGson(ri));
                    ar.set(ri);
                    LockSupport.unpark(currentThread);
                };
                RecordReqHolder.rrm.put(liveId, consumer);
                //通知录制服务进行录制
                log.info(">>>>>>>>>>>>>>>>>>>>notice record server<<<<<<<<<<<<<<<<<<<<<<");
                wbpDao.asyncRequest(recordor.getId(), ProtocolHeaders.START_RECORD,
                        lr.getCourse(),
                        null);
                LockSupport.parkNanos(5000_000_000L);
                final RecordorServer recorderInfo = ar.get();
                if (recorderInfo == null) {
                    //说明超时了
                    return Response.err();
                }
                recordor.setAudioPort(recorderInfo.getAudioPort())
                        .setVideoPort(recorderInfo.getVideoPort());
                final RecordorServer rs = new RecordorServer();
                rs.setIp(recordor.getIp())
                        .setVideoPort(recordor.getVideoPort())
                        .setAudioPort(recordor.getAudioPort())
                        .setLiveId(liveId);

                final RecordorServerWrap data = new RecordorServerWrap().setRecordor(rs);
                log.info(">>>>>>>>>>>>>>recorder info proceeded:<<<<<<<<<<<<<");
                log.info(JsonUtil.toJsonGson(data));
                return Response.ok(data);
            }).orElse(Response.ok());
            WbpResponse.response(response,dc,request);
        });
        return null;
    }
}
