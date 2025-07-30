package com.tiaozhanbei.controller;

import com.tiaozhanbei.pojo.User;
import com.tiaozhanbei.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Boolean login(@RequestBody User user){
        String username = user.getUsername();
        String password = user.getPassword();
        return userService.login(username, password);
    }

    @PostMapping
    public String addUser(@RequestBody User user) {
        int row = userService.addUser(user);
        if (row == 0) return "增加用户失败";
        return "增加用户成功";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Integer id) {
        int row = userService.deleteUser(id);
        if (row == 0) return "删除用户失败";
        return "删除用户成功";
    }

    @PutMapping
    public String updateUser(@RequestBody User user) {
        int row = userService.updateUser(user);
        if (row == 0) return "更新用户失败";
        return "更新用户成功";
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id){
        User user = userService.selectUserById(id);
        return user;
    }

    @GetMapping
    public List<User> getAllUser(){
        List<User> list = userService.selectAll();
        return list;
    }
}
