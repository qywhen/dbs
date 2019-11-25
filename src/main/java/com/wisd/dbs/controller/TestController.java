package com.wisd.dbs.controller;

import com.wisd.dbs.bean.*;
import com.wisd.dbs.dao.WbpDao;
import com.wisd.dbs.service.*;
import com.wisd.dbs.session.SessionHolder;
import com.wisd.dbs.utils.JsonUtil;
import com.wisd.dbs.wbp.WbpRequestHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author scarlet
 */

@RestController("test")
@Data
@AllArgsConstructor
public class TestController {
    private final WbpDao wbpDao;
    private final CourseListService courseListService;
    private final LivePublishService livePublishService;
    private final LiveViewService liveViewService;

    @GetMapping("/rsend")
    public void rsend() {
        val request = WbpRequestHolder.get(123456);
        wbpDao.asyncRequest(request, "123456", null);
    }

    @GetMapping("/query")
    public Response q(@RequestBody CourseQueryVo vo) {
        val query = courseListService.queryLivePlan(vo);
        return query;
    }

    @GetMapping("/queryCourseDocument")
    public Response qcd(@RequestBody CourseQueryVo vo) {
        val qcd = courseListService.queryCourseDocument(vo);
        return qcd;
    }

    @GetMapping("/allliving")
    public Response liveq() {
        return courseListService.allLiving();
    }

    @GetMapping("/allDev")
    public Response allDev() {
        return Response.ok(WbpRequestHolder.allDev());
    }

    @GetMapping("/livestart")
    public Response start(@RequestBody Device device) {
        Response resp = livePublishService.start(device);
        return resp;
    }

    final RegisterService registerService;


    @GetMapping("/lrquery")
    public Response lrquery(Integer id) {
        val ep = registerService.lrquery(id);
        return Response.ok(ep);
    }

    @GetMapping("/epquery")
    public Response epquery(Integer id) {
        val ep = registerService.epquery(id);
        return Response.ok(ep);
    }

    @GetMapping("/liveviewstart")
    public Response liveviewstart(@RequestBody EpVo epVo) {
        return liveViewService.start(epVo);
    }

    @GetMapping("/liveviewstop")
    public Response liveviewstop(@RequestBody Endpoint endpoint) {
        return liveViewService.stop(endpoint);
    }

    @Autowired
    SessionHolder sessionHolder;

    @GetMapping("/allsignin")
    public String allsignin() {
        final val loginDevices = sessionHolder.getLoginDevices();
        return JsonUtil.toJsonGson(Response.ok(loginDevices));
    }

    @Autowired
    EndPointViewService endPointViewService;

    //    @GetMapping("/epviewopen")
    //    public Response endpointview(
    //            @RequestBody DataCarrier<EndPointView, Response> dataCarrier) {
    //        return endPointViewService.open(dataCarrier);
    //    }
    //
    //    @GetMapping("/epviewclose")
    //    public Response epViewclose(@RequestBody DataCarrier<EndPointView, Response>
    //    dataCarrier) {
    //        return endPointViewService.close(dataCarrier);
    //    }
}
