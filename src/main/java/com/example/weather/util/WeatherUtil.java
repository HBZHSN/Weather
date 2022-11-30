package com.example.weather.util;

import com.alibaba.fastjson.JSONArray;
import com.example.weather.vo.WeatherHour;
import com.example.weather.vo.WeatherWarning;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/12 13:48
 */
public class WeatherUtil {
    private static final Map<Long, String> ICON_MAP = new HashMap<Long, String>() {
        {
            this.put(100L, "晴");
            this.put(101L, "多云");
            this.put(102L, "少云");
            this.put(103L, "晴间多云");
            this.put(104L, "阴");
            this.put(150L, "晴");
            this.put(151L, "多云");
            this.put(152L, "少云");
            this.put(153L, "晴间多云");
            this.put(300L, "阵雨");
            this.put(301L, "强阵雨");
            this.put(302L, "雷阵雨");
            this.put(303L, "强雷阵雨");
            this.put(304L, "雷阵雨伴有冰雹");
            this.put(305L, "小雨");
            this.put(306L, "中雨");
            this.put(307L, "大雨");
            this.put(308L, "极端降雨");
            this.put(309L, "毛毛雨");
            this.put(310L, "暴雨");
            this.put(311L, "大暴雨");
            this.put(312L, "特大暴雨");
            this.put(313L, "冻雨");
            this.put(314L, "小到中雨");
            this.put(315L, "中到大雨");
            this.put(316L, "大到暴雨");
            this.put(317L, "暴雨到大暴雨");
            this.put(318L, "大暴雨到特大暴雨");
            this.put(350L, "阵雨");
            this.put(351L, "强阵雨");
            this.put(399L, "雨");
            this.put(400L, "小雪");
            this.put(401L, "中雪");
            this.put(402L, "大雪");
            this.put(403L, "暴雪");
            this.put(404L, "雨夹雪");
            this.put(405L, "雨雪天气");
            this.put(406L, "阵雨夹雪");
            this.put(407L, "阵雪");
            this.put(408L, "小到中雪");
            this.put(409L, "中到大雪");
            this.put(410L, "大到暴雪");
            this.put(456L, "阵雨夹雪");
            this.put(457L, "阵雪");
            this.put(499L, "雪");
            this.put(500L, "薄雾");
            this.put(501L, "雾");
            this.put(502L, "霾");
            this.put(503L, "扬沙");
            this.put(504L, "浮尘");
            this.put(507L, "沙尘暴");
            this.put(508L, "强沙尘暴");
            this.put(509L, "浓雾");
            this.put(510L, "强浓雾");
            this.put(511L, "中度霾");
            this.put(512L, "重度霾");
            this.put(513L, "严重霾");
            this.put(514L, "大雾");
            this.put(515L, "特强浓雾");
            this.put(800L, "新月");
            this.put(801L, "蛾眉月");
            this.put(802L, "上弦月");
            this.put(803L, "盈凸月");
            this.put(804L, "满月");
            this.put(805L, "亏凸月");
            this.put(806L, "下弦月");
            this.put(807L, "残月");
            this.put(900L, "热");
            this.put(901L, "冷");
            this.put(999L, "未知");

        }
    };

    public static String buildWeatherString(String weatherJSON) {
        StringBuilder result = new StringBuilder("未来24小时天气情况：\n");
        List<WeatherHour> weatherHours = JSONtoWeather(weatherJSON);
        List<Double> temps = weatherHours.stream().map(WeatherHour::getTemp).sorted().collect(Collectors.toList());

        result.append(String.format("气温：%.0f度-%.0f度\n", temps.get(0), temps.get(23)));
        String flag = weatherHours.get(0).getText();
        String firstTime = weatherHours.get(0).getFxTime().substring(11, 16);
        for (int i = 0; i < weatherHours.size(); i++) {
            WeatherHour weatherHour = weatherHours.get(i);
            String time = weatherHour.getFxTime().substring(11, 16);
            if (!Objects.equals(weatherHour.getText(), flag) || i == weatherHours.size() - 1 || time.startsWith("00")) {
                result.append(firstTime).append("—").append(time).append("：").append(flag).append("\n");
                if (time.startsWith("00") && i != weatherHours.size() - 1) {
                    result.append("次日：\n");
                }
                flag = weatherHour.getText();
                firstTime = time;
            }
        }

        if (weatherJSON.contains("雨")) {
            result.append("最近24小时有雨，出门记得带伞。");
        } else if (weatherJSON.contains("雪")) {
            result.append("最近24小时有雪，出门玩雪吧！");
        }
        return result.toString();
    }

    public static String buildWeatherWarningString(List<WeatherWarning> weatherWarnings) {
        if (weatherWarnings.size() == 0) return null;
        StringBuilder result = new StringBuilder("———天气预警信息———\n");
        for (WeatherWarning weatherWarn : weatherWarnings) {
            result.append("⭐").append(weatherWarn.getTitle()).append("\n");
        }
        result.append("查看详情：").append("https://www.qweather.com/severe-weather/").append(weatherWarnings.get(0).getLocate()).append(".html");
        return result.toString();
    }

    public static List<WeatherHour> JSONtoWeather(String JSON) {
        return JSONArray.parseArray(JSON, WeatherHour.class);
    }

    public static void main(String[] args) {
    }
}
