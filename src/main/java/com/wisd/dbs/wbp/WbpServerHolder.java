package com.wisd.dbs.wbp;

import com.wisd.net.wbp.WBPServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/7/4 9:07
 */
@Component
public class WbpServerHolder {
    public static WBPServer server;
    @Autowired
    public void confServer(WBPServer server) {
        WbpServerHolder.server = server;
    }
}
