package com.wisd.dbs.wbp;

import com.wisd.dbs.bean.DataCarrier;
import com.wisd.dbs.bean.Session;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/9/11 8:42
 */
public class ParamContextHolder {
    private static ThreadLocal<DataCarrier<Session, Object>> tl = new ThreadLocal<>();

    public static void set(DataCarrier<Session,Object> dataCarrier) {
        tl.set(dataCarrier);
    }

    public static DataCarrier<Session,Object> get() {
        return tl.get();
    }

    public static void remove() {
        tl.remove();
    }
}
