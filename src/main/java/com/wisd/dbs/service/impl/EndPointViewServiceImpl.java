package com.wisd.dbs.service.impl;

import com.wisd.dbs.bean.*;
import com.wisd.dbs.dao.WbpDao;
import com.wisd.dbs.exception.ExceptionResolver;
import com.wisd.dbs.live.LiveHolder;
import com.wisd.dbs.live.MediaServerHolder;
import com.wisd.dbs.service.EndPointViewService;
import com.wisd.dbs.session.SessionHolder;
import com.wisd.dbs.utils.JsonUtil;
import com.wisd.dbs.wbp.ParamContextHolder;
import com.wisd.dbs.wbp.WbpContextHolder;
import com.wisd.dbs.wbp.WbpResponse;
import com.wisd.dbs.wbp.listener.WbpResponseListener;
import com.wisd.net.wbp.exception.WBPError;
import com.wisd.net.wbp.packet.WBPResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/18 15:41
 */
@Service
@AllArgsConstructor
@Slf4j
public class EndPointViewServiceImpl implements EndPointViewService {
    private final WbpDao wbpDao;
    private final SessionHolder sessionHolder;

    @Override
    public void invite(EndPointView endPointView) {
        val mode = endPointView.getMode();

        VerifyInviteParam verifyInviteParam = new VerifyInviteParam(endPointView).invoke();
        if (verifyInviteParam.is()) {
            return;
        }
        AtomicReference<Response> arf = new AtomicReference<>();
        val mediaServer = getMediaServer(endPointView);
        endPointView.setMediaServer(mediaServer);
        final val request = WbpContextHolder.get();
        final val dc = ParamContextHolder.get();

        WbpResponseListener wbplistener = new WbpResponseListener(wbpResponse -> {
            Response<EndPointView> resp = getResponse(wbpResponse);
            val error = resp.getError();
            if (error == ReturnCode.ok.getError()) {
                //分析结果
                val epv = resp.getData();
                epv.setMode(endPointView.getMode()).setMediaServer(mediaServer);
                //设置互动状态信息
                verifyInviteParam.getLr().getInvites().remove(epv);
                verifyInviteParam.getLr().getInvites().add(epv);
                verifyInviteParam.getDev().getInvites().remove(epv);
                verifyInviteParam.getDev().getInvites().add(epv);
                val response = Response.ok(epv);
                arf.set(response);
                //向媒体服务推送
                if (mode == StaticConsts.CALL) {
                    pushRole(epv, StaticConsts.ROLE_CALL_OPEN);
                } else {
                    pushRole(epv, StaticConsts.ROLE_CALL_CLOSE);
                }
            } else {
                processFailed(arf, resp);
            }
            WbpResponse.directResponse(dc.setParams(null).setResult(arf.get()), request);
        }, (wbpError, wbpResponse) -> {
            processError(wbpError, arf);
            WbpResponse.directResponse(dc.setParams(null).setResult(arf.get()), request);
        });
        inviteRequest(endPointView, wbplistener);
    }

    @Override
    public void bye(EndPointView epv) {
        AtomicReference<Response> arf = new AtomicReference<>();

        VerifyInviteParam verifyInviteParam = new VerifyInviteParam(epv).invoke();
        if (verifyInviteParam.is()) {
            return;
        }
        LiveRoom dev = verifyInviteParam.getDev();
        LiveRoom lr = verifyInviteParam.getLr();

        final val request = WbpContextHolder.get();
        final val dc = ParamContextHolder.get();

        WbpResponseListener wbplistener = new WbpResponseListener(wbpResponse -> {
            Response<EndPointView> resp = getResponse(wbpResponse);
            val error = resp.getError();
            if (error == ReturnCode.ok.getError()) {
                //分析结果
                val response = Response.ok(epv);

                lr.getInvites().remove(epv);
                dev.getInvites().remove(epv);
                arf.set(response);
                //向媒体服务推送角色转换
                pushRole(epv, StaticConsts.ROLE_CALL_CLOSE);
            } else {
                processFailed(arf, resp);
            }
            WbpResponse.directResponse(dc.setParams(null).setResult(arf.get()), request);
        }, (wbpError, wbpResponse) -> {
            processError(wbpError, arf);
            WbpResponse.directResponse(dc.setParams(null).setResult(arf.get()), request);
        });
        byRequest(epv, wbplistener);
    }

    private Media getMediaServer(EndPointView endPointView) {
        val roomId = endPointView.getRoomId();
        return LiveHolder.getByRoomId(roomId)
                .map(Device::getMediaServer)
                .orElseGet(() -> MediaServerHolder.any().orElse(null));
    }

    private void pushRole(EndPointView endPointView, int role) {
        try {
            val liveId = endPointView.getLiveId();
            val liveRoom = LiveHolder.get(liveId);
            val mediaServer = liveRoom.getMediaServer();
            val mediaServerId = mediaServer.getId();

            val roleSwitch = new RoleSwitch(endPointView.getEpId(), liveId, role);
            wbpDao.asyncRequest(mediaServerId, ProtocolHeaders.ROLE_SWITCH, roleSwitch, null);
        } catch (Exception e) {
            val stacktrace = ExceptionResolver.logExceptionStackTrace(e);
            log.error(stacktrace);
        }
    }

    private void inviteRequest(EndPointView endPointView,
                               WbpResponseListener wbpResponseListener) {
        val epid = endPointView.getEpId();
        val dc = ParamContextHolder.get().setParams(endPointView);
        val requestData = JsonUtil.toJson(dc);
        wbpDao.asyncRequest(epid, requestData, wbpResponseListener);
    }

    private void byRequest(EndPointView epv, WbpResponseListener wbpResponseListener) {
        final val dc = ParamContextHolder.get();
        val epid = epv.getEpId();

        val s = JsonUtil.toJsonGson(dc);
        wbpDao.asyncRequest(epid, s, wbpResponseListener);
    }

    private Response<EndPointView> getResponse(WBPResponse wbpResponse) {
        val bytes = wbpResponse.getBody();
        val respStr = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("---------received from ep:-------------");
        System.out.println(respStr);
        DataCarrier<EndPointView, Response<EndPointView>> dc = JsonUtil.fromJsonGson(respStr,
                StaticConsts.epvRepvType);
        return dc.getResult();
    }

    private void processError(WBPError wbpError, AtomicReference<Response> arf) {
        val format = wbpError.format();
        log.warn("-------------send msg failed:" + format + "------------");
        arf.set(Response.err());
    }

    private void processFailed(AtomicReference<Response> arf, Response<EndPointView> resp) {
        val error = resp.getError();
        log.warn("-----failed:" + error + "---msg:" + resp.getDescription() +
                 "------");
        arf.set(Response.build(ReturnCode.epRefused));
    }

    @Getter
    @Setter
    private class VerifyInviteParam {
        private boolean myResult;
        private EndPointView epv;
        private LiveRoom dev;
        private LiveRoom lr;

        public VerifyInviteParam(EndPointView epv) {
            this.epv = epv;
        }

        boolean is() {
            return myResult;
        }

        public VerifyInviteParam invoke() {
            val epId = epv.getEpId();

            val op = sessionHolder.getByIdNotExpire(epId);
            if (!op.isPresent()) {
                val resp = Response.build(ReturnCode.epNotSignin);
                WbpResponse.response(resp);
                myResult = true;
                return this;
            }

            val ep = (Endpoint) op.get();

            final val sessionId = epv.getSessionId();
            dev = ((LiveRoom) sessionHolder.get(sessionId));

            final val roomId = dev.getId();
            final val lrOp = LiveHolder.getByRoomId(roomId);
            if (!lrOp.isPresent()) {
                WbpResponse.response(Response.build(ReturnCode.liveNotExist));
                myResult = true;
                return this;
            }
            lr = lrOp.get();
            final val eps = lr.getEps();

            if (!eps.contains(ep)) {
                WbpResponse.response(Response.build(ReturnCode.epNotInThisLive));
                myResult = true;
                return this;
            }
            myResult = false;
            return this;
        }
    }
}
