package com.wisd.dbs.controller;

import com.wisd.dbs.bean.DataCarrier;
import com.wisd.dbs.bean.EndPointView;
import com.wisd.dbs.bean.Response;
import com.wisd.dbs.bean.StaticConsts;
import com.wisd.dbs.service.EndPointViewService;
import com.wisd.dbs.utils.JsonUtil;
import com.wisd.net.wbp.packet.WBPRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.springframework.stereotype.Controller;

/**
 * @author scarlet
 */
@Controller("endpoint.view")
@Getter
@AllArgsConstructor
public class EndpointViewController {
    private final EndPointViewService endPointViewService;

    public void invite(String param) {
        val epv=JsonUtil.fromJsonGson(param, EndPointView.class);
        endPointViewService.invite(epv);
    }

    /**
     * 关闭互动
     *
     * @param param
     */
    public void bye(String param) {
        val epv=JsonUtil.fromJsonGson(param, EndPointView.class);
        endPointViewService.bye(epv);
    }

}
