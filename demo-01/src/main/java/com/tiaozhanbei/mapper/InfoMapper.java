package com.tiaozhanbei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiaozhanbei.pojo.Info;

import java.util.List;

/**
 * 增方法中主键不用指定，其他字段不能为空
 * 删查改方法中，传参对象中需要查看的字段赋值，其余字段赋空值
 */
public interface InfoMapper extends BaseMapper<Info> {
    int insertInfo(Info info);

    int deleteInfo(Info info);

    List<Info> selectInfo(Info info);

    int updateInfo(Info info);
}
