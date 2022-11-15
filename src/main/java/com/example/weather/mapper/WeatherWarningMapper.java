package com.example.weather.mapper;

import com.example.weather.VO.WeatherWarning;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/15 18:28
 */
@Mapper
public interface WeatherWarningMapper {
    Integer newWeatherWarning(WeatherWarning weatherWarning);

    WeatherWarning getWeatherWarningById(String id);

    Integer updateWeatherWarning(String id);
}
