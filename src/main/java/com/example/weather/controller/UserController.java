package com.example.weather.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.weather.service.UserService;
import com.example.weather.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/11 22:46
 */
@RestController
@RequestMapping("/user/weather")
public class UserController {
    @Autowired
    private UserService userService;
    @Value(value = "${weather.key}")
    private String KEY = null;

    @GetMapping("/newUser")
    public Integer newUser(@RequestParam(value = "name") String name,
                           @RequestParam(value = "target") Long target,
                           @RequestParam(value = "type") Integer type,
                           @RequestParam(value = "city") String city) throws IOException {
        return userService.newUser(name, target, type, city);
    }

}
