package com.wisd.dbs.controller;

import com.wisd.dbs.bean.Device;
import com.wisd.dbs.bean.EpVo;
import com.wisd.dbs.bean.Response;
import com.wisd.dbs.service.LiveViewService;
import com.wisd.dbs.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.springframework.stereotype.Controller;

/**
 * @author scarlet
 */

@Controller("live.view")
@Getter
@AllArgsConstructor
public class LiveViewController {
    private final LiveViewService liveViewService;

    public Response start(String param) {
        val epVo = JsonUtil.fromJsonGson(param, EpVo.class);

        return liveViewService.start(epVo);
    }

    public Response stop(String param) {
        val device = JsonUtil.fromJsonGson(param, Device.class);

        return liveViewService.stop(device);
    }
}
