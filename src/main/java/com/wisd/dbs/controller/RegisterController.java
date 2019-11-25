package com.wisd.dbs.controller;

import com.wisd.dbs.bean.*;
import com.wisd.dbs.record.RecordorServer;
import com.wisd.dbs.service.RegisterService;
import com.wisd.dbs.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.RestController;

import static com.wisd.dbs.bean.StaticConsts.*;

/**
 * @author scarlet
 */
@RestController("register")
@Getter
@AllArgsConstructor
@Slf4j
public class RegisterController {
    private final RegisterService registerService;

    /**
     * 设备注册
     *
     * @param param
     * @return
     */
    @SneakyThrows
    public Response signin(String param) {
        val device = JsonUtil.fromJsonGson(param, Device.class);

        val type = device.getType();

        Response resp;
        switch (type) {
            case ENDPOINT:
                //注册观看端
                val endpoint = JsonUtil.fromJsonGson(param, Endpoint.class);
                resp = registerService.signinEndpoint(endpoint);
                break;
            case MEDIA_SERVER:
                //媒体服务
                resp = registerService.signinMediaServer(device);
                break;
            case GUIDE_EP:
                val liveRoom = JsonUtil.fromJsonGson(param, LiveRoom.class);
                resp = registerService.signinLiveRoom(liveRoom);
                break;
            case RECORD_SERVER:
                val recordorServer = JsonUtil.fromJsonGson(param, RecordorServer.class);
                resp = registerService.signinRecordor(recordorServer);
                break;
            default:
                resp = Response.ok();
                break;
        }
        return resp;
    }

    /**
     * 设备注销
     *
     * @param param
     * @return
     */
    @SneakyThrows
    public Response signout(String param) {
        //注销设备
        val device = JsonUtil.fromJsonGson(param, Device.class);
        return registerService.signout(device, true);
    }


    /**
     * 保活
     *
     * @param param
     * @return
     */
    @SneakyThrows
    public Response keepalive(String param) {
        // 保活业务
        val device = JsonUtil.fromJsonGson(param, Device.class);
        return registerService.keepAlive(device);
    }

}
