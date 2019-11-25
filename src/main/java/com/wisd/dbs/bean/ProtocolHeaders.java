package com.wisd.dbs.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/8/26 21:44
 */
@AllArgsConstructor
@Getter
public enum ProtocolHeaders {
    START_RECORD("live.record", "start"),
    STOP_RECORD("live.record", "stop"),
    ROLE_SWITCH("endpoint.view", "roleSwitch"),
    LIVE_STATUS_CHANGE("live.status", "change"),
    SIGNIN("register", "signin"),
    KEEP_ALIVE("register", "keepalive"),
    ADD_LIVE_COURSE("event.info.course", "add"),
    DELETE_LIVE_COURSE("event.info.course", "delete");
    private String object;
    private String method;
}
