package com.wisd.dbs.record;

import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/11/11 12:49
 */
@Data
public class RecordReqHolder {

    public static ConcurrentHashMap<Integer, Consumer<RecordorServer>>
            rrm = new ConcurrentHashMap<>(128);
    public static Lock lock = new ReentrantLock();
}
