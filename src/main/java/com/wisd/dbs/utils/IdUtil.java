package com.wisd.dbs.utils;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/5/28 9:08
 */
public class IdUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static int intId() {
        return Math.abs(uuid().hashCode());
    }

    public static int intId(String uuid) {
        return Math.abs(uuid.hashCode());
    }
}
