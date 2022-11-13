package com.example.weather.mapper;

import com.example.weather.VO.LocateWeather;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/12 12:52
 */
@Mapper
public interface WeatherMapper {
    public Integer newWeather(Long locate, String weather);

    public LocateWeather getTodayWeatherByLocate(Long locate, Timestamp time);

}
