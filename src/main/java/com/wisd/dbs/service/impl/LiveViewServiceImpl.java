package com.wisd.dbs.service.impl;

import com.wisd.dbs.bean.*;
import com.wisd.dbs.dao.WbpDao;
import com.wisd.dbs.exception.ExceptionResolver;
import com.wisd.dbs.live.LiveHolder;
import com.wisd.dbs.service.LiveViewService;
import com.wisd.dbs.session.SessionHolder;
import com.wisd.dbs.utils.JsonUtil;
import com.wisd.dbs.wbp.WbpRequestHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

/**
 * 直播观看
 *
 * @author scarlet
 * @time 2019/6/19 7:27
 */
@Service
@AllArgsConstructor
@Slf4j
public class LiveViewServiceImpl implements LiveViewService {
    private final SessionHolder sessionHolder;
    final private WbpDao wbpDao;

    /**
     * 开始观看直播
     *
     * @param epVo
     * @return
     */
    @Override
    public Response start(EpVo epVo) {
        val courseId = epVo.getCourseId();
        val sessionId = epVo.getSessionId();
        val ep = ((Endpoint) sessionHolder.get(sessionId));
        //通过课程id查询直播间
        val op = LiveHolder.getLiveRoomByCourseId(courseId);
        if (!op.isPresent()) {
            //如果直播间不存在
            return Response.build(ReturnCode.courseNotStart);
        }
        //直播间
        val dev = op.get();
        //设置直播id
        val liveId = dev.getLiveId();
        ep.setLiveId(liveId);
        ep.setStatus(StaticConsts.LIVE_START);
        ep.setMediaServer(dev.getMediaServer());

        //填加到观看终端列表
        dev.getEps().add(ep);
        val lr = LiveHolder.get(liveId);
        lr.getEps().add(ep);
        // 推送状态
        pushStatus(ep, lr, StaticConsts.EP_VIEW_START);
        //响应
        return Response.ok(ep);
    }

    /**
     * 结束观看直播
     *
     * @param device
     * @return
     */
    @Override
    public Response stop(Device device) {
        String sessionId = device.getSessionId();
        Endpoint ep = ((Endpoint) sessionHolder.get(sessionId));

        val liveId = ep.getLiveId();
        if (liveId == null) {
            return Response.ok();
        }
        val lr = LiveHolder.get(liveId);
        if (lr == null) {
            return Response.ok();
        }
        ep.setMediaServer(null);
        ep.setLiveId(null);
        ep.setStatus(StaticConsts.LIVE_STOP);

        //终端列表中移除
        lr.getEps().remove(ep);
        val ssId = lr.getSessionId();
        val dev = sessionHolder.get(ssId);
        if (dev != null) {
            ((LiveRoom) dev).getEps().remove(ep);
        }
        //推送状态
        pushStatus(ep, lr, StaticConsts.EP_VIEW_STOP);
        return Response.ok();
    }

    private void pushStatus(Endpoint ep, LiveRoom liveRoom, int status) {
        try {
            val lr = new LiveRoom();
            val ep1 = ((Endpoint) new Endpoint()
                    .setName(ep.getName())
                    .setEpType(ep.getEpType())
                    .setStatus(status)
                    .setId(ep.getId()));
            lr.getEps().add(ep1);
            //设置状态
            ep.setStatus(status);

            val dc = DataCarrier.of(ProtocolHeaders.LIVE_STATUS_CHANGE).setParams(lr);
            //获取request
            val wbpRequest = WbpRequestHolder.get(liveRoom.getId());
            //推送
            wbpDao.asyncRequest(wbpRequest, JsonUtil.toJson(dc), null);
        } catch (Exception e) {
            final val stackTrace = ExceptionResolver.logExceptionStackTrace(e);
            log.error(stackTrace);
        }
    }
}
