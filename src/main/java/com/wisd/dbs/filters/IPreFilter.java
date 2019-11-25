package com.wisd.dbs.filters;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/9/9 14:53
 */
public interface IPreFilter {
    default int getOrder() {
        return 0;
    }

    boolean doPre();

}
