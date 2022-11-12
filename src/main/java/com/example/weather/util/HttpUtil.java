package com.example.weather.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/11 23:19
 */
public class HttpUtil {
    public static String post(String url, Object body) throws IOException {
        HttpPost httpPost = new HttpPost();
        httpPost.setURI(URI.create(url));
        httpPost.setEntity(new StringEntity(JSONObject.toJSONString(body), ContentType.create("application/json", "utf-8")));
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity());
        return result;
    }

    public static String get(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(get);
        String result = EntityUtils.toString(response.getEntity());
        return result;
    }
}
