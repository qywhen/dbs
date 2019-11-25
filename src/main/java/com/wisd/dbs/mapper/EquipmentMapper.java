package com.wisd.dbs.mapper;

import com.wisd.dbs.bean.Endpoint;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/20 13:57
 */
@Repository
public interface EquipmentMapper {
    @Select("select * from tb_equipment where no=#{param1}")
    @ResultMap("com.wisd.dbs.mapper.EquipmentMapper.EquipmentResult")
    Endpoint findById(Integer epid);
}
