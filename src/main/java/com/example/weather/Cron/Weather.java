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
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public static final String KEY = "99c16aa017844e5fa4bd6531c41516ab";
    public static final String SESSION = "hV2eBIK1";
    public static final String TEXT = "Plain";

    @Autowired
    private UserService userService;
    @Autowired
    private WeatherService weatherService;

    public static List<WeatherHour> getTodayWeather(Long cityCode) throws IOException {
        HttpGet get = new HttpGet(String.format("https://devapi.qweather.com/v7/weather/24h?location=%s&key=%s", cityCode, KEY));
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(get);
        String result = EntityUtils.toString(response.getEntity());
        logger.info(String.format("getTodayWeather:locate:%d,result:%s", cityCode, result));
        return JSONArray.parseArray(JSONObject.parseObject(result).getString("hourly"), WeatherHour.class);
    }

    @Scheduled(cron = "0 5 0,21 * * ?")
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
            item.setText(WeatherUtil.checkWeather(weatherJSON));
            items.add(item);

            Message msg = new Message();
            msg.setTarget(userweather.getTarget());
            msg.setSessionKey(SESSION);
            msg.setMessageChain(items);

            if (userweather.getTarget() == 1) {
                HttpUtil.post("http://124.71.143.134:8081/sendFriendMessage", msg);
            } else {
                HttpUtil.post("http://124.71.143.134:8081/sendGroupMessage", msg);
            }

            logger.info(String.format("sendWeatherMessage:user:%s,locate:%d,message:%s", userweather.getName(), userweather.getLocate(), msg));
        }
    }

    public static void main(String[] args) throws IOException {
        List<WeatherHour> weatherHours = getTodayWeather(101210101L);
        Message message = new Message();
        message.setSessionKey("hV2eBIK1");
        message.setTarget(1149983457L);
        MessageItem messageItem = new MessageItem();
        messageItem.setType("Plain");
        messageItem.setText(weatherHours.toString());
        List<MessageItem> messageItemList = new ArrayList<>();
        messageItemList.add(messageItem);
        message.setMessageChain(messageItemList);
        HttpUtil.post("http://124.71.143.134:8081/sendFriendMessage", message);
    }

}
