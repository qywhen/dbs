package com.wisd.dbs.filters;

import com.wisd.dbs.bean.ProtocolHeaders;
import com.wisd.dbs.bean.Response;
import com.wisd.dbs.bean.ReturnCode;
import com.wisd.dbs.session.SessionHolder;
import com.wisd.dbs.wbp.ParamContextHolder;
import com.wisd.dbs.wbp.WbpResponse;
import lombok.Getter;
import lombok.val;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/9/9 16:21
 */
@Component
@Getter
public class SigninPreFilter implements IPreFilter {
    private int order = 0;
    private final SessionHolder sessionHolder;

    public SigninPreFilter(SessionHolder sessionHolder) {
        this.sessionHolder = sessionHolder;
    }

    @Override
    public boolean doPre() {
        final val dataCarrier = ParamContextHolder.get();
        //验证sessionId
        final val session = dataCarrier.getParams();
        final val sessionId = session.getSessionId();
        if (!ProtocolHeaders.SIGNIN.getMethod().equals(dataCarrier.getMethod())) {
            //不是登陆或注销操作才做验证
            if (sessionHolder.isExpired(sessionId)) {
                final val response = Response.build(ReturnCode.sessionNotExist);
                dataCarrier.setParams(null).setResult(response);
                WbpResponse.directResponse(dataCarrier);
                return false;
            }
        }
        return true;
    }
}
