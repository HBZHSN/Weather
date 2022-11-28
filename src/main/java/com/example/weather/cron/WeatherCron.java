package com.example.weather.cron;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.weather.vo.*;
import com.example.weather.service.MessageService;
import com.example.weather.service.UserService;
import com.example.weather.service.WeatherService;
import com.example.weather.service.WeatherWarningService;
import com.example.weather.util.HttpUtil;
import com.example.weather.util.WeatherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

@Component
@Configuration
@EnableAsync
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
    @Autowired
    private MessageService messageService;

    public List<WeatherHour> getTodayWeather(Long cityCode) throws Exception {
        String result = HttpUtil.get(String.format("https://devapi.qweather.com/v7/weather/24h?location=%s&key=%s", cityCode, KEY));
        logger.info(String.format("getTodayWeather:locate:%d,result:%s", cityCode, result));
        return JSONArray.parseArray(JSONObject.parseObject(result).getString("hourly"), WeatherHour.class);
    }

    public List<WeatherWarning> getNowWeatherWarning(Long cityCode) throws Exception {
        String result = HttpUtil.get(String.format("https://devapi.qweather.com/v7/warning/now?key=%s&location=%s", KEY, cityCode));
        logger.info(String.format("getNowWeatherWarning:locate:%d,result:%s", cityCode, result));
        return JSONArray.parseArray(JSONObject.parseObject(result).getString("warning"), WeatherWarning.class);
    }

    @Async
    @Scheduled(cron = "0 50 6,21 * * ?")
    public void getWeather() {
        List<Long> locates = userService.getAllLocate();
        for (Long locate : locates) {
            List<WeatherHour> weatherHours = new ArrayList<>();
            try {
                weatherHours = getTodayWeather(locate);
            } catch (Exception e) {
                logger.error("getTodayWeatherError", e);
                return;
            }
            weatherService.newWeather(locate, weatherHours);
        }
    }

    @Async
    @Scheduled(cron = "0 0 7,22 * * ?")
    public void sendWeather() {
        List<UserWeather> userWeathers = userService.getUserWeather();
        for (UserWeather userweather : userWeathers) {
            String weatherJSON = null;
            try {
                weatherJSON = weatherService.getTodayWeatherByLocate(userweather.getLocate()).getWeather();
            } catch (Exception e) {
                logger.error("sendWeatherError", e);
                return;
            }
            String resultWeather = WeatherUtil.buildWeatherString(weatherJSON);
//            MessageUtil.sendPlain(resultWeather, userweather.getTarget());
            messageService.newMessage(userweather.getTarget(), userweather.getType(), resultWeather);
        }
    }

    @Async
    @Scheduled(cron = "0 55 5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21 * * ?")
    public void saveWeatherWarning() {
        List<Long> locates = userService.getAllLocate();
        for (Long locate : locates) {
            List<WeatherWarning> weatherWarnings = new ArrayList<>();
            try {
                weatherWarnings = getNowWeatherWarning(locate);
            } catch (Exception e) {
                logger.error("getNowWeatherWarningError", e);
                return;
            }
            for (WeatherWarning weatherWarning : weatherWarnings) {
                if (weatherWarning.getId() != null) {
                    weatherWarning.setLocate(locate);
                    weatherWarning.setSendStatus(0);
                    weatherWarningService.newWeatherWarning(weatherWarning);
                }
            }
        }
    }

    @Async
    @Scheduled(cron = "0 0 6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22 * * ?")
    public void sendWeatherWarning() {
        List<Long> locates = userService.getAllLocate();
        for (Long locate : locates) {
            List<WeatherWarning> weatherWarnings = weatherWarningService.getTodayWeatherWarningByLocate(locate);
            for (WeatherWarning weatherWarning : weatherWarnings) {
                if (weatherWarning.getId() != null) {
                    weatherWarning.setLocate(locate);
                    weatherWarning.setSendStatus(0);
                    if (weatherWarningService.newWeatherWarning(weatherWarning) == 1) {
                        weatherWarningService.updateWeatherWarning(weatherWarning.getId());
                        logger.info(JSON.toJSONString(weatherWarnings));
                        List<Long> targets = userService.getTargetsByLocate(locate);
                        for (Long target : targets) {
                            String resultString = WeatherUtil.buildWeatherWarningString(weatherWarnings);
//                            MessageUtil.sendPlain(resultString, target);
                            messageService.newMessage(target, 1, resultString);
                        }
                    }
                }
            }
        }
    }

//    @Async
//    @Scheduled(cron = "0/10 * * * * ? ")
//    public void test() throws IOException {
//        messageService.newMessage(1149983457L,1,WeatherUtil.buildWeatherWarningString(weatherWarningService.getTodayWeatherWarningByLocate(101210111L)));
//    }

    public static void main(String[] args) throws IOException {
    }

}
