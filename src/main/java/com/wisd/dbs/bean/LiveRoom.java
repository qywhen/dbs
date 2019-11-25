package com.wisd.dbs.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wisd.dbs.record.RecordorServer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/18 9:36
 */
@Getter
@Setter
@Table(name = "tb_classroom")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiveRoom extends Device implements Cloneable {
    private String name;
    private Integer statusId;
    private LocalDateTime createTime;
    private String schoolId;
    private String equipmentId;
    private String commissionId;
    private String schoolName;
    private String equipmentName;
    private String commissionName;
    @Transient
    private Set<Endpoint> eps = new HashSet<>();
    @Transient
    private Integer epCount;
    private String startTime;
    @Transient
    private LiveCoursePlan course;
    @Transient
    private String url;
    @Transient
    private RecordorServer recordor;
    @Transient
    @JsonIgnore
    private boolean offline;
    /**
     * 互动信息
     */
    @Transient
    private ConcurrentSkipListSet<EndPointView> invites =
            new ConcurrentSkipListSet<>(Comparator.comparing(EndPointView::getEpId));

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
