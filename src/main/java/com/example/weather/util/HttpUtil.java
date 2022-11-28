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
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            httpPost.setURI(URI.create(url));
            httpPost.setEntity(new StringEntity(JSONObject.toJSONString(body), ContentType.create("application/json", "utf-8")));
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
            httpPost.setConfig(requestConfig);
            HttpResponse response = client.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    public static String get(String url) throws Exception {
        String result = null;
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
            httpGet.setConfig(requestConfig);
            HttpResponse response = client.execute(httpGet);
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    public static String aiGet(String urlBefore) throws Exception {
//        text = new String(text.getBytes("iso8859-1"), StandardCharsets.UTF_8);
        URL url = new URL(urlBefore);
        URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
        String result = get(uri.toString());
        result = result.replace("{br}", "\n");
        return result;
    }
}
