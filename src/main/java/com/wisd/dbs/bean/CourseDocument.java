package com.wisd.dbs.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/7/9 11:03
 */
@Data
@Entity
@Table(name = "course_document")
@EqualsAndHashCode(of = "id")
public class CourseDocument {
    @Id
    private String id;
    private Integer statusId;
    private String courseId;
    private String chapterId;
    private String periodId;
    private String name;
    private Integer status;
    private String url;
    private String author;
    private String size;
    private Integer type;
    private String pic;
    private String categoryId;
    private Integer isFree;
    private Integer label;
    private Integer isPutaway;
    private String teacherId;
    private String teacherName;
    private String schoolId;
    private String schoolName;
    private String gradeId;
    private String gradeName;
    private String summary;
    private String courseType;
    private String courseTime;
}
