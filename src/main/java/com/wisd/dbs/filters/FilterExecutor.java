package com.wisd.dbs.filters;

import com.wisd.dbs.bean.Response;
import com.wisd.dbs.bean.ReturnCode;
import com.wisd.dbs.wbp.WbpResponse;
import lombok.AllArgsConstructor;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/9/11 11:26
 */
@Aspect
@Component
@AllArgsConstructor
public class FilterExecutor {
    private final FilterChain filterChain;

    @Pointcut("execution( * com.wisd.dbs.wbp.listener.MethodInvoker.invoke(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public void aroundForFilter(ProceedingJoinPoint joinPoint) {
        try {
            //执行前置过滤器链
            if (!filterChain.doPreChain()) {
                return;
            }
            //反射代理方法
            val resp = joinPoint.proceed();
            //执行后置过滤器链
            filterChain.doPostChain(resp);
        } catch (Throwable throwable) {
            WbpResponse.response(Response.build(ReturnCode.invalidParam));
        }
    }
}
