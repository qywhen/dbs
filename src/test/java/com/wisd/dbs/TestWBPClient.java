package com.wisd.dbs;

import com.google.gson.Gson;
import com.wisd.dbs.bean.DataCarrier;
import com.wisd.dbs.bean.Device;
import com.wisd.dbs.bean.Endpoint;
import com.wisd.dbs.wbp.listener.WbpResponseListener;
import com.wisd.net.wbp.*;
import com.wisd.net.wbp.datalayer.DataFlowStatistics;
import com.wisd.net.wbp.datalayer.DataPacketLostStatistics;
import com.wisd.net.wbp.datalayer.NetDataLayer;
import com.wisd.net.wbp.datalayer.NetDataLayer.NetLinkType;
import com.wisd.net.wbp.datalayer.NumericalStatistics;
import com.wisd.net.wbp.exception.WBPError;
import com.wisd.net.wbp.log.ILogListener;
import com.wisd.net.wbp.log.Log;
import com.wisd.net.wbp.log.LogType;
import com.wisd.net.wbp.packet.WBP.WBPType;
import com.wisd.net.wbp.packet.WBPNotify;
import com.wisd.net.wbp.packet.WBPRequest;
import com.wisd.net.wbp.packet.WBPResponse;
import com.wisd.net.wbp.translayer.INetSocket.NetType;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestWBPClient {
    public static final String TEST_SERVER_IP = "103.123.213.10";//"114.251.66.81";
    public static final int TEST_SERVER_PORT = 7000;
    private WBPClient client;
    private int num = 10000;
    private int failed = 0;
    private IWBPListener wbpListener = new IWBPListener() {

        @Override
        public boolean onResponse(WBPResponse response) {
            System.out.println(new String(response.getBody()));
            return true;
        }

        @Override
        public boolean onRequest(WBPRequest request) {
            return true;
        }

        @Override
        public boolean onNotify(WBPNotify notify) {
            // TODO Auto-generated method stub
            return true;
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

    @Before
    public void before() {
        client = new WBPClient(0, NetType.UDP);
        client.setProperty(WBPLayer.Params.LogLevel.name(), LogType.e.ordinal())
                .setProperty(NetDataLayer.Params.NetLinkType.name(), NetLinkType.adsl.name());
        client.setWbpListener(wbpListener);
        client.setLogListener(logListener);
        System.out.println("I'm TestWBPClient");
        //
        init();
    }

    @Test
    public void test0() {
        WBPDialog dialog = WBPDialog.buildDialog(client, WBPType.Request,
                TEST_SERVER_IP, TEST_SERVER_PORT);
        dialog.getRequest().setBody("hello syncRequest".getBytes());
        dialog.request();
    }

    @Test
    public void test1() {
        WBPDialog dialog = WBPDialog.buildDialog(client, WBPType.Request,
                TEST_SERVER_IP, TEST_SERVER_PORT);
        dialog.setTimeout(5000);
        val carrier = new DataCarrier<>();
        carrier.setObject("course.list");
        carrier.setMethod("query");
        val device = new Endpoint();
        device.setId(101236);
        //        device.setSessionId("bed371ddff36401d8b41a9e2c41afd");
        device.setType(2);
        device.setEpType(1);
        device.setUser("123456");
        val token = DigestUtils.md5DigestAsHex("123456".getBytes());
        device.setToken("25B93752D3259C6F4A6asdfs95459724FaAD6");
        device.setExpire(10000);
        carrier.setParams(device);
        val s = new Gson().toJson(carrier);
        dialog.getRequest().setBody(s.getBytes());
        //            dialog.asyncRequest(new WbpResponseListener(wbpResponse -> {
        //                val body = wbpResponse.getBody();
        //                val x = new String(body);
        //                System.out.println(x);
        //            }, (wbpError, wbpResponse) -> System.out.println(wbpError.format()
        //            )));
        dialog.syncRequest(new WbpResponseListener(wbpResponse -> {
            val body = wbpResponse.getBody();
            val x = new String(body);
            System.out.println(x);
        }, (wbpError, wbpResponse) -> System.out.println(wbpError.format()
        )));
    }

    @Test
    public void test10() throws InterruptedException {
        val carrier = new DataCarrier<>();
        carrier.setObject("register");
        carrier.setMethod("signin");
        val device = new Endpoint();
        device.setUser("superAdmin");
        device.setToken("25B93752D3259C6F4A695459724FAAD6");
        device.setEpType(2);
        device.setId(128);
        device.setSessionId("bed371ddff36401d8b41a9e2c41afde5");
        device.setType(2);
        device.setExpire(10000);
        carrier.setParams(device);
        val s = new Gson().toJson(carrier);
        final ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
//        pool.scheduleWithFixedDelay(() -> {
                    WBPDialog dialog = WBPDialog.buildDialog(client, WBPType.Request,
                            TEST_SERVER_IP, TEST_SERVER_PORT);
                    dialog.setTimeout(3000);
                    dialog.getRequest().setBody(s.getBytes());
                    dialog.syncRequest(new WbpResponseListener(wbpResponse -> {
                        val body = wbpResponse.getBody();
                        val x = new String(body);
                        System.out.println(x);
                    }, (wbpError, wbpResponse) -> System.out.println(wbpError.format())));
//                }, 0, 3,
//                TimeUnit.SECONDS);
//        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        //            dialog.asyncRequest(new WbpResponseListener(wbpResponse -> {
        //                val body = wbpResponse.getBody();
        //                val x = new String(body);
        //                System.out.println(x);
        //            }, (wbpError, wbpResponse) -> System.out.println(wbpError.format()
        //            )));
        //        Thread.sleep(10000);
    }

    @Test
    public void test3() {
        try {
            WBPDialog dialog = WBPDialog.buildDialog(client, WBPType.Request,
                    TEST_SERVER_IP, TEST_SERVER_PORT);
            dialog.setTimeout(3000);
            val carrier = new DataCarrier<Device, Object>();
            carrier.setObject("live.publish");
            carrier.setMethod("check");
            val device = new Device();

            //            device.setUser("wenqi1uyue1");
            //            device.setToken("123456");
            //            device.setId(123123);
            //            device.setType(1);
            //            device.setExpire(100);
            device.setSessionId("b3d2841be90947c49d88b9672fbd5c23");
            carrier.setParams(device);

            val bytes = new Gson().toJson(carrier).getBytes();
            dialog.getRequest().setBody(bytes);
            dialog.request(respListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        try {
            this.failed = 0;
            for (int i = 0; i < 100000; i++) {
                long t1 = System.currentTimeMillis();
                WBPDialog dialog = WBPDialog.buildDialog(client, WBPType.Request,
                        TEST_SERVER_IP, TEST_SERVER_PORT);
                dialog.getRequest().setBody(readFile("/D:/workfiles/wbp/wbp/bin/test.file"));
                dialog.setTimeout(50000);
                dialog.request(new IWBPResonpseListener() {

                    @Override
                    public void onSuccess(WBPResponse response) {
                        System.out.println("success");
                        // TODO Auto-generated method stub
                        writeFile("/D:/workfiles/wbp/wbp/bin/live_response.png",
                                response.getBody());
                    }

                    @Override
                    public void onError(WBPError error, WBPResponse response) {
                        // TODO Auto-generated method stub
                        System.out.println("failed");
                        TestWBPClient.this.failed++;
                    }
                });
                long t2 = System.currentTimeMillis();
                client.getDataLayer().testConnection(dialog.getRequest().getTo());
                DataFlowStatistics statistics = client.getDataLayer().getDataFlowStatistics(
                        dialog.getRequest().getTo());
                DataPacketLostStatistics lostStatistics =
                        client.getDataLayer().getDataPacketLostStatistics(
                                dialog.getRequest().getTo());
                NumericalStatistics linkDelayStatistics =
                        client.getDataLayer().getNetDelayStatistics(
                                dialog.getRequest().getTo());
                System.out.println(String.format("test %d, costs time:%dms, F/Total: %d/%d\r\n"
                                                 +
                                                 "data_flow U/D:%1.2fkbps/%1.2fkbps, " +
                                                 "lost_packets L/T/R:%d/%d/%1.4f,\r\n"
                                                 +
                                                 "link_delay avg/70v/80v/90v:%dms/%dms/%dms" +
                                                 "/%dms",
                        i, (t2 - t1), this.failed, i,
                        statistics.getUploadDataFlow(), statistics.getDownloadDataFlow(),
                        lostStatistics.getLostCount(), lostStatistics.getTotalCount(),
                        lostStatistics.getTotalCount() > 0 ?
                                (double) lostStatistics.getLostCount() /
                                lostStatistics.getTotalCount() : 0,
                        linkDelayStatistics.getStatiticsAvg(),
                        linkDelayStatistics.getv70(), linkDelayStatistics.getv80(),
                        linkDelayStatistics.getv90()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] readFile(String fileName) {
        try {
            File f = new File(fileName);
            if (!f.exists() || !f.isFile()) {
                System.out.println("not found file");
            }
            ByteBuffer buf = ByteBuffer.allocate((int) f.length());
            DataInputStream input = new DataInputStream(new FileInputStream(f));
            int len = 0;
            byte[] data = new byte[1024];
            while (-1 != (len = input.read(data))) {
                buf.put(data, 0, len);
            }
            input.close();
            buf.flip();
            return buf.array();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeFile(String fileName, byte[] data) {
        try {
            File f = new File(fileName);
            //
            DataOutputStream out = new DataOutputStream(new FileOutputStream(f));
            out.write(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        client.init();
    }
}
