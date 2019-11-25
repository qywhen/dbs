package com.wisd.dbs.filters;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/9/10 10:23
 */
public interface IPostFilter {
    default int getOrder() {
        return 0;
    }

    Object doPost(Object obj);
}
