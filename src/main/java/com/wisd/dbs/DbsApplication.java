package com.wisd.dbs;

import com.wisd.dbs.properties.LiveProperties;
import com.wisd.dbs.wbp.listener.LogListener;
import com.wisd.dbs.wbp.listener.WBPServerListener;
import com.wisd.net.wbp.WBPLayer;
import com.wisd.net.wbp.WBPServer;
import com.wisd.net.wbp.datalayer.NetDataLayer;
import com.wisd.net.wbp.datalayer.process.WBPDataFactory;
import com.wisd.net.wbp.log.LogType;
import com.wisd.net.wbp.translayer.INetSocket;
import lombok.val;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@MapperScan("com.wisd.dbs.mapper")
@EnableAspectJAutoProxy
public class DbsApplication {
    @Bean
    public WBPServer wbpServer(LogListener logListener, WBPServerListener wbpServerListener,
                               LiveProperties liveProperties) {
        val server = new WBPServer(liveProperties.getWbpServerPort(), INetSocket.NetType.UDP);
        server.setProperty(WBPLayer.Params.LogLevel.name(), LogType.e.ordinal())
                .setProperty(NetDataLayer.Params.MaxSndCacheTime.name(), 50000)
                .setProperty(NetDataLayer.Params.ResendWindowTime.name(), 2000)
                .setProperty(WBPDataFactory.Params.FeedbackIntervalTime.name(), 500)
                .setProperty(WBPDataFactory.Params.FeedbackWindowTime.name(), 1000)
                .setProperty(WBPDataFactory.Params.MaxCombPackTime.name(), 50000);
        server.setWbpListener(wbpServerListener);
        server.setLogListener(logListener);
        server.init();
        return server;
    }

    @Bean
    public ExecutorService pool() {
        val processors = Runtime.getRuntime().availableProcessors();
        val es = new LinkedBlockingQueue<Runnable>();
        val tSize = Math.max(8, processors);
        return new ThreadPoolExecutor(tSize, tSize, 30, TimeUnit.SECONDS, es,
                new BasicThreadFactory.Builder().namingPattern("主线程池-%d").build());
    }

    public static void main(String[] args) {
        try {
            SpringApplication.run(DbsApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
