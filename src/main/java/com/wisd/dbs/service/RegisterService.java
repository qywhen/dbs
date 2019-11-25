package com.wisd.dbs.service;

import com.wisd.dbs.bean.*;
import com.wisd.dbs.record.RecordorServer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/18 15:41
 */
public interface RegisterService {
    Response signout(Device device, boolean b);

    Response keepAlive(Device device);

    Response signinLiveRoom(LiveRoom liveRoom);

    Response signinEndpoint(Endpoint endpoint);

    Response signinMediaServer(Device device);

//    boolean verify(String user, String token);

    LiveRoom lrquery(Integer id);

    Endpoint epquery(Integer id);

    Response signinRecordor(RecordorServer recordorServer);
}
