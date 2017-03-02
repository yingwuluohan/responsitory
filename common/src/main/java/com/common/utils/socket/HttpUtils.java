package com.common.utils.socket;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.*;

/**
 */
public class HttpUtils {

    public static String getUrl(String url) {
        // (1) 创建HttpGet实例
        HttpGet get = new HttpGet(url);
        // (2) 使用HttpClient发送get请求，获得返回结果HttpResponse
        HttpClient http = new DefaultHttpClient();
        HttpResponse response = null;
        String result = getHttpRequest( get , http );
        return result;
    }

    public static String postHttpRequest( String url ){
        // (1) 创建Httppost实例
        HttpPost post = new HttpPost(url);
        HttpClient http = new DefaultHttpClient();
        HttpResponse response = null;
        String result = getHttpRequest( post , http );
        return result;
    }
    private static String getHttpRequest(HttpRequestBase httpRequestBase , HttpClient http ){
        HttpResponse response = null;
        //临时存储字符串。
        StringBuffer buffer = new StringBuffer();
        try {
            response = http.execute( httpRequestBase );
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                InputStream in = null;
                try {
                    in = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                } finally {
                    //关闭输入流
                    if (in != null)
                        in.close();
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }finally {
            //释放连接。http://www.tuicool.com/articles/VJjQV3
            try {
                httpRequestBase.abort();
            } catch (Exception e) {

            }
            try {
                // 释放连接
                httpRequestBase.releaseConnection();
            } catch (Exception e) {

            }
            if (response != null) {
                try {
                    EntityUtils.consumeQuietly(response.getEntity());
                } catch (Exception e) {

                }
            }
        }
        return buffer.toString();
    }

    /**
     * 根据登录Cookie获取资源
     * 一切异常均未处理，需要酌情检查异常
     *
     * @throws Exception
     */
    private static void getResoucesByLoginCookies() throws Exception
    {
        String username = "testtongji4";// 登录用户
        // 需要提交登录的信息
        String urlLogin = "http://www.iteye.com/login?name=" + username + "&password="  ;
        // 登录成功后想要访问的页面 可以是下载资源 需要替换成自己的iteye Blog地址
        String urlAfter = "http://314649444.iteye.com/";
        DefaultHttpClient client = new DefaultHttpClient(new PoolingClientConnectionManager());
        /**
         * 第一次请求登录页面 获得cookie
         * 相当于在登录页面点击登录，此处在URL中 构造参数，
         * 如果参数列表相当多的话可以使用HttpClient的方式构造参数
         * 此处不赘述
         */
        HttpPost post = new HttpPost(urlLogin);
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        CookieStore cookieStore = client.getCookieStore();
        client.setCookieStore(cookieStore);

        /**
         * 带着登录过的cookie请求下一个页面，可以是需要登录才能下载的url
         * 此处使用的是iteye的博客首页，如果登录成功，那么首页会显示【欢迎XXXX】
         *
         */
        HttpGet get = new HttpGet(urlAfter);
        response = client.execute(get);
        entity = response.getEntity();
        /**
         * 将请求结果放到文件系统中保存为 myindex.html,便于使用浏览器在本地打开 查看结果
         */
        String pathName = "d:\\myindex.html";
        writeHTMLtoFile(entity, pathName);
        if( client != null ){
            client.close();
        }
    }
    /**
     * Write htmL to file.
     * 将请求结果以二进制形式放到文件系统中保存为.html文件,便于使用浏览器在本地打开 查看结果
     *
     * @param entity the entity
     * @param pathName the path name
     * @throws Exception the exception
     */
    public static void writeHTMLtoFile(HttpEntity entity, String pathName) throws Exception
    {

        byte[] bytes = new byte[(int) entity.getContentLength()];

        FileOutputStream fos = new FileOutputStream(pathName);

        bytes = EntityUtils.toByteArray(entity);

        fos.write(bytes);

        fos.flush();

        fos.close();
    }
    public static void main( String[] agrs ){
        System.out.println( getUrl( "http://www.baidu.com" ));
    }

}
