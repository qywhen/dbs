package com.wisd.dbs.mapper;

import com.wisd.dbs.bean.LiveRoom;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/18 14:59
 */
@Repository
public interface LiveRoomMapper {
    @Select("select * from tb_classroom where no=#{param1}")
    @ResultMap("com.wisd.dbs.mapper.LiveRoomMapper.LiveRoomResult")
    LiveRoom findByRoomId(Integer roomid);
}
