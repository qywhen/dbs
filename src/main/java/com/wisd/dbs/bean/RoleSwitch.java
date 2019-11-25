package com.wisd.dbs.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/28 9:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleSwitch {
    private Integer epId;
    private Integer liveId;
    private Integer roleSwitch;
}
