package com.example.weather.service;

import com.alibaba.fastjson.JSONObject;
import com.example.weather.vo.UserWeather;
import com.example.weather.mapper.UserMapper;
import com.example.weather.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/11 22:47
 */
@Service
public class UserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Value(value = "${weather.key}")
    private String KEY = null;

    @Autowired
    private UserMapper userMapper;


    public Integer newUser(String name, Long target, Integer type, String city) {
        if (userMapper.countByTarget(target, type) == 1) {
            userMapper.deleteByTarget(target, type);
        }
        String cityResult = null;
        try {
            cityResult = HttpUtil.get(String.format("https://geoapi.qweather.com/v2/city/lookup?location=%s&key=%s", city, KEY));
        } catch (Exception e) {
            logger.error("newUserError", e);
            return 0;
        }
        Long locate = Long.parseLong(JSONObject.parseObject(cityResult).getJSONArray("location").getJSONObject(0).getString("id"));
        return userMapper.newUser(name, target, type, locate);
    }

    public UserWeather getUserWeatherById(Long id) {
        return userMapper.getUserWeatherById(id);
    }

    public List<UserWeather> getUserWeather() {
        return userMapper.getUserWeather();
    }

    public List<Long> getAllLocate() {
        return userMapper.getAllLocate();
    }

    public List<Long> getTargetsByLocate(Long locate) {
        return userMapper.getTargetsByLocate(locate);
    }

}
