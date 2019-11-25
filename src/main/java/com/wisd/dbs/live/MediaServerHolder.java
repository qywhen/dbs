package com.wisd.dbs.live;

import com.wisd.dbs.bean.Media;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 媒体服务存储容器
 *
 * @author scarlet
 * @time 2019/6/25 20:23
 */
public class MediaServerHolder {
    private static ConcurrentSkipListSet<Media> csls =
            new ConcurrentSkipListSet<>(Comparator.comparing(Media::getId));

    public static Optional<Media> any() {
        return csls.stream().findAny();
    }

    synchronized public static void add(Media s) {
        csls.remove(s);
        csls.add(s);
    }

    public static void remove(Media server) {
        csls.remove(server);
    }
}
