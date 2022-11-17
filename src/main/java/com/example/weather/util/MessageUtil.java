package com.example.weather.util;

import com.example.weather.Cron.UserCron;
import com.example.weather.VO.Message;
import com.example.weather.VO.MessageItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/15 16:05
 */
@Component
public class MessageUtil {
    private static Logger logger = LoggerFactory.getLogger(MessageUtil.class);
//    @Value(value = "${weather.session}")
    private static String SESSION = null;
//    @Value(value = "${weather.domain}")
    private static String DOMAIN = null;

    @Value(value = "${weather.session}")
    public void setSESSION(String SESSION) {
        this.SESSION = SESSION;
    }

    @Value(value = "${weather.domain}")
    public void setDOMAIN(String DOMAIN) {
        this.DOMAIN = DOMAIN;
    }

    public static void sendPlain(String text, Long target) throws IOException {
        List<MessageItem> items = new ArrayList<>();
        MessageItem item = new MessageItem();
        item.setType("Plain");
        item.setText(text);
        items.add(item);

        Message msg = new Message();
        msg.setTarget(target);
        msg.setSessionKey(SESSION);
        msg.setMessageChain(items);

        String postResult = null;
        try {
            postResult = HttpUtil.post(DOMAIN + "/sendFriendMessage", msg);
        } catch (Exception e) {
            logger.error("Failed to send friend message", e);
            return;
        }
        logger.info("SentFriendMessage: " + postResult + "text:" + text);
    }
}
