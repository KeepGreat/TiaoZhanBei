package com.tiaozhanbei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tiaozhanbei.pojo.Info;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 增方法中主键不用指定，其他字段不能为空
 * 删查改方法中，传参对象中需要查看的字段赋值，其余字段赋空值
 */
public interface InfoMapper extends BaseMapper<Info> {
    int insertInfo(Info info);

    int deleteInfo(Info info);

    List<Info> selectInfo(Info info);

    List<Info> selectAll();

    int updateInfo(Info info);

    IPage<Info> selectPageInfo(IPage<?> page,
                               @Param("gender") String gender, @Param("age") Short age, @Param("name") String name,
                               @Param("keyword") String keyword, @Param("diagnosis") String diagnosis,
                               @Param("queryStart") LocalDateTime queryStart, @Param("queryEnd") LocalDateTime queryEnd);
}
