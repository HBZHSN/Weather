package com.example.weather.mapper;

import com.example.weather.vo.WeatherWarning;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

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

    List<WeatherWarning> getTodayWeatherWarningByLocate(Long locate, Timestamp time);
}
