package com.wisd.dbs.service.impl;

import com.github.pagehelper.PageHelper;
import com.wisd.dbs.bean.*;
import com.wisd.dbs.live.LiveHolder;
import com.wisd.dbs.mapper.CategoryMapper;
import com.wisd.dbs.mapper.CourseDocumentMapper;
import com.wisd.dbs.mapper.LiveCourseMapper;
import com.wisd.dbs.mapper.SchoolMapper;
import com.wisd.dbs.properties.LiveProperties;
import com.wisd.dbs.service.CourseListService;
import com.wisd.dbs.utils.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/19 11:31
 */
@Service
@AllArgsConstructor
public class CourseListServiceImpl implements CourseListService {
    private final LiveCourseMapper liveCourseMapper;
    private final CourseDocumentMapper courseDocumentMapper;
    private LiveProperties liveProperties;
    private CategoryMapper categoryMapper;
    private SchoolMapper schoolMapper;

    /**
     * 查询 直播课程表(本周)
     *
     * @param courseQueryVo
     * @return
     */
    @Override
    public Response queryLivePlan(CourseQueryVo courseQueryVo) {
        val pageNo = courseQueryVo.getPageNo();
        val pageRows = courseQueryVo.getPageRows();
//        val start = (pageNo - 1) * pageRows;
        //本周开始时间
        LocalDateTime from = TimeUtil.mondayThisWeek();
        //本周结束时间
        LocalDateTime to = TimeUtil.sundayThisWeek();
        val startTime = TimeUtil.format(from);
        val endTime = TimeUtil.format(to);
        //查一周的课程数量
//        val count = liveCourseMapper.findCount(startTime, endTime);
        //查一周的课程列表
        PageHelper.startPage(pageNo, pageRows);
        val page = liveCourseMapper.findPage(startTime, endTime);
        final val list = page.getResult();
        list.forEach(course -> {
            //计算每节课是星期几
            val time = course.getStartTime();
            val week = TimeUtil.week(time);
            course.setWeek(week);
        });
        final val count = page.getTotal();
        //设置响应数据
        val liveCoursePageData =
                new PageData<LiveCoursePlan>().setPage(pageNo).setTotal(count).setRows(list);
        //响应
        return Response.ok(liveCoursePageData);
    }


    /**
     * 所有正在进行的直播
     *
     * @return
     */
    @Override
    public Response allLiving() {
        //所有正在进行的直播
        val data = LiveHolder.allLivingCourse();
        //正在直播的数量
        val total = LiveHolder.count();
        //响应体
        val listPageData = new PageData<LiveCoursePlan>()
                //设置数量
                .setTotal(total)
                //设置列表
                .setRows(data);
        return Response.ok(listPageData);
    }

    /**
     * 查询点播列表
     *
     * @param courseQueryVo
     * @return
     */
    @Override
    public Response queryCourseDocument(CourseQueryVo courseQueryVo) {
        val pageNo = courseQueryVo.getPageNo();
        val pageRows = courseQueryVo.getPageRows();
//        val start = (pageNo - 1) * pageRows;
//        courseQueryVo.setStart(start);
        //查数量
//        val count = courseDocumentMapper.findCount(courseQueryVo);
        //查列表
        PageHelper.startPage(pageNo, pageRows);
        val page = courseDocumentMapper.findPage(courseQueryVo);
        final val list = page.getResult();
        list.forEach(course -> {
            //拼播放路径和图片路径
            final val pic = course.getPic();
            course.setPic(liveProperties.getDocumentUrlPrefix() + pic);
            final val url = course.getUrl();
            course.setUrl(liveProperties.getDocumentUrlPrefix() + url);
        });
        final val count = page.getTotal();
        //响应
        val liveCoursePageData =
                new PageData<CourseDocument>().setPage(pageNo).setTotal(count).setRows(list);
        return Response.ok(liveCoursePageData);
    }
    //
    //    @Override
    //    public Response categories() {
    //        final val allTops = categoryMapper.findAllTops();
    //        allTops.forEach(category -> {
    //            final val id = category.getId();
    //            val children = categoryMapper.findByParentIdOrderedBySorted(id);
    //            category.setChildren(children);
    //        });
    //        return Response.ok(allTops);
    //    }


    @Override
    public Response categories(CourseQueryVo cqvo) {
        final val queryType = cqvo.getQueryType();
        if (queryType == StaticConsts.CATEGORY_SCHOOL) {
            return Response.ok(schoolMapper.findAll());
        }
        val categories = categoryMapper.findByParentId(queryType);
        return Response.ok(categories);
    }
}
