package com.example.weather.VO;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/17 14:01
 */
@Data
public class MessageLog {
    Long id;
    Long target;
    Integer type;
    String text;
    Timestamp time;
}
