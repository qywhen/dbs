package com.wisd.dbs.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/17 13:16
 */
@Data
@Entity(name = "tb_live_plan")
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = "id")
public class LiveCoursePlan {
    @Id
    private String id;
    private Integer roomId;
    @JsonIgnore
    private String courseId;
    private String name;
    private String pic;
    private String courseType;
    private String summary;
    private String schoolId;
    private String schoolName;
    private String teacherId;
    private String teacherName;
    private String gradeId;
    private String gradeName;
    /**
     * 是否精品课程
     */
    private Integer label;
    private String startTime;
    private String endTime;
    private String lengthTime;
    private Integer statusId;
    private Integer liveId;
    @Transient
    private Integer week;
}
