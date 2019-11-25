package com.wisd.dbs.wbp.listener;

import com.wisd.dbs.bean.Response;
import com.wisd.net.wbp.IWBPResonpseListener;
import com.wisd.net.wbp.exception.WBPError;
import com.wisd.net.wbp.packet.WBPResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 同步请求监听器
 *
 * @author scarlet
 * @time 2019/6/29 17:05
 */
@Data
@AllArgsConstructor
@Slf4j
public class WbpResponseListener implements IWBPResonpseListener {
    private Consumer<WBPResponse> success;
    private BiConsumer<WBPError, WBPResponse> error;

    @Override
    public void onSuccess(WBPResponse wbpResponse) {
        success.accept(wbpResponse);
    }

    @Override
    public void onError(WBPError wbpError, WBPResponse wbpResponse) {
        error.accept(wbpError, wbpResponse);
    }
}
