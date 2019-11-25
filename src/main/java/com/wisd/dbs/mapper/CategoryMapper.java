package com.wisd.dbs.mapper;

import com.wisd.dbs.bean.Category;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/9/3 8:52
 */
public interface CategoryMapper {
    @Select("select * from tb_category where parent_id=0")
    @ResultMap("com.wisd.dbs.mapper.CategoryMapper.CategoryResult")
    List<Category> findAllTops();

    @Select("select * from tb_category where parent_id=#{param1} order by sorted")
    @ResultMap("com.wisd.dbs.mapper.CategoryMapper.CategoryResult")
    List<Category> findByParentIdOrderedBySorted(String id);

    @Select("select * from tb_category where parent_id=#{param1} order by sorted")
    @ResultMap("com.wisd.dbs.mapper.CategoryMapper.CategoryResult")
    List<Category> findByParentId(Integer queryType);
}
