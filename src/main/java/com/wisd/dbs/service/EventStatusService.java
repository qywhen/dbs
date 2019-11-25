package com.wisd.dbs.service;

import com.wisd.dbs.bean.Device;
import com.wisd.dbs.bean.Response;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/26 14:44
 */
public interface EventStatusService {
    Response change(Device device);
}
