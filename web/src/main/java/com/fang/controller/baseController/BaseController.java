package com.fang.controller.basecontroller;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by on 2017/3/1.
 */
public class BaseController {

    public String getParamter(String key) {
        return getHttpRequest().getParameter(key);
    }
    public static HttpServletRequest getHttpRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        return request;
    }
    public int getParamterForInt(String key, int defaultValue) {
        if (StringUtils.isNotBlank(getParamter(key))) {
            return Integer.parseInt(getParamter(key));
        } else {
            return defaultValue;
        }
    }
    public static Object getCookie(String key) {
        Cookie[] cookies = getHttpRequest().getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie ck : cookies) {
            if (key.equals(ck.getName())) {
                return ck.getValue();
            }
        }
        return null;
    }

    public static void delCookie(String key, HttpServletResponse response) {
        Cookie[] cookies = getHttpRequest().getCookies();
        Cookie cookie = null;
        if (cookies != null) {
            for (Cookie ck : cookies) {
                if (key.equals(ck.getName())) {
                    cookie = ck;
                    break;
                }
            }
        }
        cookie.setDomain("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static void setCookie(String key, String o, Integer maxAge, HttpServletResponse response) {
        Cookie cookies = new Cookie(key, o);
        if (maxAge == null) {
            //会话cookie
        } else {
            cookies.setMaxAge(maxAge);
        }
        cookies.setPath("/");
        response.addCookie(cookies);
    }

    protected void printWriterAjax(HttpServletResponse response, String message) {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");

        try {
            PrintWriter pwriter = response.getWriter();
            pwriter.print(message);
            pwriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * print  对象为json
     *
     * @param response
     */
    protected void printWriterAjax(HttpServletResponse response, Object obj) {
        printWriterAjax(response, JSON.toJSONString(obj));
    }
}
