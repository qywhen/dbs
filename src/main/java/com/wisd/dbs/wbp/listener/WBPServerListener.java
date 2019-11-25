package com.wisd.dbs.wbp.listener;

import com.wisd.dbs.bean.*;
import com.wisd.dbs.exception.ExceptionResolver;
import com.wisd.dbs.filters.FilterChain;
import com.wisd.dbs.session.SessionHolder;
import com.wisd.dbs.utils.JsonUtil;
import com.wisd.dbs.wbp.ParamContextHolder;
import com.wisd.dbs.wbp.WbpContextHolder;
import com.wisd.dbs.wbp.WbpResponse;
import com.wisd.net.wbp.IWBPListener;
import com.wisd.net.wbp.packet.WBPNotify;
import com.wisd.net.wbp.packet.WBPRequest;
import com.wisd.net.wbp.packet.WBPResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

/**
 * 异步请求监听器
 *
 * @author scarlet
 * @time 2019/6/12 10:50
 */
@Slf4j
@Data
@Component
@AllArgsConstructor
public class WBPServerListener implements IWBPListener {
    private final ApplicationContext applicationContext;
    private final ExecutorService pool;
    private final SessionHolder sessionHolder;
    private final FilterChain filterChain;
    private final MethodInvoker methodInvoker;

    @Override
    public boolean onRequest(WBPRequest request) {
        pool.execute(() -> {
            WbpContextHolder.set(request);
            val body = request.getBody();
            val content = new String(body, StandardCharsets.UTF_8);
            DataCarrier<Session, Object> dc =
                    JsonUtil.fromJsonGson(content, StaticConsts.sRType);

            ParamContextHolder.set(dc);
            try {
                DataCarrier<Object, Object> dataCarrier =
                        JsonUtil.fromJsonGson(content, StaticConsts.oOType);
                //参数中给定的spring bean名称
                String object = dataCarrier.getObject();
                //方法名
                String requestMethod = dataCarrier.getMethod();
                val param = JsonUtil.toJsonGson(dataCarrier.getParams());
                //从applicationContext获取此bean的实例
                val bean = applicationContext.getBean(object);
                val aClass = bean.getClass();
                //获取此方法
                val method = aClass.getMethod(requestMethod, String.class);
                final val from = request.getFrom();
                final val ip = from.getAddress();
                final val port = from.getPort();
                if (!ProtocolHeaders.KEEP_ALIVE.getMethod().equals(requestMethod)) {
                    log.info("----------received from" + ip + ":" + port + ":----------");
                    log.info(content);
                } else {
                    log.debug("----------received from" + ip + ":" + port + ":----------");
                    log.debug(content);
                }

                methodInvoker.invoke(method, bean, param);

            } catch (Exception e) {
                processException(e);
            }
        });
        return true;
    }

    private void processException(Exception e) {
        //异常栈信息
        val s1 = ExceptionResolver.logExceptionStackTrace(e);
        log.error("-----------exception happened:-------------");
        //日志
        log.error(s1);

        //响应体
        val response = Response.build(ReturnCode.invalidParam);
        //响应会话
        WbpResponse.response(response);
    }

    @Override
    public boolean onResponse(WBPResponse wbpResponse) {
        return true;
    }

    @Override
    public boolean onNotify(WBPNotify wbpNotify) {
        return false;
    }
}
