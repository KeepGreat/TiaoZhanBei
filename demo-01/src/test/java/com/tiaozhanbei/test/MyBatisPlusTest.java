package com.tiaozhanbei.test;

import com.tiaozhanbei.mapper.InfoMapper;
import com.tiaozhanbei.mapper.UserMapper;
import com.tiaozhanbei.pojo.Info;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@SpringBootTest
public class MyBatisPlusTest {

    @Autowired
    private InfoMapper infoMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test01() throws IOException {
        //准备图片
        String imagePath = "src/test/resources/3.png";
        File imageFile = new File(imagePath);

        try (FileInputStream input = new FileInputStream(imageFile)){
            //创建MultipartFile对象
            MultipartFile multipartFile = new MockMultipartFile("image", imageFile.getName(), "image/jpg", input);
            //存储图片到数据库
            Info info = new Info();
            info.setImage(multipartFile.getBytes());
            info.setImageType("png");
            info.setPatientAge((short) 1);
            info.setPatientGender("男");
            info.setDiagnosis("无");
            info.setKeyword("无");
            info.setCreatedAt(LocalDateTime.now());
            int row = infoMapper.insertInfo(info);
            System.out.println(row);
            //从数据库中取图片
            Info info1 = infoMapper.selectById(1);
            System.out.println("Image size: " + info1.getImage().length);
        }
    }
}
