package com.tiaozhanbei.pojo.dto;

import com.tiaozhanbei.pojo.User;
import lombok.Data;

@Data
public class UpdateUserDTO {
    private User user;
    private String oldPassword;
}
