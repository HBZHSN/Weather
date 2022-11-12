package com.example.weather.VO;

import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/11 17:05
 */
@Data
public class WeatherHour {
    String fxTime;//预报时间
    Double temp;//温度
    Long icon;//图标
    String text;//文字描述天气状况
    Integer wind360;//风向360度
    String windDir;//风向
    String windScale;//风力等级
    Double windSpeed;//风速
    Double humidity;//相对湿度
    Double pop;//降水概率
    Double precip;//当前小时累计降水量
    Integer pressure;//大气压强
    Double dew;//云量
}
