package com.example.weather.service;

import com.example.weather.VO.UserWeather;
import com.example.weather.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/11 22:47
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public Integer newUser(String name,Long target,Integer type,Long locate){
        return userMapper.newUser(name, target, type, locate);
    }

    public UserWeather getUserWeatherById(Long id){
        return userMapper.getUserWeatherById(id);
    }

    public List<UserWeather> getUserWeather(){
        return userMapper.getUserWeather();
    }

    public List<Long> getAllLocate(){
        return userMapper.getAllLocate();
    }

}
