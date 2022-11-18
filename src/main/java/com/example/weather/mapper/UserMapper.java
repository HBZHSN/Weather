package com.example.weather.mapper;

import com.example.weather.vo.UserWeather;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/11 22:47
 */
@Mapper
public interface UserMapper {
    Integer newUser(String name, Long target, Integer type, Long locate);

    List<UserWeather> getUserWeather();

    UserWeather getUserWeatherById(Long id);

    List<Long> getAllLocate();

    Integer countByTarget(Long target, Integer type);

    void deleteByTarget(Long target, Integer type);

    List<Long> getTargetsByLocate(Long locate);
}
