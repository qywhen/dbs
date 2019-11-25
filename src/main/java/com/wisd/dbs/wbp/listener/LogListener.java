package com.wisd.dbs.wbp.listener;

import com.wisd.net.wbp.log.ILogListener;
import com.wisd.net.wbp.log.Log;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/12 10:52
 */
@Component
public class LogListener implements ILogListener {
    @Override
    public void w(Log log) {
        System.out.println(log.getTime() + "[W]" + log.getText());
    }

    @Override
    public void v(Log log) {
        System.out.println(log.getTime() + "[V]" + log.getText());
    }

    @Override
    public void i(Log log) {
        System.out.println(log.getTime() + "[I]" + log.getText());
    }

    @Override
    public void e(Log log) {
        System.out.println(log.getTime() + "[E]" + log.getText());
    }

    @Override
    public void d(Log log) {
        System.out.println(log.getTime() + "[D]" + log.getText());
    }
}
