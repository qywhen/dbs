package com.wisd.dbs.bean;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/7/8 11:20
 */
@Data
@Accessors(chain = true)
public class CourseQueryVo {
    /**
     * 课程类型 -1所有1语文2数学 3英语
     */
    private String courseType;
    /**
     * 1 直播列表 2 课程表 10所有点播 11精品课
     */
    private Integer queryType;
    private Integer courseTime;
    private String gradeName;
    private String schoolName;
    private String startTime;
    private String endTime;
    private Integer pageNo;
    private Integer start;
    private Integer pageRows;
    private String sessionId;
}
