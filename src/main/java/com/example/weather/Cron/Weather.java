package com.example.weather.Cron;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.weather.VO.Message;
import com.example.weather.VO.MessageItem;
import com.example.weather.VO.UserWeather;
import com.example.weather.VO.WeatherHour;
import com.example.weather.service.UserService;
import com.example.weather.service.WeatherService;
import com.example.weather.util.HttpUtil;
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
public class Weather {

    private static Logger logger = LoggerFactory.getLogger(Weather.class);
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

    public List<WeatherHour> getTodayWeather(Long cityCode) throws IOException {
        String result = HttpUtil.get(String.format("https://devapi.qweather.com/v7/weather/24h?location=%s&key=%s", cityCode, KEY));
        logger.info(String.format("getTodayWeather:locate:%d,result:%s", cityCode, result));
        return JSONArray.parseArray(JSONObject.parseObject(result).getString("hourly"), WeatherHour.class);
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
            List<MessageItem> items = new ArrayList<>();
            MessageItem item = new MessageItem();
            item.setType(TEXT);
            String weatherJSON = weatherService.getTodayWeatherByLocate(userweather.getLocate()).getWeather();
            item.setText(WeatherUtil.buildWeatherString(weatherJSON));
            items.add(item);

            Message msg = new Message();
            msg.setTarget(userweather.getTarget());
            msg.setSessionKey(SESSION);
            msg.setMessageChain(items);

            if (userweather.getType() == 1) {
                String result = HttpUtil.post(DOMAIN + "/sendFriendMessage", msg);
                logger.info("SentFriendMessage: " + result);
            } else {
                String result = HttpUtil.post(DOMAIN + "/sendGroupMessage", msg);
                logger.info("SentGroupMessage: " + result);
            }

            logger.info(String.format("sendWeatherMessage:domain:%s,user:%s,locate:%d,message:%s", DOMAIN, userweather.getName(), userweather.getLocate(), msg));
        }
    }
//    @Scheduled(cron = "0/10 * * * * ?")
//    public void sendWeatherTest() throws IOException {
//        UserWeather userweather = userService.getUserWeatherById(9L);
//        List<MessageItem> items = new ArrayList<>();
//        MessageItem item = new MessageItem();
//        item.setType(TEXT);
//        String weatherJSON = weatherService.getTodayWeatherByLocate(userweather.getLocate()).getWeather();
//        item.setText(WeatherUtil.buildWeatherString(weatherJSON));
//        items.add(item);
//
//        Message msg = new Message();
//        msg.setTarget(userweather.getTarget());
//        msg.setSessionKey(SESSION);
//        msg.setMessageChain(items);
//
//        if (userweather.getType() == 1) {
//            String result = HttpUtil.post(DOMAIN + "/sendFriendMessage", msg);
//            logger.info("SentFriendMessage: " + result);
//        } else {
//            String result = HttpUtil.post(DOMAIN + "/sendGroupMessage", msg);
//            logger.info("SentGroupMessage: " + result);
//        }
//
//        logger.info(String.format("sendWeatherMessage:domain:%s,user:%s,locate:%d,message:%s", DOMAIN, userweather.getName(), userweather.getLocate(), msg));
//
//    }

    public static void main(String[] args) throws IOException {

    }

}
