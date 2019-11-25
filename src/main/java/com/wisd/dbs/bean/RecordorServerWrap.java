package com.wisd.dbs.bean;

import com.wisd.dbs.record.RecordorServer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/9/16 21:39
 */
@Getter
@Setter
@Accessors(chain = true)
public class RecordorServerWrap {
    private RecordorServer recordor;
}
