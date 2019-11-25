package com.wisd.dbs.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/18 21:16
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiveInfo {
    private String liveId;
    private LiveCoursePlan course;
    private List<Endpoint> eps;
    private Media media;
    private int status;
}
