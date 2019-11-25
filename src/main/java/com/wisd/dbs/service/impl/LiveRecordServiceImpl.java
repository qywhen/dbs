package com.wisd.dbs.service.impl;

import com.wisd.dbs.bean.Response;
import com.wisd.dbs.record.RecordReqHolder;
import com.wisd.dbs.record.RecordorServer;
import com.wisd.dbs.service.LiveRecordService;
import com.wisd.dbs.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/11/11 12:47
 */
@Service
@Slf4j
public class LiveRecordServiceImpl implements LiveRecordService {
    @Override
    public Response start(RecordorServer ri) {
        log.info(">>>>>>>>>>>>>>>received record start response<<<<<<<<<<<<<<<<<<");
        log.info(JsonUtil.toJsonGson(ri));
        final int liveId = ri.getLiveId();
        final ConcurrentHashMap<Integer, Consumer<RecordorServer>> rrm = RecordReqHolder.rrm;
        final Consumer<RecordorServer> removed = rrm.remove(liveId);
        removed.accept(ri);
        return null;
    }
}
