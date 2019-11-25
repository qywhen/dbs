package com.wisd.dbs.controller;

import com.wisd.dbs.bean.Device;
import com.wisd.dbs.bean.Response;
import com.wisd.dbs.live.LiveHolder;
import com.wisd.dbs.service.EventStatusService;
import com.wisd.dbs.utils.JsonUtil;
import com.wisd.dbs.websocket.GlobalConsts;
import com.wisd.dbs.websocket.WsMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author scarlet
 */
@RestController("event.status")
@Getter
@AllArgsConstructor
public class EventStatusController {
    private final SimpMessagingTemplate messagingTemplate;
    private final EventStatusService eventStatusService;

    public Response change(String param) {
        val device = JsonUtil.fromJsonGson(param, Device.class);
        return eventStatusService.change(device);
    }


    @MessageMapping(GlobalConsts.HELLO_MAPPING)
    @SendTo(GlobalConsts.TOPIC)
    public void queryAllLivingCourse(WsMessage msg) {
        System.out.println("message received: " + msg.getMessage());
        val liveCoursePlans = LiveHolder.allLivingCourse();
        val json = JsonUtil.toJson(liveCoursePlans);
        messagingTemplate.convertAndSend(GlobalConsts.TOPIC, new WsMessage(json));
    }
}
