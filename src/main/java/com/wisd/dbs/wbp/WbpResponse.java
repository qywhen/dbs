package com.wisd.dbs.wbp;

import com.wisd.dbs.bean.DataCarrier;
import com.wisd.dbs.bean.ProtocolHeaders;
import com.wisd.dbs.bean.Session;
import com.wisd.dbs.utils.JsonUtil;
import com.wisd.net.wbp.WBPDialog;
import com.wisd.net.wbp.packet.WBPRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/8/5 14:01
 */
@Slf4j
public class WbpResponse {
    public static void response(Object resp) {
        final val dataCarrier = ParamContextHolder.get();
        dataCarrier.setParams(null).setResult(resp);
        directResponse(dataCarrier);
    }

    public static void response(Object resp, DataCarrier<Session,Object> dc, WBPRequest request) {
        dc.setParams(null).setResult(resp);
        directResponse(dc,request);
    }

    public static void directResponse(DataCarrier<?, ?> dataCarrier) {
        final val request = WbpContextHolder.get();
        directResponse(dataCarrier, request);
    }

    public static void directResponse(DataCarrier<?, ?> dataCarrier, WBPRequest request) {
        val s = JsonUtil.toJson(dataCarrier);
        final val from = request.getFrom();
        final val ip = from.getAddress();
        final val port = from.getPort();
        if (!ProtocolHeaders.KEEP_ALIVE.getMethod().equals(dataCarrier.getMethod())) {
            log.info("============response to:" + ip + ":" + port + "==============");
            log.info(s);
        } else {
            log.debug("============response to:" + ip + ":" + port + "==============");
            log.debug(s);
        }
        val dialog = new WBPDialog(WbpServerHolder.server, request);
        dialog.response(0, s.getBytes(StandardCharsets.UTF_8));
    }
}
