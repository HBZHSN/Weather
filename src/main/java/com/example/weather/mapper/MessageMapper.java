package com.example.weather.mapper;

import com.example.weather.VO.Message;
import com.example.weather.VO.MessageLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/17 14:00
 */
@Mapper
public interface MessageMapper {
    public Integer newMessage(MessageLog messageLog);

}
