package com.wisd.dbs.service;

import com.wisd.dbs.bean.Device;
import com.wisd.dbs.bean.LiveRoom;
import com.wisd.dbs.bean.Response;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/18 15:42
 */
public interface LivePublishService {
    Response start(Device param);

    Response stop(Device device);

    Response baseStop(LiveRoom dev);

    Response check(Device device);
}
