package com.tiaozhanbei.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Info {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private byte[] image1;
    private String image1Type;
    private byte[] image2;
    private String image2Type;
    private String patientGender;
    private Short patientAge;
    private String patientName;
    private String keyword;
    private String diagnosis;
    //格式：yyyy-mm-ddThh:mm:ss
    private LocalDateTime createdAt;
}
