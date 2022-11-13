package com.example.weather.Cron;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.weather.VO.Message;
import com.example.weather.VO.MessageItem;
import com.example.weather.service.UserService;
import com.example.weather.util.HttpUtil;
import com.example.weather.util.WeatherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
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
@Configuration
@EnableScheduling
public class User {

    private Logger logger = LoggerFactory.getLogger(User.class);
    @Value(value = "${weather.session}")
    private String SESSION = null;
    @Value(value = "${weather.domain}")
    private String DOMAIN = null;

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void dealMessage() throws IOException {
        JSONObject countResult = JSONObject.parseObject(HttpUtil.get(DOMAIN + "/countMessage?sessionKey=" + SESSION));
        Long countMessage = countResult.getLong("data");
        if (countMessage != 0) {
            String result = HttpUtil.get(String.format("%s/fetchMessage?sessionKey=%s&count=%s", DOMAIN, SESSION, countMessage));
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
                    HttpUtil.post(DOMAIN + "/resp/newFriendRequestEvent", body);
                } else if (type.equals("FriendMessage")) {
                    JSONArray messageChain = resultObject.getJSONArray("messageChain");
                    if (messageChain.getJSONObject(1).getString("type").equals("Plain")) {
                        String text = messageChain.getJSONObject(1).getString("text");
                        if (text.startsWith("订阅天气")) {
                            String city = text.substring(5);
                            JSONObject sender = resultObject.getJSONObject("sender");
                            logger.info(sender.toJSONString());
                            logger.info(city);
                            userService.newUser(sender.getString("nickname"), sender.getLong("id"), 1, city);
                            List<MessageItem> items = new ArrayList<>();
                            MessageItem item = new MessageItem();
                            item.setType("Plain");
                            item.setText(String.format("订阅成功，机器人将在每天7、22时自动发送%s天气", city));
                            items.add(item);

                            Message msg = new Message();
                            msg.setTarget(sender.getLong("id"));
                            msg.setSessionKey(SESSION);
                            msg.setMessageChain(items);

                            String postResult = HttpUtil.post(DOMAIN + "/sendFriendMessage", msg);
                            logger.info("SentFriendMessage: " + postResult);
                        }
                    }
                }
            }
        }
    }


}
