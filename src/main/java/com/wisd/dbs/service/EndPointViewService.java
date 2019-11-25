package com.wisd.dbs.service;

import com.wisd.dbs.bean.EndPointView;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/18 15:40
 */
public interface EndPointViewService {
    //    Response open(DataCarrier<EndPointView, Response> dataCarrier);
    //
    //    Response close(DataCarrier<EndPointView, Response> dataCarrier);
    //
    //
    //    Response call(DataCarrier<EndPointView,Response> dataCarrier);

    void invite(EndPointView epv);

    void bye(EndPointView endPointView);
}
