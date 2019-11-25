package com.wisd.dbs.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/9/4 10:52
 */
@Data
@Accessors(chain = true)
public class School {
    private String id;
    private String schoolName;
    @JsonIgnore
    private String address;
    @JsonIgnore
    private String headmasterPhone;
    @JsonIgnore
    private String headmaster;
    @JsonIgnore
    private String schoolNature;
    private String schoolNo;
    @JsonIgnore
    private String schoolGrade;
    @JsonIgnore
    private String categoryId;
    private String statusId;
    @JsonIgnore
    private String label;
    @JsonIgnore
    private String schoolAudit;
}
