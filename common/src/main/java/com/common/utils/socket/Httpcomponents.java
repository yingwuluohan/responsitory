package com.common.utils.socket;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by fn on 2017/3/2.
 */
public class Httpcomponents {

    public String getHttp(){
        CloseableHttpClient httpClient= HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://www.baidu.com");
        CloseableHttpResponse response = null;
        String strResult= null;
        try {
            response = httpClient.execute(httpget);
            HttpEntity httpEntity= response.getEntity();
            strResult = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strResult;
    }

}
