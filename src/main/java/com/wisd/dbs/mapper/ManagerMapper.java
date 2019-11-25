package com.wisd.dbs.mapper;

import com.wisd.dbs.bean.Manager;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/8/8 10:04
 */
@Repository
public interface ManagerMapper {
    @Select("select  * from tb_manager where user_name=#{param1}")
    @ResultMap("com.wisd.dbs.mapper.ManagerMapper.ManagerResult")
    Manager findByUserName(String userName);
}
