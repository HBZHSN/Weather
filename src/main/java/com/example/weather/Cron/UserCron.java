package com.example.weather.Cron;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.weather.VO.MessageItem;
import com.example.weather.service.MessageService;
import com.example.weather.service.UserService;
import com.example.weather.util.HttpUtil;
import com.example.weather.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/12 15:28
 */
@Component
@Configuration
@EnableAsync
@EnableScheduling
public class UserCron {

    private Logger logger = LoggerFactory.getLogger(UserCron.class);
    @Value(value = "${weather.session}")
    private String SESSION = null;
    @Value(value = "${weather.domain}")
    private String DOMAIN = null;
    @Value(value = "${weather.verifyKey}")
    private String VERIFY_KEY = null;
    @Value(value = "${weather.qq}")
    private String QQ = null;
    @Value(value = "${weather.notify}")
    private Long NOTIFY = null;

    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @PostConstruct
    public void startProject() {
        try {
//            MessageUtil.sendPlain("项目启动成功", Long.valueOf(NOTIFY));
            messageService.newMessage(NOTIFY, 1, "项目启动成功");
        } catch (Exception e) {
            logger.error("sendStartMessageError", e);
        }
    }

    @Async
    @Scheduled(cron = "0/10 * * * * ?")
    public void dealMessage() throws Exception {
        Long countMessage = null;
        try {
            JSONObject countResult = JSONObject.parseObject(HttpUtil.get(DOMAIN + "/countMessage?sessionKey=" + SESSION));
            if (countResult.getInteger("code") == 3) {//session失效，临时用一个session发消息
                Map<String, String> map = new HashMap<>();
                map.put("verifyKey", VERIFY_KEY);
                String session = JSONObject.parseObject(HttpUtil.post(DOMAIN + "/verify", map)).getString("session");
                map.clear();
                map.put("sessionKey", session);
                map.put("qq", QQ);
                HttpUtil.post(DOMAIN + "/bind", map);
                Map<String, Object> mapMessage = new HashMap<>();
                mapMessage.put("sessionKey", session);
                mapMessage.put("target", NOTIFY);
                List<MessageItem> items = new ArrayList<>();
                MessageItem item = new MessageItem();
                item.setText("Session失效，快去看一眼！");
                item.setType("Plain");
                items.add(item);
                mapMessage.put("messageChain", items);
                HttpUtil.post(DOMAIN + "/sendFriendMessage", mapMessage);
                System.exit(0);
            }
            countMessage = countResult.getLong("data");
        } catch (Exception e) {
            logger.error("dealMessageError", e);
        }
        if (countMessage != 0) {
            String result = null;
            try {
                result = HttpUtil.get(String.format("%s/fetchMessage?sessionKey=%s&count=%s", DOMAIN, SESSION, countMessage));
            } catch (Exception e) {
                logger.error("dealMessageError", e);
                return;
            }
            JSONArray resultArray = JSONObject.parseObject(result).getJSONArray("data");
            for (int i = 0; i < resultArray.size(); i++) {
                JSONObject resultObject = resultArray.getJSONObject(i);
                String type = resultObject.getString("type");
                if (type.equals("NewFriendRequestEvent")) {
                    Map<String, Object> body = new HashMap<>();
                    body.put("sessionKey", SESSION);
                    body.put("eventId", resultObject.getLong("eventId"));
                    body.put("fromId", resultObject.getLong("fromId"));
                    body.put("groupId", resultObject.getLong("groupId"));
                    body.put("operate", 1);
                    logger.info(body.toString());
                    try {
                        HttpUtil.post(DOMAIN + "/resp/newFriendRequestEvent", body);
                    } catch (Exception e) {
                        logger.error("dealMessageError", e);
                        return;
                    }
                } else if (type.equals("FriendMessage")) {
                    JSONArray messageChain = resultObject.getJSONArray("messageChain");
                    if (messageChain.getJSONObject(1).getString("type").equals("Plain")) {
                        String text = messageChain.getJSONObject(1).getString("text");
                        JSONObject sender = resultObject.getJSONObject("sender");
                        if (text.startsWith("订阅天气")) {
                            String city = text.substring(5);
                            logger.info(sender.toJSONString());
                            userService.newUser(sender.getString("nickname"), sender.getLong("id"), 1, city);
//                            MessageUtil.sendPlain(String.format("订阅成功，机器人将在每天7、22时自动发送%s天气", city), sender.getLong("id"));
                            messageService.newMessage(sender.getLong("id"), 1, String.format("订阅成功，机器人将在每天7、22时自动发送%s天气", city));
                        } else {
                            text = sender.getString("nickname") + " : " + text;
//                            MessageUtil.sendPlain(text, Long.valueOf(NOTIFY));
                            messageService.newMessage(NOTIFY, 1, text);
                        }
                    }
                }
            }
        }
    }


}
