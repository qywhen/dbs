package com.wisd.dbs.session;


import com.wisd.dbs.bean.Device;
import com.wisd.dbs.bean.StaticConsts;
import com.wisd.dbs.exception.ExceptionResolver;
import com.wisd.dbs.live.LiveHolder;
import com.wisd.dbs.properties.LiveProperties;
import com.wisd.dbs.service.RegisterService;
import com.wisd.dbs.utils.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 监听会话
 *
 * @author scarlet
 * @date 2019-01-24
 * @time 22:48
 */
@Component
@Slf4j
@AllArgsConstructor
@Data
public class SessionCleaner {
    private final SessionHolder sessionHolder;
    private final RegisterService registerService;
    private final LiveProperties liveProperties;

    /**
     * 基本处理逻辑
     */
    public void run() {
        val milliPeriod = liveProperties.getPeriod() * 1000;
        while (true) {
            try {
                log.info("====sessionMap:onlineCount:" + sessionHolder.size() + "=====");
                if (sessionHolder.isEmpty()) {
                    //空map则此线程睡眠最小时间
                    LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(milliPeriod));
                    //                    Thread.sleep(milliPeriod);
                } else {
                    //最先即将过期的用户
                    val earliestToClean = sessionHolder.firstToExpired();
                    StaticConsts.rwLock.readLock().lock();
                    try {
                        //已经过期的
                        val expiredMap = sessionHolder.expiredDevices();
                        //遍历已经过期的会话做下线处理
                        expiredMap.forEach(
                                (key, value) -> {
                                    registerService.signout(value, false);
                                    final val id = value.getId();
                                    StaticConsts.liveLock.lock();
                                    try {
                                        final val lrOp = LiveHolder.getByRoomId(id);
                                        lrOp.ifPresent(liveRoom -> liveRoom.setOffline(true));
                                    } finally {
                                        StaticConsts.liveLock.unlock();
                                    }
                                });
                    } finally {
                        StaticConsts.rwLock.readLock().unlock();
                    }
                    //计算需要睡眠的时间
                    val millis = milliToExpired(earliestToClean);
                    val min = Math.min(millis, milliPeriod);
                    LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(min));
                    //                    Thread.sleep(min);
                }
            } catch (Exception e) {
                //异常栈
                val str = ExceptionResolver.logExceptionStackTrace(e);
                //记日志
                log.info(str);
            }
        }
    }


    /**
     * 还有多少时间将过期
     *
     * @param device
     * @return 如果返回负数则已经过期
     */
    private long milliToExpired(Device device) {
        val theMilli = TimeUtil.toMilli(device.getExpireTime());
        val nowMill = TimeUtil.toMilli(LocalDateTime.now());
        return theMilli - nowMill;
    }

}
