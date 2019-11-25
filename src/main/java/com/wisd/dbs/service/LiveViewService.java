package com.wisd.dbs.service;

import com.wisd.dbs.bean.Device;
import com.wisd.dbs.bean.EpVo;
import com.wisd.dbs.bean.Response;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/19 7:26
 */
public interface LiveViewService {
    Response start(EpVo epVo);

    Response stop(Device device);
}
