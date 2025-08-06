package com.tiaozhanbei.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiaozhanbei.pojo.Info;
import com.tiaozhanbei.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/info")
@CrossOrigin
public class InfoController {

    @Autowired
    private InfoService infoService;

    @PostMapping()
    public String addInfo(@RequestBody Info info){
        int row = infoService.addInfo(info);
        if (row == 0) return "添加记录失败";
        return "添加记录成功";
    }

    @DeleteMapping("/{id}")
    public String deleteInfo(@PathVariable Integer id){
        int row = infoService.deleteInfo(id);
        if (row == 0) return "删除记录失败";
        return "删除记录成功";
    }

    @PostMapping("/delete")
    public String deleteBatch(@RequestBody Info info){
        int row = infoService.deleteBatch(info);
        if (row == 0) return "删除记录失败";
        return "删除记录成功";
    }

    @PutMapping
    public String updateInfo(@RequestBody Info info){
        int row = infoService.updateInfo(info);
        if (row == 0) return "更新记录失败";
        return "更新记录成功";
    }

    @GetMapping
    public List<Info> getAllInfo(){
        List<Info> allInfo = infoService.getAllInfo();
        return allInfo;
    }

    @GetMapping("/{page}/{size}")
    public Page<Info> getPageInfo(@PathVariable("page") int pageNo,
                                  @PathVariable("size") int pageSize,
                                  @RequestParam(required = false) String patientGender,
                                  @RequestParam(required = false) Short patientAge,
                                  @RequestParam(required = false) String patientName,
                                  @RequestParam(required = false) String keyword,
                                  @RequestParam(required = false) String diagnosis,
                                  @RequestParam(required = false) LocalDateTime queryStart,
                                  @RequestParam(required = false) LocalDateTime queryEnd){
        Page<Info> pageInfo = infoService.getPageInfo(pageNo, pageSize,
                patientGender, patientAge, patientName, keyword, diagnosis, queryStart, queryEnd);
        return pageInfo;
    }

    @GetMapping("/{id}")
    public Info getInfoById(@PathVariable Integer id){
        Info infoById = infoService.getInfoById(id);
        return infoById;
    }
}
