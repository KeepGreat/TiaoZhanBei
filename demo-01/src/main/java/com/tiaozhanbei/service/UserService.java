package com.tiaozhanbei.service;

import com.tiaozhanbei.pojo.User;

import java.util.List;

public interface UserService {

    int addUser(User user);

    int deleteUser(Integer id);

    int updateUser(User user);

    User selectUserById(Integer id);

    List<User> selectAll();

    Boolean login(String username, String password);
}
