package com.wisd.dbs.service;

import com.wisd.dbs.bean.Response;
import com.wisd.dbs.record.RecordorServer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/11/11 12:46
 */
public interface LiveRecordService {
    Response start(RecordorServer ri);
}
