package com.example.weather.service;

import com.example.weather.Cron.WeatherCron;
import com.example.weather.VO.Message;
import com.example.weather.VO.MessageLog;
import com.example.weather.mapper.MessageMapper;
import com.example.weather.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/17 14:00
 */
@Service
public class MessageService {
    private static Logger logger = LoggerFactory.getLogger(MessageService.class);
    @Autowired
    private MessageMapper messageMapper;

    public Integer newMessage(Long target, Integer type, String text) {
        MessageLog messageLog = new MessageLog();
        messageLog.setTarget(target);
        messageLog.setType(type);
        messageLog.setText(text);
        messageLog.setTime(new Timestamp(System.currentTimeMillis()));
        try {
            MessageUtil.sendPlain(text, target);
        } catch (Exception e) {
            logger.error("sendPlain failed", e);
        }
        return messageMapper.newMessage(messageLog);
    }
}
