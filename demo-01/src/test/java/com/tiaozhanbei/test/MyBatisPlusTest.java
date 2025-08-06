package com.tiaozhanbei.test;

import com.tiaozhanbei.mapper.InfoMapper;
import com.tiaozhanbei.mapper.UserMapper;
import com.tiaozhanbei.pojo.Info;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class MyBatisPlusTest {

    @Autowired
    private InfoMapper infoMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test01() throws IOException {
        //准备图片
        String image1Path = "src/test/resources/3.jpg";
        File image1File = new File(image1Path);
        String image2Path = "src/test/resources/4.jpg";
        File image2File = new File(image2Path);

        Info info = new Info();
        info.setPatientName("派大星");
        info.setPatientAge((short) 1);
        info.setPatientGender("男");
        info.setDiagnosis("无");
        info.setKeyword("无");
        info.setCreatedAt(LocalDateTime.now());
        FileInputStream iamge1InputStream = new FileInputStream(image1File);
        MockMultipartFile image1 = new MockMultipartFile("image1", image1File.getName(), "image/jpg", iamge1InputStream);
        FileInputStream iamge2InputStream = new FileInputStream(image2File);
        MockMultipartFile image2 = new MockMultipartFile("image2", image2File.getName(), "image/jpg", iamge2InputStream);
        info.setImage1(image1.getBytes());
        info.setImage1Type("jpg");
        info.setImage2(image2.getBytes());
        info.setImage2Type("jpg");
        int row = infoMapper.insertInfo(info);
        System.out.println(row);
//        try (FileInputStream input = new FileInputStream(image1File)){
//            //创建MultipartFile对象
//            MultipartFile multipartFile = new MockMultipartFile("image", image1File.getName(), "image/jpg", input);
//            //存储图片到数据库
//
//            info.setImage1(multipartFile.getBytes());
//            info.setImage1Type("png");
//            int row = infoMapper.insertInfo(info);
//            System.out.println(row);
//            //从数据库中取图片
//            Info info1 = infoMapper.selectById(1);
//            System.out.println("Image size: " + info1.getImage1().length);
//        }
    }

    @Test
    public void test02(){
        Info info = new Info();
//        info.setCreatedAt(LocalDateTime.parse("2025-07-24T14:21:54"));
        info.setCreatedAt(LocalDateTime.of(2025, 7, 24, 14, 21, 54));
        System.out.println(info.getCreatedAt());
        List<Info> infos = infoMapper.selectInfo(info);
        System.out.println(infos);
    }
}
