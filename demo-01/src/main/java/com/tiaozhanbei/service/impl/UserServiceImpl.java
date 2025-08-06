package com.tiaozhanbei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tiaozhanbei.mapper.UserMapper;
import com.tiaozhanbei.pojo.User;
import com.tiaozhanbei.pojo.dto.UpdateUserDTO;
import com.tiaozhanbei.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int addUser(User user) {
        List<User> list = userMapper.selectList(new QueryWrapper<User>().eq("username", user.getUsername()));
        if (!list.isEmpty()) {
            return -1;
        }
        int row = userMapper.insertUser(user);
        return row;
    }

    @Override
    public int deleteUser(Integer id) {
        int delete = userMapper.delete(new QueryWrapper<User>().eq("id", id));
        return delete;
    }

    @Override
    public int updateUser(UpdateUserDTO userDTO) {
        String oldPassword = userDTO.getOldPassword();
        User user = userMapper.selectById(userDTO.getUser().getId());
        if (!user.getPassword().equals(oldPassword)) return -1;
        User newUser = userDTO.getUser();
        int row = userMapper.updateById(newUser);
        return row;
    }

    @Override
    public User selectUserById(Integer id) {
        User user = userMapper.selectById(id);
        return user;
    }

    @Override
    public List<User> selectAll() {
        List<User> list = userMapper.selectAll();
        return list;
    }

    @Override
    public User login(String username, String password) {
        List<User> list = userMapper.selectList(new QueryWrapper<User>().eq("username", username)
                .eq("password", password));
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
