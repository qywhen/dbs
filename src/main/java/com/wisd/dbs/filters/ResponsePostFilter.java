package com.wisd.dbs.filters;

import com.wisd.dbs.controller.EndpointViewController;
import com.wisd.dbs.wbp.ParamContextHolder;
import com.wisd.dbs.wbp.WbpResponse;
import lombok.Getter;
import lombok.val;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/9/10 10:29
 */
@Getter
@Component
public class ResponsePostFilter implements IPostFilter {
    private int order = 0;
    final ApplicationContext applicationContext;

    public ResponsePostFilter(
            ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object doPost(Object obj) {
        final val dc = ParamContextHolder.get();
        final val names = applicationContext.getBeanNamesForType(EndpointViewController.class);
        if (!names[0].equals(dc.getObject())) {
            //互动不需要在此响应
            if (null != obj) {
                //如果返回值为null则不进行响应
                WbpResponse.response(obj);
            }
        }
        ParamContextHolder.remove();
        return dc;
    }
}
