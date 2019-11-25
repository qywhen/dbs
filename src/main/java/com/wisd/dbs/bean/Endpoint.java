package com.wisd.dbs.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;

/**
 * @author scarlet
 */
@Table(name = "tb_equipment")
@Entity
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class Endpoint extends Device {
    private String name;
    private String modelType;
    private Integer statusId;
    private LocalDateTime gmtCreate;
    private String schoolId;
    /**
     * //0正常终端 1 测试终端
     */
    @Transient
    private Integer epType;

}
