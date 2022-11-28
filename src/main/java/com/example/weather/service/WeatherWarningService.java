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
            List<WeatherWarning> todayWarning = getTodayWeatherWarningByLocate(warning.getLocate());
            for(WeatherWarning today : todayWarning){//相同的预警类型一天只发一次
                if(Objects.equals(today.getType(), weatherWarning.getType())){
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

    public List<WeatherWarning> getTodayWeatherWarningByLocate(Long locate){
        Timestamp time = new Timestamp(System.currentTimeMillis() - 1000 * 60 * 30);
        List<WeatherWarning> weatherWarnings = weatherWarningMapper.getTodayWeatherWarningByLocate(locate, time);
        if (weatherWarnings == null) { //如果少了信息，就再去查一遍
            time = new Timestamp(System.currentTimeMillis());
            logger.info("数据库中无数据，补偿一次查询");
            List<WeatherWarning> weatherWarningList = new ArrayList<>();
            try {
                String result = HttpUtil.get(String.format("https://devapi.qweather.com/v7/warning/now?key=%s&location=%s", KEY, locate));
                logger.info(String.format("getNowWeatherWarning:locate:%d,result:%s", locate, result));
                weatherWarningList = JSONArray.parseArray(JSONObject.parseObject(result).getString("warning"), WeatherWarning.class);
            }catch (Exception e){
                logger.error("getNowWeatherWarningError:",e);
            }
            for (WeatherWarning weatherWarning : weatherWarningList) {
                newWeatherWarning(weatherWarning);
            }
            weatherWarnings = weatherWarningMapper.getTodayWeatherWarningByLocate(locate, time);
        }
        return weatherWarnings;
    }
}
