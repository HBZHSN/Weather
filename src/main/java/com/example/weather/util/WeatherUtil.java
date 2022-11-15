package com.example.weather.util;

import com.alibaba.fastjson.JSONArray;
import com.example.weather.VO.WeatherHour;
import com.example.weather.VO.WeatherWarning;

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
        Boolean nextDay = false;
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
        StringBuilder result = new StringBuilder("！！！天气预警信息！！！\n");
        for (WeatherWarning weatherWarn : weatherWarnings) {
            result.append(weatherWarn.getText()).append("\n");
        }
        return result.toString();
    }

    public static List<WeatherHour> JSONtoWeather(String JSON) {
        return JSONArray.parseArray(JSON, WeatherHour.class);
    }

    public static void main(String[] args) {
//        System.out.println(buildWeatherString("[{\"dew\":14.0,\"fxTime\":\"2022-11-13T01:00+08:00\",\"humidity\":62.0,\"icon\":151,\"pop\":43.0,\"precip\":0.0,\"pressure\":1022,\"temp\":22.0,\"text\":\"多云\",\"wind360\":328,\"windDir\":\"西北风\",\"windScale\":\"4-5\",\"windSpeed\":29.0},{\"dew\":14.0,\"fxTime\":\"2022-11-13T02:00+08:00\",\"humidity\":62.0,\"icon\":151,\"pop\":47.0,\"precip\":0.0,\"pressure\":1022,\"temp\":21.0,\"text\":\"多云\",\"wind360\":325,\"windDir\":\"西北风\",\"windScale\":\"4-5\",\"windSpeed\":31.0},{\"dew\":13.0,\"fxTime\":\"2022-11-13T03:00+08:00\",\"humidity\":81.0,\"icon\":305,\"pop\":51.0,\"precip\":0.3,\"pressure\":1022,\"temp\":16.0,\"text\":\"小雨\",\"wind360\":329,\"windDir\":\"西北风\",\"windScale\":\"4-5\",\"windSpeed\":33.0},{\"dew\":12.0,\"fxTime\":\"2022-11-13T04:00+08:00\",\"humidity\":78.0,\"icon\":305,\"pop\":53.0,\"precip\":0.3,\"pressure\":1021,\"temp\":16.0,\"text\":\"小雨\",\"wind360\":336,\"windDir\":\"西北风\",\"windScale\":\"5-6\",\"windSpeed\":35.0},{\"dew\":11.0,\"fxTime\":\"2022-11-13T05:00+08:00\",\"humidity\":84.0,\"icon\":151,\"pop\":45.0,\"precip\":0.0,\"pressure\":1020,\"temp\":14.0,\"text\":\"多云\",\"wind360\":342,\"windDir\":\"西北风\",\"windScale\":\"5-6\",\"windSpeed\":35.0},{\"dew\":10.0,\"fxTime\":\"2022-11-13T06:00+08:00\",\"humidity\":81.0,\"icon\":305,\"pop\":53.0,\"precip\":0.3,\"pressure\":1020,\"temp\":14.0,\"text\":\"小雨\",\"wind360\":347,\"windDir\":\"西北风\",\"windScale\":\"4-5\",\"windSpeed\":33.0},{\"dew\":8.0,\"fxTime\":\"2022-11-13T07:00+08:00\",\"humidity\":59.0,\"icon\":101,\"pop\":25.0,\"precip\":0.0,\"pressure\":1020,\"temp\":16.0,\"text\":\"多云\",\"wind360\":351,\"windDir\":\"北风\",\"windScale\":\"3-4\",\"windSpeed\":22.0},{\"dew\":7.0,\"fxTime\":\"2022-11-13T08:00+08:00\",\"humidity\":57.0,\"icon\":101,\"pop\":20.0,\"precip\":0.0,\"pressure\":1020,\"temp\":16.0,\"text\":\"多云\",\"wind360\":356,\"windDir\":\"北风\",\"windScale\":\"3-4\",\"windSpeed\":20.0},{\"dew\":7.0,\"fxTime\":\"2022-11-13T09:00+08:00\",\"humidity\":55.0,\"icon\":101,\"pop\":20.0,\"precip\":0.0,\"pressure\":1021,\"temp\":16.0,\"text\":\"多云\",\"wind360\":355,\"windDir\":\"北风\",\"windScale\":\"3-4\",\"windSpeed\":20.0},{\"dew\":7.0,\"fxTime\":\"2022-11-13T10:00+08:00\",\"humidity\":53.0,\"icon\":101,\"pop\":20.0,\"precip\":0.0,\"pressure\":1022,\"temp\":16.0,\"text\":\"多云\",\"wind360\":351,\"windDir\":\"北风\",\"windScale\":\"3-4\",\"windSpeed\":18.0},{\"dew\":6.0,\"fxTime\":\"2022-11-13T11:00+08:00\",\"humidity\":51.0,\"icon\":101,\"pop\":20.0,\"precip\":0.0,\"pressure\":1022,\"temp\":17.0,\"text\":\"多云\",\"wind360\":348,\"windDir\":\"西北风\",\"windScale\":\"3-4\",\"windSpeed\":20.0},{\"dew\":6.0,\"fxTime\":\"2022-11-13T12:00+08:00\",\"humidity\":50.0,\"icon\":101,\"pop\":25.0,\"precip\":0.0,\"pressure\":1022,\"temp\":17.0,\"text\":\"多云\",\"wind360\":348,\"windDir\":\"西北风\",\"windScale\":\"3-4\",\"windSpeed\":20.0},{\"dew\":6.0,\"fxTime\":\"2022-11-13T13:00+08:00\",\"humidity\":57.0,\"icon\":101,\"pop\":49.0,\"precip\":0.0,\"pressure\":1022,\"temp\":14.0,\"text\":\"多云\",\"wind360\":349,\"windDir\":\"西北风\",\"windScale\":\"3-4\",\"windSpeed\":20.0},{\"dew\":5.0,\"fxTime\":\"2022-11-13T14:00+08:00\",\"humidity\":55.0,\"icon\":305,\"pop\":53.0,\"precip\":0.3,\"pressure\":1022,\"temp\":14.0,\"text\":\"小雨\",\"wind360\":349,\"windDir\":\"西北风\",\"windScale\":\"3-4\",\"windSpeed\":20.0},{\"dew\":5.0,\"fxTime\":\"2022-11-13T15:00+08:00\",\"humidity\":55.0,\"icon\":305,\"pop\":53.0,\"precip\":0.3,\"pressure\":1022,\"temp\":14.0,\"text\":\"小雨\",\"wind360\":347,\"windDir\":\"西北风\",\"windScale\":\"3-4\",\"windSpeed\":20.0},{\"dew\":5.0,\"fxTime\":\"2022-11-13T16:00+08:00\",\"humidity\":55.0,\"icon\":101,\"pop\":48.0,\"precip\":0.0,\"pressure\":1022,\"temp\":14.0,\"text\":\"多云\",\"wind360\":346,\"windDir\":\"西北风\",\"windScale\":\"3-4\",\"windSpeed\":22.0},{\"dew\":5.0,\"fxTime\":\"2022-11-13T17:00+08:00\",\"humidity\":56.0,\"icon\":101,\"pop\":47.0,\"precip\":0.0,\"pressure\":1022,\"temp\":14.0,\"text\":\"多云\",\"wind360\":345,\"windDir\":\"西北风\",\"windScale\":\"3-4\",\"windSpeed\":20.0},{\"dew\":5.0,\"fxTime\":\"2022-11-13T18:00+08:00\",\"humidity\":56.0,\"icon\":305,\"pop\":51.0,\"precip\":0.3,\"pressure\":1021,\"temp\":13.0,\"text\":\"小雨\",\"wind360\":344,\"windDir\":\"西北风\",\"windScale\":\"3-4\",\"windSpeed\":18.0},{\"dew\":4.0,\"fxTime\":\"2022-11-13T19:00+08:00\",\"humidity\":55.0,\"icon\":151,\"pop\":25.0,\"precip\":0.0,\"pressure\":1021,\"temp\":13.0,\"text\":\"多云\",\"wind360\":1,\"windDir\":\"北风\",\"windScale\":\"3-4\",\"windSpeed\":18.0},{\"dew\":4.0,\"fxTime\":\"2022-11-13T20:00+08:00\",\"humidity\":54.0,\"icon\":151,\"pop\":20.0,\"precip\":0.0,\"pressure\":1021,\"temp\":13.0,\"text\":\"多云\",\"wind360\":2,\"windDir\":\"北风\",\"windScale\":\"3-4\",\"windSpeed\":16.0},{\"dew\":3.0,\"fxTime\":\"2022-11-13T21:00+08:00\",\"humidity\":53.0,\"icon\":151,\"pop\":20.0,\"precip\":0.0,\"pressure\":1021,\"temp\":13.0,\"text\":\"多云\",\"wind360\":6,\"windDir\":\"北风\",\"windScale\":\"3-4\",\"windSpeed\":14.0},{\"dew\":3.0,\"fxTime\":\"2022-11-13T22:00+08:00\",\"humidity\":51.0,\"icon\":151,\"pop\":20.0,\"precip\":0.0,\"pressure\":1021,\"temp\":13.0,\"text\":\"多云\",\"wind360\":13,\"windDir\":\"东北风\",\"windScale\":\"3-4\",\"windSpeed\":14.0},{\"dew\":2.0,\"fxTime\":\"2022-11-13T23:00+08:00\",\"humidity\":49.0,\"icon\":151,\"pop\":20.0,\"precip\":0.0,\"pressure\":1022,\"temp\":13.0,\"text\":\"多云\",\"wind360\":20,\"windDir\":\"东北风\",\"windScale\":\"3-4\",\"windSpeed\":14.0},{\"dew\":2.0,\"fxTime\":\"2022-11-14T00:00+08:00\",\"humidity\":48.0,\"icon\":151,\"pop\":16.0,\"precip\":0.0,\"pressure\":1022,\"temp\":13.0,\"text\":\"多云\",\"wind360\":25,\"windDir\":\"东北风\",\"windScale\":\"3-4\",\"windSpeed\":13.0}]"));
//        System.out.println(buildWeatherString("[{\"dew\":16.0,\"fxTime\":\"2022-11-12T14:00+08:00\",\"humidity\":45.0,\"icon\":101,\"pop\":7.0,\"precip\":0.0,\"pressure\":1015,\"temp\":30.0,\"text\":\"多云\",\"wind360\":330,\"windDir\":\"西北风\",\"windScale\":\"4-5\",\"windSpeed\":25.0},{\"dew\":16.0,\"fxTime\":\"2022-11-12T15:00+08:00\",\"humidity\":45.0,\"icon\":101,\"pop\":7.0,\"precip\":0.0,\"pressure\":1015,\"temp\":29.0,\"text\":\"多云\",\"wind360\":330,\"windDir\":\"西北风\",\"windScale\":\"4-5\",\"windSpeed\":25.0},{\"dew\":16.0,\"fxTime\":\"2022-11-12T16:00+08:00\",\"humidity\":48.0,\"icon\":101,\"pop\":7.0,\"precip\":0.0,\"pressure\":1016,\"temp\":28.0,\"text\":\"多云\",\"wind360\":330,\"windDir\":\"西北风\",\"windScale\":\"3-4\",\"windSpeed\":22.0},{\"dew\":16.0,\"fxTime\":\"2022-11-12T17:00+08:00\",\"humidity\":51.0,\"icon\":101,\"pop\":7.0,\"precip\":0.0,\"pressure\":1016,\"temp\":27.0,\"text\":\"多云\",\"wind360\":330,\"windDir\":\"西北风\",\"windScale\":\"3-4\",\"windSpeed\":20.0},{\"dew\":16.0,\"fxTime\":\"2022-11-12T18:00+08:00\",\"humidity\":53.0,\"icon\":151,\"pop\":6.0,\"precip\":0.0,\"pressure\":1016,\"temp\":27.0,\"text\":\"多云\",\"wind360\":330,\"windDir\":\"西北风\",\"windScale\":\"3-4\",\"windSpeed\":22.0},{\"dew\":16.0,\"fxTime\":\"2022-11-12T19:00+08:00\",\"humidity\":54.0,\"icon\":150,\"pop\":5.0,\"precip\":0.0,\"pressure\":1017,\"temp\":26.0,\"text\":\"晴\",\"wind360\":337,\"windDir\":\"西北风\",\"windScale\":\"4-5\",\"windSpeed\":27.0},{\"dew\":16.0,\"fxTime\":\"2022-11-12T20:00+08:00\",\"humidity\":54.0,\"icon\":150,\"pop\":5.0,\"precip\":0.0,\"pressure\":1017,\"temp\":25.0,\"text\":\"晴\",\"wind360\":337,\"windDir\":\"西北风\",\"windScale\":\"4-5\",\"windSpeed\":29.0},{\"dew\":15.0,\"fxTime\":\"2022-11-12T21:00+08:00\",\"humidity\":55.0,\"icon\":150,\"pop\":6.0,\"precip\":0.0,\"pressure\":1018,\"temp\":25.0,\"text\":\"晴\",\"wind360\":336,\"windDir\":\"西北风\",\"windScale\":\"4-5\",\"windSpeed\":31.0},{\"dew\":15.0,\"fxTime\":\"2022-11-12T22:00+08:00\",\"humidity\":56.0,\"icon\":150,\"pop\":7.0,\"precip\":0.0,\"pressure\":1019,\"temp\":24.0,\"text\":\"晴\",\"wind360\":336,\"windDir\":\"西北风\",\"windScale\":\"4-5\",\"windSpeed\":33.0},{\"dew\":14.0,\"fxTime\":\"2022-11-12T23:00+08:00\",\"humidity\":55.0,\"icon\":150,\"pop\":7.0,\"precip\":0.0,\"pressure\":1020,\"temp\":24.0,\"text\":\"晴\",\"wind360\":336,\"windDir\":\"西北风\",\"windScale\":\"5-6\",\"windSpeed\":35.0},{\"dew\":14.0,\"fxTime\":\"2022-11-13T00:00+08:00\",\"humidity\":54.0,\"icon\":150,\"pop\":9.0,\"precip\":0.0,\"pressure\":1021,\"temp\":24.0,\"text\":\"晴\",\"wind360\":333,\"windDir\":\"西北风\",\"windScale\":\"5-6\",\"windSpeed\":35.0},{\"dew\":14.0,\"fxTime\":\"2022-11-13T01:00+08:00\",\"humidity\":53.0,\"icon\":151,\"pop\":14.0,\"precip\":0.0,\"pressure\":1021,\"temp\":24.0,\"text\":\"多云\",\"wind360\":328,\"windDir\":\"西北风\",\"windScale\":\"4-5\",\"windSpeed\":33.0},{\"dew\":13.0,\"fxTime\":\"2022-11-13T02:00+08:00\",\"humidity\":51.0,\"icon\":151,\"pop\":14.0,\"precip\":0.0,\"pressure\":1021,\"temp\":24.0,\"text\":\"多云\",\"wind360\":324,\"windDir\":\"西北风\",\"windScale\":\"4-5\",\"windSpeed\":33.0},{\"dew\":12.0,\"fxTime\":\"2022-11-13T03:00+08:00\",\"humidity\":67.0,\"icon\":151,\"pop\":18.0,\"precip\":0.0,\"pressure\":1021,\"temp\":19.0,\"text\":\"多云\",\"wind360\":333,\"windDir\":\"西北风\",\"windScale\":\"4-5\",\"windSpeed\":31.0},{\"dew\":12.0,\"fxTime\":\"2022-11-13T04:00+08:00\",\"humidity\":65.0,\"icon\":305,\"pop\":55.0,\"precip\":0.3,\"pressure\":1021,\"temp\":19.0,\"text\":\"小雨\",\"wind360\":354,\"windDir\":\"北风\",\"windScale\":\"4-5\",\"windSpeed\":29.0},{\"dew\":11.0,\"fxTime\":\"2022-11-13T05:00+08:00\",\"humidity\":63.0,\"icon\":305,\"pop\":55.0,\"precip\":0.3,\"pressure\":1020,\"temp\":19.0,\"text\":\"小雨\",\"wind360\":6,\"windDir\":\"北风\",\"windScale\":\"4-5\",\"windSpeed\":27.0},{\"dew\":11.0,\"fxTime\":\"2022-11-13T06:00+08:00\",\"humidity\":60.0,\"icon\":305,\"pop\":55.0,\"precip\":0.3,\"pressure\":1019,\"temp\":19.0,\"text\":\"小雨\",\"wind360\":11,\"windDir\":\"东北风\",\"windScale\":\"4-5\",\"windSpeed\":27.0},{\"dew\":8.0,\"fxTime\":\"2022-11-13T07:00+08:00\",\"humidity\":57.0,\"icon\":101,\"pop\":25.0,\"precip\":0.0,\"pressure\":1019,\"temp\":16.0,\"text\":\"多云\",\"wind360\":7,\"windDir\":\"北风\",\"windScale\":\"3-4\",\"windSpeed\":22.0},{\"dew\":7.0,\"fxTime\":\"2022-11-13T08:00+08:00\",\"humidity\":55.0,\"icon\":101,\"pop\":20.0,\"precip\":0.0,\"pressure\":1020,\"temp\":16.0,\"text\":\"多云\",\"wind360\":8,\"windDir\":\"北风\",\"windScale\":\"3-4\",\"windSpeed\":22.0},{\"dew\":7.0,\"fxTime\":\"2022-11-13T09:00+08:00\",\"humidity\":53.0,\"icon\":101,\"pop\":20.0,\"precip\":0.0,\"pressure\":1020,\"temp\":16.0,\"text\":\"多云\",\"wind360\":5,\"windDir\":\"北风\",\"windScale\":\"3-4\",\"windSpeed\":22.0},{\"dew\":6.0,\"fxTime\":\"2022-11-13T10:00+08:00\",\"humidity\":51.0,\"icon\":101,\"pop\":20.0,\"precip\":0.0,\"pressure\":1021,\"temp\":17.0,\"text\":\"多云\",\"wind360\":2,\"windDir\":\"北风\",\"windScale\":\"3-4\",\"windSpeed\":22.0},{\"dew\":6.0,\"fxTime\":\"2022-11-13T11:00+08:00\",\"humidity\":49.0,\"icon\":101,\"pop\":20.0,\"precip\":0.0,\"pressure\":1022,\"temp\":17.0,\"text\":\"多云\",\"wind360\":345,\"windDir\":\"西北风\",\"windScale\":\"3-4\",\"windSpeed\":22.0},{\"dew\":5.0,\"fxTime\":\"2022-11-13T12:00+08:00\",\"humidity\":48.0,\"icon\":101,\"pop\":25.0,\"precip\":0.0,\"pressure\":1022,\"temp\":17.0,\"text\":\"多云\",\"wind360\":358,\"windDir\":\"北风\",\"windScale\":\"3-4\",\"windSpeed\":22.0},{\"dew\":5.0,\"fxTime\":\"2022-11-13T13:00+08:00\",\"humidity\":51.0,\"icon\":305,\"pop\":61.0,\"precip\":0.5,\"pressure\":1022,\"temp\":15.0,\"text\":\"小雨\",\"wind360\":356,\"windDir\":\"北风\",\"windScale\":\"3-4\",\"windSpeed\":20.0}]"));
    }
}
