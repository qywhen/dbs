package com.wisd.dbs.live;

import com.wisd.dbs.record.RecordorServer;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/8/26 21:07
 */
public class RecordorHolder {
    private static ConcurrentSkipListSet<RecordorServer> recordors =
            new ConcurrentSkipListSet<>(Comparator.comparing(RecordorServer::getId));

    public static Optional<RecordorServer> any() {
        return recordors.stream().findAny();
    }

    public static void add(RecordorServer s) {
        recordors.remove(s);
        recordors.add(s);
    }

    public static void remove(RecordorServer server) {
        recordors.remove(server);
    }
}
