package com.wisd.dbs.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/15 9:29
 */
@EqualsAndHashCode(of = "id",callSuper = false)
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@MappedSuperclass
@Accessors(chain = true)
public class Device extends Session implements Serializable {
    @Id
    private Integer id;
    @JsonIgnore
    private String user;
    @JsonIgnore
    private String token;
    @Transient
    @JsonIgnore
    private LocalDateTime expireTime;
    @Transient
    @JsonIgnore
    private Integer type;//1直播，2终端 3媒体服务 4媒体分发服务
    @Transient
    private Media mediaServer;
    @Transient
    @JsonIgnore
    private Integer expire;//有效期
    @Transient
    private Integer liveId;
    @Transient
    private Integer status;//直播状态 1开启，2关闭

}
