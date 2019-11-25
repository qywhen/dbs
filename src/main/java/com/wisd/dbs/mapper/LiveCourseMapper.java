package com.wisd.dbs.mapper;

import com.github.pagehelper.Page;
import com.wisd.dbs.bean.LiveCoursePlan;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/18 9:03
 */
@Repository
public interface LiveCourseMapper {
    @Select("select * from tb_live_plan")
    @ResultMap("com.wisd.dbs.mapper.LiveCourseMapper.LiveCourseResult")
    LiveCoursePlan findByRoomNo(String roomNo);

    @Select("select a.*,b.no as roomId from tb_live_plan a join tb_classroom b on a.room_no" +
            " = b.id where b.no = #{param1} and a.start_time like CONCAT(#{param2},'%') " +
            "order by a.start_time")
    @ResultMap("com.wisd.dbs.mapper.LiveCourseMapper.LCR")
    List<LiveCoursePlan> findTodayByRoomId(Integer roomNo, String format);

    @Select("select a.*, b.no as roomId from tb_live_plan a join tb_classroom b on a.room_no" +
            " = b.id where b.no = #{param1} and a.start_time < #{param2} and a.end_time > " +
            "#{param3} order by a.start_time limit 1")
    @ResultMap("com.wisd.dbs.mapper.LiveCourseMapper.LCR")
    LiveCoursePlan findByRoomNoAndTime(Integer roomNo, String startStr, String now);

    @Select("select * from tb_live_plan where start_time between #{param1} and #{param2} " +
            "order by start_time")
    @ResultMap("com.wisd.dbs.mapper.LiveCourseMapper.LiveCourseResult")
    Page<LiveCoursePlan> findPage(String startTime, String endTime);

    @Select("select count(id) from tb_live_plan where start_time between #{param1} and " +
            "#{param2}")
    int findCount(String startTime, String endTime);
}
