package com.example.weather.service;

import com.example.weather.vo.WeatherWarning;
import com.example.weather.mapper.WeatherWarningMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/15 18:28
 */
@Service
public class WeatherWarningService {
    @Autowired
    private WeatherWarningMapper weatherWarningMapper;

    public Integer newWeatherWarning(WeatherWarning weatherWarning) {
        WeatherWarning warning = getWeatherWarningById(weatherWarning.getId());
        if (warning == null) {
            weatherWarning.setSendStatus(0);
            return weatherWarningMapper.newWeatherWarning(weatherWarning);
        }
        return 0;
    }

    public WeatherWarning getWeatherWarningById(String id) {
        return weatherWarningMapper.getWeatherWarningById(id);
    }

    public Integer updateWeatherWarning(String id) {
        return weatherWarningMapper.updateWeatherWarning(id);
    }
}
