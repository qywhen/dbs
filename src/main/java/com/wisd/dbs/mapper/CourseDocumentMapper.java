package com.wisd.dbs.mapper;

import com.github.pagehelper.Page;
import com.wisd.dbs.bean.CourseDocument;
import com.wisd.dbs.bean.CourseQueryVo;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/7/9 11:16
 */
@Repository
public interface CourseDocumentMapper {
    int findCount(CourseQueryVo courseQueryVo);

    Page<CourseDocument> findPage(CourseQueryVo courseQueryVo);
}
