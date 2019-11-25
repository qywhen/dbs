package com.wisd.dbs.bean;

import lombok.Getter;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 请求内部共用线程池
 *
 * @author scarlet
 * @time 2019/11/21 11:48
 */
@Getter
public enum SharedPool {
    THESINGLEPOOL;

    private ThreadPoolExecutor poolExecutor =
            new ThreadPoolExecutor(16, 16, 30, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(),
                    new BasicThreadFactory.Builder().namingPattern("业务线程池-%d").build());

}
