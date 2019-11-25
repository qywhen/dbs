package com.wisd.dbs.dao.impl;

import com.wisd.dbs.bean.DataCarrier;
import com.wisd.dbs.bean.ProtocolHeaders;
import com.wisd.dbs.dao.WbpDao;
import com.wisd.dbs.utils.JsonUtil;
import com.wisd.dbs.wbp.WbpRequestHolder;
import com.wisd.dbs.wbp.WbpServerHolder;
import com.wisd.dbs.wbp.listener.WbpResponseListener;
import com.wisd.net.wbp.WBPDialog;
import com.wisd.net.wbp.packet.WBP;
import com.wisd.net.wbp.packet.WBPRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;

/**
 * @author scarlet
 */
@Repository
@Slf4j
@AllArgsConstructor
public class WbpDaoImpl implements WbpDao {

    @Override
    public void syncRequest(String serverIp, int serverPort, String requestData,
                            WbpResponseListener wbpResponseListener) {
        WBPDialog dialog =
                WBPDialog.buildDialog(WbpServerHolder.server, WBP.WBPType.Request, serverIp,
                        serverPort);
        dialog.getRequest().setBody(requestData.getBytes(StandardCharsets.UTF_8));
        dialog.setTimeout(5000);
        dialog.syncRequest(wbpResponseListener);
    }

    /**
     * 同步请求
     *
     * @param request             注册时捕获的wbp协议请求
     * @param requestData         发送的数据
     * @param wbpResponseListener 响应监听器
     */
    @Override
    public void syncRequest(WBPRequest request, String requestData,
                            WbpResponseListener wbpResponseListener) {
        val from = request.getFrom();
        val ip = from.getAddress();
        val port = from.getPort();
        log.info("----------send to :ip" + ip + "port:" + port + "---------------");
        log.info("sendMsg:" + requestData);
        syncRequest(ip, port, requestData, wbpResponseListener);
    }

    /**
     * 异步请求
     *
     * @param request             注册时捕获的wbp协议请求
     * @param requestData         发送的数据
     * @param wbpResponseListener 响应监听器
     */
    @Override
    public void asyncRequest(WBPRequest request, String requestData,
                             WbpResponseListener wbpResponseListener) {
        val from = request.getFrom();
        val ip = from.getAddress();
        val port = from.getPort();

        log.info("----------send to :ip" + ip + "port:" + port + "---------------");
        log.info("sendMsg:" + requestData);
        asyncRequest(ip, port, requestData, wbpResponseListener);
    }

    @Override
    public void asyncRequest(String ip, int port, String requestData,
                             WbpResponseListener wbpResponseListener) {
        WBPDialog dialog =
                WBPDialog.buildDialog(WbpServerHolder.server, WBP.WBPType.Request, ip, port);
        dialog.getRequest().setBody(requestData.getBytes(StandardCharsets.UTF_8));
        dialog.setTimeout(5000);
        dialog.asyncRequest(wbpResponseListener);
    }

    @Override
    public void asyncRequest(WBPRequest request, ProtocolHeaders protocolHeaders, Object param,
                             WbpResponseListener wbpResponseListener) {
        final val dc = DataCarrier.of(protocolHeaders).setParams(param);
        final val requestStr = JsonUtil.toJson(dc);
        asyncRequest(request, requestStr, wbpResponseListener);
    }

    @Override
    public void asyncRequest(int id, ProtocolHeaders protocolHeaders, Object param,
                             WbpResponseListener wbpResponseListener) {
        val request = WbpRequestHolder.get(id);
        asyncRequest(request, protocolHeaders, param, wbpResponseListener);
    }

    @Override
    public void asyncRequest(int id, String requestStr,
                             WbpResponseListener wbpResponseListener) {
        val request = WbpRequestHolder.get(id);
        asyncRequest(request, requestStr, wbpResponseListener);
    }
}
