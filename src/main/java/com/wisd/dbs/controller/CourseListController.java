package com.wisd.dbs.controller;

import com.wisd.dbs.bean.CourseQueryVo;
import com.wisd.dbs.bean.Response;
import com.wisd.dbs.exception.ExceptionResolver;
import com.wisd.dbs.service.CourseListService;
import com.wisd.dbs.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import static com.wisd.dbs.bean.StaticConsts.*;

/**
 * @author scarlet
 */
@RestController("course.list")
@Getter
@AllArgsConstructor
@Slf4j
public class CourseListController {
    private final CourseListService courseListService;

    public Response query(String param) {
        try {
            CourseQueryVo cpp = JsonUtil.fromJsonGson(param, CourseQueryVo.class);
            val queryType = cpp.getQueryType();
            Response resp;
            switch (queryType) {
                case QUERY_LIVING:
                    resp = courseListService.allLiving();
                    break;
                case QUERY_PLAN:
                    resp = courseListService.queryLivePlan(cpp);
                    break;
                case QUERY_NORMAL_DOCUMENT:
                case QUERY_LABEL_DOCUMENT:
                    resp = courseListService.queryCourseDocument(cpp);
                    break;
                default:
                    resp = Response.ok(new ArrayList<>());
                    break;
            }
            return resp;
        } catch (Exception e) {
            val s1 = ExceptionResolver.logExceptionStackTrace(e);
            log.error("-----------exception happened:-------------");
            //日志
            log.error(s1);
            throw e;
        }
    }

    //    @GetMapping("/categories")
    //    public Response queryCategories(String content) {
    //        DataCarrier<CourseQueryVo, Response> dc = JsonUtil.fromJsonGson(content,
    //                StaticConsts.cqvRType);
    //        val resp = courseListService.categories();
    //        System.out.println(JsonUtil.toJsonGson(resp));
    //        WbpResponse.response(dc, WbpContextHolder.get(), resp);
    //        return resp;
    //    }

    @GetMapping("/categories")
    public Response categories(String param) {
        CourseQueryVo cqvo = JsonUtil.fromJsonGson(param, CourseQueryVo.class);
        return courseListService.categories(cqvo);
    }
}
