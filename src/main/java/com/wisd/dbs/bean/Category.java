package com.wisd.dbs.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/9/3 8:48
 */
@Data
@EqualsAndHashCode(of = "id")
public class Category {
    private String id;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
    private Integer statusId;
    private String parentId;
    private String name;
    private String remark;
    private String type;
    private int sorted;
    private List<Category> children;
}
