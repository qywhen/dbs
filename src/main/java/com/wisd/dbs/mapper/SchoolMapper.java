package com.wisd.dbs.mapper;

import com.wisd.dbs.bean.School;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/9/4 11:06
 */
public interface SchoolMapper {
    @Select("select * from tb_school")
    @ResultMap("com.wisd.dbs.mapper.SchoolMapper.SchoolResult")
    List<School> findAll();
}
