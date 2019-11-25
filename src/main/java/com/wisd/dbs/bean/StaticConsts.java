package com.wisd.dbs.bean;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/18 21:30
 */

public class StaticConsts {
    public static final int GUIDE_EP = 1;
    public static final int ENDPOINT = 2;
    public static final int MEDIA_SERVER = 3;
    public static final int RECORD_SERVER = 5;

    public static final int ROLE_CALL_OPEN = 2;
    public static final int ROLE_CALL_CLOSE = 1;

    public static final int EP_VIEW_START = 1;
    public static final int EP_VIEW_STOP = 2;

    public static final int LIVE_START = 1;
    public static final int LIVE_STOP = 2;

    public static final int OPEN = 1;   //调看
    public static final int CALL = 2;  //互动
    public static final int NORMAL = 0;  //无状态

    public static final int CATEGORY_SCHOOL = 5;
    /**
     * 终端类型 预监终端
     */
    public static final int EP_PREVIEW = 1;

    public static final int QUERY_LIVING = 1;
    public static final int QUERY_PLAN = 2;
    public static final int QUERY_NORMAL_DOCUMENT = 10;
    public static final int QUERY_LABEL_DOCUMENT = 11;


    public static Type sRType = new TypeToken<DataCarrier<Session, Response>>() {}.getType();
    public static Type oOType = new TypeToken<DataCarrier<Object, Response>>() {}.getType();

    public static Type epvRepvType =
            new TypeToken<DataCarrier<EndPointView, Response<EndPointView>>>() {}.getType();


    public static ReadWriteLock rwLock = new ReentrantReadWriteLock();
    public static Lock liveLock = new ReentrantLock();

}
