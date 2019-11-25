package com.wisd.dbs.dao;

import com.wisd.dbs.bean.ProtocolHeaders;
import com.wisd.dbs.wbp.listener.WbpResponseListener;
import com.wisd.net.wbp.packet.WBPRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/23 14:36
 */
public interface WbpDao {
    void syncRequest(String serverIp, int serverPort,
                     String requestData, WbpResponseListener wbpResponseListener);

    void syncRequest(WBPRequest request, String requestData,
                     WbpResponseListener wbpResponseListener);

    void asyncRequest(WBPRequest request, String requestData,
                      WbpResponseListener wbpResponseListener);

    void asyncRequest(String ip, int port, String requestData,
                      WbpResponseListener wbpResponseListener);

    void asyncRequest(WBPRequest request, ProtocolHeaders protocolHeaders, Object param,
                      WbpResponseListener wbpResponseListener);

    void asyncRequest(int id, ProtocolHeaders protocolHeaders, Object param,
                      WbpResponseListener wbpResponseListener);
    void asyncRequest(int id, String requestStr, WbpResponseListener wbpResponseListener);
}
