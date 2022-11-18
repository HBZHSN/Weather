package com.example.weather.vo;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/11 17:54
 */
@Data
public class Message {
    String sessionKey;
    Long target;
    List<MessageItem> messageChain;
}
