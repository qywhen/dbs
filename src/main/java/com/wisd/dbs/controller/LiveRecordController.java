package com.wisd.dbs.controller;

import com.wisd.dbs.bean.Response;
import com.wisd.dbs.record.RecordorServer;
import com.wisd.dbs.service.LiveRecordService;
import com.wisd.dbs.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/11/11 12:42
 */
@RestController("record.live")
public class LiveRecordController {
    @Autowired
    LiveRecordService lrs;

    public Response start(String param) {

        RecordorServer ri = JsonUtil.fromJsonGson(param, RecordorServer.class);
        return lrs.start(ri);
    }
}
