package com.example.weather.Cron;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.weather.VO.*;
import com.example.weather.service.UserService;
import com.example.weather.service.WeatherService;
import com.example.weather.service.WeatherWarningService;
import com.example.weather.util.HttpUtil;
import com.example.weather.util.MessageUtil;
import com.example.weather.util.WeatherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/11 16:47
 */

@Configuration
@EnableScheduling
public class WeatherCron {

    private static Logger logger = LoggerFactory.getLogger(WeatherCron.class);
    private static final String TEXT = "Plain";

    @Value(value = "${weather.domain}")
    private String DOMAIN = null;
    @Value(value = "${weather.session}")
    private String SESSION = null;
    @Value(value = "${weather.key}")
    private String KEY = null;

    @Autowired
    private UserService userService;
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private WeatherWarningService weatherWarningService;

    public List<WeatherHour> getTodayWeather(Long cityCode) throws IOException {
        String result = HttpUtil.get(String.format("https://devapi.qweather.com/v7/weather/24h?location=%s&key=%s", cityCode, KEY));
        logger.info(String.format("getTodayWeather:locate:%d,result:%s", cityCode, result));
        return JSONArray.parseArray(JSONObject.parseObject(result).getString("hourly"), WeatherHour.class);
    }

    public List<WeatherWarning> getNowWeatherWarning(Long cityCode) throws IOException {
        String result = HttpUtil.get(String.format("https://devapi.qweather.com/v7/warning/now?key=%s&location=%s", KEY, cityCode));
        logger.info(String.format("getNowWeatherWarning:locate:%d,result:%s", cityCode, result));
        return JSONArray.parseArray(JSONObject.parseObject(result).getString("warning"), WeatherWarning.class);
    }

    @Scheduled(cron = "0 5 6,21 * * ?")
    public void getWeather() throws IOException {
        List<Long> locates = userService.getAllLocate();
        for (Long locate : locates) {
            List<WeatherHour> weatherHours = getTodayWeather(locate);
            weatherService.newWeather(locate, weatherHours);
        }
    }

    @Scheduled(cron = "0 0 7,22 * * ?")
    public void sendWeather() throws IOException {
        List<UserWeather> userWeathers = userService.getUserWeather();
        for (UserWeather userweather : userWeathers) {
            String weatherJSON = weatherService.getTodayWeatherByLocate(userweather.getLocate()).getWeather();
            MessageUtil.sendPlain(WeatherUtil.buildWeatherString(weatherJSON), userweather.getTarget());
        }
    }

    @Scheduled(cron = "0 0/30 6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22 * * ?")
    public void sendWeatherWarning() throws IOException {
        List<Long> locates = userService.getAllLocate();
        for (Long locate : locates) {
            List<WeatherWarning> weatherWarnings = getNowWeatherWarning(locate);
            for (WeatherWarning weatherWarning : weatherWarnings) {
                if (weatherWarning.getId() != null) {
                    weatherWarning.setLocate(locate);
                    weatherWarning.setSendStatus(0);
                    if (weatherWarningService.newWeatherWarning(weatherWarning) == 1) {
                        weatherWarningService.updateWeatherWarning(weatherWarning.getId());
                        logger.info(JSON.toJSONString(weatherWarnings));
                        List<Long> targets = userService.getTargetsByLocate(locate);
                        for (Long target : targets) {
                            MessageUtil.sendPlain(WeatherUtil.buildWeatherWarningString(weatherWarnings), target);
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {

    }

}
