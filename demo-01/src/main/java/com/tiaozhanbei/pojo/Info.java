package com.tiaozhanbei.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Info {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private byte[] image;
    private String imageType;
    private String patientGender;
    private Short patientAge;
    private String keyword;
    private String diagnosis;
    private LocalDateTime createdAt;
}
