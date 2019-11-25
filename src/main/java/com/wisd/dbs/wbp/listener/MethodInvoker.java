package com.wisd.dbs.wbp.listener;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/9/11 14:16
 */
@Component
public class MethodInvoker {
    @SneakyThrows
    public Object invoke(Method method, Object bean, String param) {
        return method.invoke(bean, param);
    }
}
