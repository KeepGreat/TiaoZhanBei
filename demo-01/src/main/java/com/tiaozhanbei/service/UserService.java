package com.tiaozhanbei.service;

import com.tiaozhanbei.pojo.User;
import com.tiaozhanbei.pojo.dto.UpdateUserDTO;

import java.util.List;

public interface UserService {

    int addUser(User user);

    int deleteUser(Integer id);

    int updateUser(UpdateUserDTO userDTO);

    User selectUserById(Integer id);

    List<User> selectAll();

    User login(String username, String password);
}
