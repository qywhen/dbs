package com.wisd.dbs.filters;

import lombok.var;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/9/9 17:17
 */
@Component
public class FilterChain {
    /**
     * 前置过滤器列表
     */
    public static ArrayList<IPreFilter> preFilters = new ArrayList<>();
    /**
     * 后置过滤器列表
     */
    public static ArrayList<IPostFilter> postFilters = new ArrayList<>();

    public boolean doPreChain() {
        for (IPreFilter filter : preFilters) {
            if (!filter.doPre()) {
                return false;
            }
        }
        return true;
    }

    public void doPostChain(Object param) {
        var result = param;
        for (IPostFilter filter : postFilters) {
            result = filter.doPost(result);
        }
    }
}
