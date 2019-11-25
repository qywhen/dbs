package com.wisd.dbs;

import com.wisd.net.wbp.IWBPListener;
import com.wisd.net.wbp.WBPDialog;
import com.wisd.net.wbp.WBPLayer;
import com.wisd.net.wbp.WBPServer;
import com.wisd.net.wbp.datalayer.NetDataLayer;
import com.wisd.net.wbp.datalayer.process.WBPDataFactory;
import com.wisd.net.wbp.log.ILogListener;
import com.wisd.net.wbp.log.Log;
import com.wisd.net.wbp.log.LogType;
import com.wisd.net.wbp.packet.WBPNotify;
import com.wisd.net.wbp.packet.WBPRequest;
import com.wisd.net.wbp.packet.WBPResponse;
import com.wisd.net.wbp.translayer.INetSocket.NetType;

public class TestWBPServer {
    private WBPServer server;
    private IWBPListener wbpListener = new IWBPListener() {

        @Override
        public boolean onResponse(WBPResponse response) {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public boolean onRequest(WBPRequest request) {
            // TODO Auto-generated method stub
            WBPDialog dialog = new WBPDialog(server, request);
            System.out.println(new String(request.getBody()));
            dialog.response(0, "hello,ok.".getBytes());
            return true;
        }

        @Override
        public boolean onNotify(WBPNotify notify) {
            // TODO Auto-generated method stub
            return true;
        }
    };
    private ILogListener logListener = new ILogListener() {

        @Override
        public void w(Log log) {
            // TODO Auto-generated method stub

            System.out.println(log.getTime() + "[W]" + log.getText());
        }

        @Override
        public void v(Log log) {
            // TODO Auto-generated method stub
            System.out.println(log.getTime() + "[V]" + log.getText());
        }

        @Override
        public void i(Log log) {
            // TODO Auto-generated method stub
            System.out.println(log.getTime() + "[I]" + log.getText());
        }

        @Override
        public void e(Log log) {
            // TODO Auto-generated method stub
            System.out.println(log.getTime() + "[E]" + log.getText());
        }

        @Override
        public void d(Log log) {
            // TODO Auto-generated method stub
            System.out.println(log.getTime() + "[D]" + log.getText());
        }
    };

    public TestWBPServer() {
        this.server = new WBPServer(7000, NetType.UDP);
        server.setProperty(WBPLayer.Params.LogLevel.name(), LogType.d.ordinal())
                .setProperty(NetDataLayer.Params.MaxSndCacheTime.name(), 50000)
                .setProperty(NetDataLayer.Params.ResendWindowTime.name(), 2000)
                .setProperty(WBPDataFactory.Params.FeedbackIntervalTime.name(), 500)
                .setProperty(WBPDataFactory.Params.FeedbackWindowTime.name(), 1000)
                .setProperty(WBPDataFactory.Params.MaxCombPackTime.name(), 50000);
        this.server.setWbpListener(this.wbpListener);
        this.server.setLogListener(this.logListener);
    }

    public static void main(String[] args) {
        System.out.println("I'm TestWBPServer");
        //
        TestWBPServer server = new TestWBPServer();
        server.init();
    }

    public void init() {
        this.server.init();
    }
}
