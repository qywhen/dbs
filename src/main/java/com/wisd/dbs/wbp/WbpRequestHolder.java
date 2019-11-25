package com.wisd.dbs.wbp;

import com.wisd.net.wbp.packet.WBPRequest;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 传输协议客户端存储容器
 *
 * @author scarlet
 * @time 2019/6/19 9:44
 */
public class WbpRequestHolder {
    private static ConcurrentHashMap<Integer, WBPRequest> wbprMap =
            new ConcurrentHashMap<>(128);

    public static void save(int id, WBPRequest wbpRequest) {
        wbprMap.put(id, wbpRequest);
    }

    public static WBPRequest get(int id) {
        return wbprMap.get(id);
    }

    public static WBPRequest remove(int id) {
        return wbprMap.remove(id);
    }

    public static Collection<WBPRequest> allDev() {
        return wbprMap.values();
    }
}
