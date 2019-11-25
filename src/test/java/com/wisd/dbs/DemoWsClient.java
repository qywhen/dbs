package com.wisd.dbs;

import com.wisd.dbs.websocket.GlobalConsts;
import com.wisd.dbs.websocket.WsMessage;
import lombok.var;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/14 10:38
 */
public class DemoWsClient {

    public static final String SEND_URL = GlobalConsts.HELLO_MAPPING;

    static public class MyStompSessionHandler extends StompSessionHandlerAdapter {

        private String name;

        public MyStompSessionHandler(String name) {
            this.name = name;
        }

        private void showHeaders(StompHeaders headers) {
            for (Map.Entry<String, List<String>> e : headers.entrySet()) {
                System.err.print("  " + e.getKey() + ": ");
                boolean first = true;
                for (String v : e.getValue()) {
                    if (!first) {
                        System.err.print(", ");
                    }
                    System.err.print(v);
                    first = false;
                }
                System.err.println();
            }
        }

        private void sendJsonMessage(StompSession session) {
            var msg = new WsMessage(name);
            session.send(SEND_URL, msg);
        }

        private void subscribeTopic(StompSession session) {
            session.subscribe(GlobalConsts.TOPIC, new StompFrameHandler() {

                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return WsMessage.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    //处理服务器推送的消息
                    System.err.println(payload.toString());
                }
            });
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.err.println("Connected! Headers:");
            showHeaders(connectedHeaders);

            subscribeTopic(session);
            sendJsonMessage(session);

            System.err.println("please input your new message:");
        }
    }

    public static void main(String[] args) throws Exception {
        var simpleWebSocketClient = new StandardWebSocketClient();
        var transports = new ArrayList<Transport>(1);
        transports.add(new WebSocketTransport(simpleWebSocketClient));

        var sockJsClient = new SockJsClient(transports);
        var stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        var url = "ws://localhost:8844" + GlobalConsts.ENDPOINT;
        var name = "spring-" + ThreadLocalRandom.current().nextInt(1, 99);
        var sessionHandler = new MyStompSessionHandler(name);
        var session = stompClient.connect(url, sessionHandler).get();

        //发送消息
        var in = new BufferedReader(new InputStreamReader(System.in));
        for (; ; ) {
            System.out.print(name + " >> ");
            System.out.flush();
            var line = in.readLine();
            if (line == null) {
                break;
            }
            if (line.length() == 0) {
                continue;
            }
            var msg = new WsMessage(name + ": I have a new message [" + line + "]");
            session.send(SEND_URL, msg);
        }
    }
}