package com.example.weather.Cron;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.weather.util.HttpUtil;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/12 15:28
 */
@Configuration
@EnableScheduling
public class User {

    private Logger logger = LoggerFactory.getLogger(User.class);
    @Value(value = "${weather.session}")
    private String SESSION = null;
    @Value(value = "${weather.domain}")
    private String DOMAIN = null;

    @Scheduled(cron = "0/10 * * * * ?")
    public Long countMessage() throws IOException {
        JSONObject result = JSONObject.parseObject(HttpUtil.get(DOMAIN+"/countMessage?sessionKey="+SESSION));
        logger.info(result.toJSONString());
        return result.getLong("data");
    }

}