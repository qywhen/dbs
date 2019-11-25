package com.wisd.dbs.bean;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/8/8 15:36
 */
@Data
@Accessors(chain = true)
public class Session {
    private String sessionId;
}
