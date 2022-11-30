package com.example.weather.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.weather.util.HttpUtil;
import com.example.weather.vo.LocateWeather;
import com.example.weather.vo.WeatherHour;
import com.example.weather.vo.WeatherWarning;
import com.example.weather.mapper.WeatherWarningMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/15 18:28
 */
@Service
public class WeatherWarningService {
    private Logger logger = LoggerFactory.getLogger(WeatherWarningService.class);
    @Autowired
    private WeatherWarningMapper weatherWarningMapper;

    @Value(value = "${weather.key}")
    private String KEY = null;

    public Integer newWeatherWarning(WeatherWarning weatherWarning) {
        WeatherWarning warning = getWeatherWarningById(weatherWarning.getId());
        if (warning == null) {
            logger.info(weatherWarning.toString());
            List<WeatherWarning> todayWarning = getTodayWeatherWarningByLocate(weatherWarning.getLocate());
            for (WeatherWarning today : todayWarning) {//相同的预警类型一天只发一次
                if (Objects.equals(today.getType(), weatherWarning.getType())) {
                    return 0;
                }
            }
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

    public List<WeatherWarning> getTodayWeatherWarningByLocate(Long locate) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Timestamp time = new Timestamp(c.getTimeInMillis());
        List<WeatherWarning> weatherWarnings = weatherWarningMapper.getTodayWeatherWarningByLocate(locate, time);
        return weatherWarnings;
    }
}
