package com.wisd.dbs.controller;

import com.wisd.dbs.bean.LiveRoom;
import com.wisd.dbs.bean.Response;
import com.wisd.dbs.live.LiveHolder;
import com.wisd.dbs.properties.LiveProperties;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * @author scarlet
 */
@RestController
@AllArgsConstructor
public class LiveStateController {
    private final LiveProperties liveProperties;

    @GetMapping("/live/showall")
    @SneakyThrows
    public Response allLiving() {
        val livings = LiveHolder.all();
        livings.forEach(liveRoom -> liveRoom.setEpCount(liveRoom.getEps().size()));
        val list = livings.stream().map(liveRoom -> {
            try {
                LiveRoom clone = ((LiveRoom) liveRoom.clone());
                clone.setEpCount(clone.getEps().size());
                clone.setEps(null);
                clone.setMediaServer(null);
                clone.setSessionId(null);
                val liveId = liveRoom.getLiveId();
                val mediaServer = liveRoom.getMediaServer();
                //生成一个直播url
                val ip = mediaServer.getIp();
                //生成直播地址
                val urlprefix = liveProperties.getUrlprefix();
                val urlsuffix = liveProperties.getUrlsuffix();
                val url = urlprefix + ip + urlsuffix + liveId;
                clone.setUrl(url);
                return clone;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
        return Response.ok(list);
    }

    @GetMapping("/live/{liveId}")
    public Response show(@PathVariable Integer liveId) {
        final val lr = LiveHolder.get(liveId);
        return Response.ok(lr);
    }
}
