package com.wisd.dbs.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/19 11:34
 */
@Data
@Accessors(chain = true)
public class PageData<T> {
    private long total;
    private int page;
    private List<T> rows;
}
