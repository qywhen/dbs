package com.wisd.dbs.live;

import com.wisd.dbs.bean.StaticConsts;
import com.wisd.dbs.exception.ExceptionResolver;
import com.wisd.dbs.properties.LiveProperties;
import com.wisd.dbs.service.LivePublishService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/10/10 9:57
 */
@Component
@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class LiveCleaner {
    private final LiveProperties liveProperties;
    private final LivePublishService lpService;

    /**
     * 基本处理逻辑
     */
    public void run() {
        val milliPeriod = liveProperties.getPeriod() * 1000;
        while (true) {
            try {
                log.info("====liveMap:count:" + LiveHolder.count() + "=====");
                StaticConsts.liveLock.lock();
                try {
                    final val allFinished = LiveHolder.allFinished();
                    allFinished.forEach(lpService::baseStop);
                } finally {
                    StaticConsts.liveLock.unlock();
                }
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(milliPeriod));
            } catch (Exception e) {
                //异常栈
                val str = ExceptionResolver.logExceptionStackTrace(e);
                //记日志
                log.info(str);
            }
        }
    }
}
