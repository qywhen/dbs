package com.wisd.dbs.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/13 10:11
 */
@AllArgsConstructor
@Getter
public enum ReturnCode {
    ok(0, "OK"),
    sessionExpired(90010, "session expired"),
    verifyFailed(90011, "verify failed"),
    liveRoomNotExist(90020, "live room not exists"),
    liveRoomNotRegister(90021, "live room not register"),
    endpointNotRegister(90022, "endpoint not register"),
    sessionNotExist(90023, "session not exist"),
    noneCourse(90024, "none course"),
    courseNotStart(90025, "course not start"),
    liveNotExist(90027, "live not exist"),
    epNotExist(90028, "ep not exist"),
    epNotSignin(90029, "ep not signin"),
    NoMediaServer(90030, "none media server"),
    epRefused(90031, "endpoint refused"),
    epNotInThisLive(90032, "ep not in this live"),
    hasSigninWithOtherAddr(90033, "has signin with other address"),
    invalidParam(90090, "invalid param"),
    unknownFailed(90099, "unknown failed");

    private int error;
    private String description;
}
