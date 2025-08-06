package com.tiaozhanbei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiaozhanbei.mapper.InfoMapper;
import com.tiaozhanbei.pojo.Info;
import com.tiaozhanbei.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    private InfoMapper infoMapper;

    @Override
    public int addInfo(Info info) {
        int row = infoMapper.insertInfo(info);
        return row;
    }

    @Override
    public int deleteInfo(Integer id) {
        int delete = infoMapper.delete(new QueryWrapper<Info>().eq("id", id));
        return delete;
    }

    @Override
    public int deleteBatch(Info info) {
        int row = infoMapper.deleteInfo(info);
        return row;
    }

    @Override
    public int updateInfo(Info info) {
        int row = infoMapper.updateInfo(info);
        return row;
    }

    @Override
    public List<Info> getAllInfo() {
        List<Info> infos = infoMapper.selectAll();
        return infos;
    }

    @Override
    public Page<Info> getPageInfo(int page, int size,
                                  String gender, Short age, String name, String keyword, String diagnosis, LocalDateTime createdAt) {
        QueryWrapper<Info> infoQueryWrapper = new QueryWrapper<>();
        if (name != null) infoQueryWrapper.like("patient_name", name);
        if (gender != null) infoQueryWrapper.eq("patient_gender", gender);
        if (age != null) infoQueryWrapper.eq("patient_age", age);
        if (keyword != null) infoQueryWrapper.like("keyword", keyword);
        if (diagnosis != null) infoQueryWrapper.like("diagnosis", diagnosis);
        if (createdAt != null) infoQueryWrapper.eq("created_at", createdAt);
        Page<Info> infoPage = infoMapper.selectPage(new Page<>(page, size), infoQueryWrapper);
        return infoPage;
    }

    @Override
    public Info getInfoById(Integer id) {
        Info info = infoMapper.selectById(id);
        return info;
    }


}
