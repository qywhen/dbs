package com.wisd.dbs.service.impl;

import com.wisd.dbs.bean.*;
import com.wisd.dbs.dao.WbpDao;
import com.wisd.dbs.exception.ExceptionResolver;
import com.wisd.dbs.live.LiveHolder;
import com.wisd.dbs.live.MediaServerHolder;
import com.wisd.dbs.mapper.LiveCourseMapper;
import com.wisd.dbs.properties.LiveProperties;
import com.wisd.dbs.record.RecordorServer;
import com.wisd.dbs.service.EventStatusService;
import com.wisd.dbs.service.LivePublishService;
import com.wisd.dbs.session.SessionHolder;
import com.wisd.dbs.utils.IdUtil;
import com.wisd.dbs.utils.JsonUtil;
import com.wisd.dbs.utils.TimeUtil;
import com.wisd.dbs.wbp.WbpRequestHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 导播室端 直播 控制
 *
 * @author scarlet
 */
@Service
@AllArgsConstructor
@Slf4j
public class LivePublishServiceImpl implements LivePublishService {
    private final SessionHolder sessionHolder;
    private final LiveCourseMapper liveCourseMapper;
    private final WbpDao wbpDao;
    private final LiveProperties liveProperties;
    private final EventStatusService eventStatusService;

    /**
     * 开启直播
     *
     * @param param
     * @return
     */
    @Override
    public Response start(Device param) {
        val sessionId = param.getSessionId();
        val liveRoom = ((LiveRoom) sessionHolder.get(sessionId));
        final val roomNo = liveRoom.getId();
        final val opLive = LiveHolder.getByRoomId(roomNo);
        if (opLive.isPresent()) {
            val lr = opLive.get();
            //已经开始
            return Response.ok(lr);
        }
        val now = TimeUtil.now();
        val start =
                LocalDateTime.now().plusMinutes(liveProperties.getEarlyStartMinute());
        val startStr = TimeUtil.format(start);

        LiveCoursePlan liveCourse =
                liveCourseMapper.findByRoomNoAndTime(roomNo, startStr, now);
        if (liveCourse == null) {
            return Response.build(ReturnCode.noneCourse);
        }
        final val pic = liveCourse.getPic();
        liveCourse.setPic(liveProperties.getDocumentUrlPrefix() + pic);
        liveCourse.setStartTime(TimeUtil.now());
        liveCourse.setRoomId(roomNo);
        liveRoom.setCourse(liveCourse);
        val any = MediaServerHolder.any();
        return any.map(mediaServer -> {
            liveRoom.setMediaServer(mediaServer);
            int liveId = IdUtil.intId(liveCourse.getId());
            liveCourse.setLiveId(liveId);
            liveRoom.setLiveId(liveId);
            liveRoom.setStartTime(now);
            LiveHolder.put(liveId, liveRoom);

            notificationEp(liveCourse, ProtocolHeaders.ADD_LIVE_COURSE);

            //通知录制服务进行录制
//            final Response change = eventStatusService.change(liveRoom);
            //            Executors.newSingleThreadScheduledExecutor().execute(
            //                    () -> eventStatusService.change(liveRoom));
//            log.info(">>>>>>>>>>>>>>recorder info proceeded:<<<<<<<<<<<<<");
//            log.info(JsonUtil.toJsonGson(change));
            return Response.ok(liveRoom);
        }).orElseGet(() -> Response.build(ReturnCode.NoMediaServer));
    }


    private void pushLiveStatus(LiveCoursePlan liveCourse, Device device,
                                ProtocolHeaders protocolHeaders) {
        try {
            val id = device.getId();
            val request = WbpRequestHolder.get(id);
            wbpDao.asyncRequest(request, protocolHeaders, liveCourse, null);
        } catch (Exception e) {
            val exceptionStackTrace = ExceptionResolver.logExceptionStackTrace(e);
            log.error(exceptionStackTrace);
        }
    }


    /**
     * 结束直播
     *
     * @param device
     * @return
     */
    @Override
    public Response stop(Device device) {
        final val sessionId = device.getSessionId();
        val dev = ((LiveRoom) sessionHolder.get(sessionId));
        return baseStop(dev);
    }

    @Override
    public Response baseStop(LiveRoom dev) {
        StaticConsts.liveLock.lock();
        try {
            final val lrOp = LiveHolder.getByRoomId(dev.getId());
            if (!lrOp.isPresent()) {
                return Response.ok();
            }
            LiveRoom lr = lrOp.get();
            val liveId = lr.getLiveId();

            lr.getEps().forEach(ep -> ep.setLiveId(null));

            final val course = lr.getCourse();
            notificationEp(course, ProtocolHeaders.DELETE_LIVE_COURSE);

            dev.setLiveId(null);
            dev.setMediaServer(null);
            dev.getEps().clear();
            dev.setCourse(null);

            //通知录制服务停止录制
            final val recordor = lr.getRecordor();
            recordStopNotice(recordor, course);
            LiveHolder.remove(liveId);
        } finally {
            StaticConsts.liveLock.unlock();
        }
        return Response.ok();
    }

    private void notificationEp(LiveCoursePlan course, ProtocolHeaders protocolHeaders) {
        //通知
        val eps = sessionHolder.allEndPoint();

        eps.forEach(ep -> pushLiveStatus(course, ep, protocolHeaders));
    }

    /**
     * 查询直播教室状态
     *
     * @param device
     * @return
     */
    @Override
    public Response check(Device device) {
        val sessionId = device.getSessionId();
        val dev = sessionHolder.get(sessionId);
        final val lock = StaticConsts.liveLock;
        lock.lock();
        try {
            final val lrOp = LiveHolder.getByRoomId(dev.getId());
            lrOp.ifPresent(liveRoom -> liveRoom.setOffline(false));
            return lrOp.map(Response::ok).orElse(Response.build(ReturnCode.liveNotExist));
        } finally {
            lock.unlock();
        }
    }

    private void recordStopNotice(RecordorServer recordor, Object param) {
        try {
            wbpDao.asyncRequest(recordor.getId(), ProtocolHeaders.STOP_RECORD, param, null);
        } catch (Exception e) {
            ExceptionResolver.logExceptionStackTrace(e);
        }
    }

}
