package com.wisd.dbs.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/13 8:16
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class DataCarrier<T, R> {
    private String object;
    private String method;
    private T params;
    private R result;

    public static <T, R> DataCarrier<T, R> of(ProtocolHeaders protocolHeader) {
        return new DataCarrier<T, R>().setObject(protocolHeader.getObject()).setMethod(
                protocolHeader.getMethod());
    }
}
