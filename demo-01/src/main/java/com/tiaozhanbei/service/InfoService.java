package com.tiaozhanbei.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiaozhanbei.pojo.Info;

import java.time.LocalDateTime;
import java.util.List;

public interface InfoService{

    int addInfo(Info info);

    int deleteInfo(Integer id);

    int deleteBatch(Info info);

    int updateInfo(Info info);

    List<Info> getAllInfo();

    Page<Info> getPageInfo(int size, int page,
                           String gender, Short age, String name, String keyword, String diagnosis, LocalDateTime createdAt);

    Info getInfoById(Integer id);
}
