package com.example.weather.cron;

import com.example.weather.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/18 15:13
 */
@Component
public class Runner implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(Runner.class);
    @Value(value = "${weather.notify}")
    private Long NOTIFY = null;

    @Autowired
    private MessageService messageService;

    @Override
    public void run(String... args) throws Exception {
        try {
//            MessageUtil.sendPlain("项目启动成功", NOTIFY);
            messageService.newMessage(NOTIFY, 1, "项目启动成功");
        } catch (Exception e) {
            logger.error("sendStartMessageError", e);
        }
    }
}

