package com.wisd.dbs;

import com.wisd.net.wbp.*;
import com.wisd.net.wbp.exception.WBPError;
import com.wisd.net.wbp.log.ILogListener;
import com.wisd.net.wbp.log.Log;
import com.wisd.net.wbp.packet.WBP.WBPType;
import com.wisd.net.wbp.packet.WBPNotify;
import com.wisd.net.wbp.packet.WBPRequest;
import com.wisd.net.wbp.packet.WBPResponse;
import com.wisd.net.wbp.translayer.INetSocket.NetType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WBPTest {
    public static final String TEST_SERVER_IP = "192.168.20.136";
    public static final int TEST_SERVER_PORT = 9900;
    private WBPClient client;
    private IWBPListener wbpListener = new IWBPListener() {

        @Override
        public boolean onResponse(WBPResponse response) {
            // TODO Auto-generated method stub
            //System.out.println(new String(response.getBody()));
            return false;
        }

        @Override
        public boolean onRequest(WBPRequest request) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onNotify(WBPNotify notify) {
            // TODO Auto-generated method stub
            return false;
        }
    };
    private IWBPResonpseListener respListener = new IWBPResonpseListener() {

        @Override
        public void onSuccess(WBPResponse response) {
            // TODO Auto-generated method stub
            System.out.println(new String(response.getBody()));
        }

        @Override
        public void onError(WBPError error, WBPResponse response) {
            // TODO Auto-generated method stub

        }
    };
    private ILogListener logListener = new ILogListener() {

        @Override
        public void w(Log log) {
            // TODO Auto-generated method stub
            System.out.println(log.getTime() + "[WARN]" + log.getText());
        }

        @Override
        public void v(Log log) {
            // TODO Auto-generated method stub
            System.out.println(log.getTime() + "[VORBE]" + log.getText());
        }

        @Override
        public void i(Log log) {
            // TODO Auto-generated method stub
            System.out.println(log.getTime() + "[INFO]" + log.getText());
        }

        @Override
        public void e(Log log) {
            // TODO Auto-generated method stub
            System.out.println(log.getTime() + "[ERROR]" + log.getText());
        }

        @Override
        public void d(Log log) {
            // TODO Auto-generated method stub
            System.out.println(log.getTime() + "[DEBUG]" + log.getText());
        }
    };

    @Before
    public void setup() throws Exception {
        client = new WBPClient(0, NetType.UDP);
        //client.setServerAddr("192.168.20.136", 9000);
        client.setProperty(WBPLayer.Params.LogLevel.name(), 5);
        client.setWbpListener(this.wbpListener);
        client.setLogListener(this.logListener);
        client.init();
    }

    @After
    public void tearDown() throws Exception {
        client.uninit();
    }

    @Test
    public void test1() throws Exception {
        WBPDialog dialog = WBPDialog.buildDialog(client, WBPType.Request, TEST_SERVER_IP,
                TEST_SERVER_PORT);
        dialog.setTimeout(3000);
        dialog.getRequest().setBody(String.format("hello%d", 0).getBytes());
        dialog.request(new IWBPResonpseListener() {
            @Override
            public void onSuccess(WBPResponse response) {
                // TODO Auto-generated method stub

                Assert.assertEquals("hello,ok", new String(response.getBody()));
            }

            @Override
            public void onError(WBPError error, WBPResponse response) {
                // TODO Auto-generated method stub

            }
        });
    }

}
