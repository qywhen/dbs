package com.wisd.dbs.service;

import com.wisd.dbs.bean.Category;
import com.wisd.dbs.bean.CourseQueryVo;
import com.wisd.dbs.bean.Response;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/19 11:30
 */
public interface CourseListService {
    Response queryLivePlan(CourseQueryVo courseQueryVo);

    Response allLiving();

    Response queryCourseDocument(CourseQueryVo cpp);

//    Response categories();

    Response categories(CourseQueryVo cqvo);
}
