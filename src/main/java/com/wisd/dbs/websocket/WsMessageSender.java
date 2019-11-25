package com.wisd.dbs.websocket;

import com.wisd.dbs.bean.LiveCoursePlan;
import com.wisd.dbs.bean.LiveInfo;
import com.wisd.dbs.utils.JsonUtil;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/7/9 16:21
 */
@Component
public class WsMessageSender {
    private final SimpMessagingTemplate messagingTemplate;

    public WsMessageSender(
            SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notice(LiveCoursePlan course, int status) {
        val liveInfo = new LiveInfo();
        liveInfo.setCourse(course);
        liveInfo.setStatus(status);
        messagingTemplate.convertAndSend(GlobalConsts.TOPIC,
                new WsMessage(JsonUtil.toJson(liveInfo)));
    }
}
