package com.wisd.dbs.record;

import com.wisd.dbs.bean.Device;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/8/27 7:36
 */
@Getter
@Setter
@Accessors(chain = true)
public class RecordorServer extends Device {
//    private Integer port;
    private String ip;
    private int videoPort;
    private int audioPort;
//    private RecorderInfo recorderInfo;
}
