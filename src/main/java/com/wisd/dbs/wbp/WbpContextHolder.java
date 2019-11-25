package com.wisd.dbs.wbp;

import com.wisd.net.wbp.packet.WBPRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/9/1 12:25
 */
public class WbpContextHolder {
    private static ThreadLocal<WBPRequest>tl=new ThreadLocal<>();

    public static void set(WBPRequest request) {
        tl.set(request);
    }

    public static WBPRequest get() {
        return tl.get();
    }
}
