package com.example.weather.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/11 23:04
 */
@Data
public class UserWeather {
    Long id;
    String name;
    Long target;
    Integer type;
    Long locate;
}
