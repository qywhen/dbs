package com.wisd.dbs.bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/8/8 9:58
 */
@Entity
@Table(name = "tb_manager")
@Data
public class Manager {
    @Id
    private String id;
    private String userName;
    private String pwd;
    private String mobile;
    private String email;
    private Integer statusId;
    private String roleId;
    private String typeId;
    private String roleName;
    private String typeName;

}
