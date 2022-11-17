package com.example.weather.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
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
    public static String post(String url, Object body) throws Exception {
        String result = null;
        HttpPost httpPost = new HttpPost();
        try {
            httpPost.setURI(URI.create(url));
            httpPost.setEntity(new StringEntity(JSONObject.toJSONString(body), ContentType.create("application/json", "utf-8")));
            CloseableHttpClient client = HttpClientBuilder.create().build();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
            httpPost.setConfig(requestConfig);
            HttpResponse response = client.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
            client.close();
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    public static String get(String url) throws Exception {
        String result = null;
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
            httpGet.setConfig(requestConfig);
            HttpResponse response = client.execute(httpGet);
            result = EntityUtils.toString(response.getEntity());
            client.close();
        } catch (Exception e) {
            throw e;
        }
        return result;
    }
}
