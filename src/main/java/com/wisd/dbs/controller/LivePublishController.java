package com.wisd.dbs.controller;

import com.wisd.dbs.bean.DataCarrier;
import com.wisd.dbs.bean.Device;
import com.wisd.dbs.bean.Response;
import com.wisd.dbs.bean.StaticConsts;
import com.wisd.dbs.service.LivePublishService;
import com.wisd.dbs.utils.JsonUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author scarlet
 */
@RestController("live.publish")
@Getter
public class LivePublishController {
    private final LivePublishService livePublishService;

    public LivePublishController(LivePublishService livePublishService) {
        this.livePublishService = livePublishService;
    }

    /**
     * 开始直播
     *
     * @param param
     * @return
     */
    @SneakyThrows
    public Response start(String param) {
        //直播开始业务
        val device = JsonUtil.fromJsonGson(param, Device.class);

        return livePublishService.start(device);

    }


    /**
     * 关闭直播
     *
     * @param param
     * @return
     */
    @SneakyThrows
    public Response stop(String param) {
        //直播关闭业务
        val device = JsonUtil.fromJsonGson(param, Device.class);
        return livePublishService.stop(device);
    }

    /**
     * 查询直播室状态
     *
     * @param param
     * @return
     */
    public Response check(String param) {
        val device = JsonUtil.fromJsonGson(param, Device.class);

        return livePublishService.check(device);
    }
}
