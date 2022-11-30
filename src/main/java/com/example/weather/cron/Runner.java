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
    private MessageService messageService;

    @Override
    public void run(String... args) throws Exception {
        try {
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append("机器人目前支持功能\n")
//                    .append("【每日天气自动发送】\n发送：订阅天气 济南\n")
//                    .append("【音乐快速分享】\n发送：#音乐 两只老虎\n")
//                    .append("【apex相关信息查询】\n发送：/apexhelp\n")
//                    .append("【青云客聊天机器人】\n发送：help");
//            String message = stringBuilder.toString();
//            JSONArray idFriendJSON = JSONObject.parseObject(HttpUtil.get(DOMAIN + "/friendList?sessionKey=" + SESSION)).getJSONArray("data");
//            List<Long> ids = new ArrayList<>();
//            for (int i = 0; i < idFriendJSON.size(); i++) {
//                ids.add(idFriendJSON.getJSONObject(i).getLong("id"));
//                messageService.newMessage(ids.get(i), 1, message);
//            }
//            JSONArray idGroupJSON = JSONObject.parseObject(HttpUtil.get(DOMAIN + "/groupList?sessionKey=" + SESSION)).getJSONArray("data");
//            for (int i = 0; i < idGroupJSON.size(); i++) {
//                ids.add(idGroupJSON.getJSONObject(i).getLong("id"));
//            }
            messageService.newMessage(NOTIFY, "项目启动成功");
        } catch (Exception e) {
            logger.error("sendStartMessageError", e);
        }
    }
}

