package com.wisd.dbs.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wisd.dbs.exception.MyException;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.wisd.dbs.bean.ReturnCode.unknownFailed;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2018-11-20 11:25
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {
    private int error;
    private String description;
    private T data;

    public static <T> Response ok(T data) {
        return new Response<>(ReturnCode.ok.getError(), ReturnCode.ok.getDescription(), data);
    }

    public static Response ok() {
        return ok(null);
    }

    public static Response build(ReturnCode returnCode) {
        return new Response<>(returnCode.getError(), returnCode.getDescription(), null);
    }

    public static Response err() {
        return new Response<>(unknownFailed.getError(), unknownFailed.getDescription(), null);
    }

    public static Response build(MyException e) {
        return new Response<>(e.getReturnCode().getError(), e.getReturnCode().getDescription(),
                null);
    }
}
