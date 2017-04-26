package com.common.utils.utils;

import com.fang.common.project.CookieUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by  on 2017/2/28.
 */
public class WebUtil {
    public static String getSSOSessionId(HttpServletRequest request) {
        String ssoSessionId = null;
        Cookie cookie = CookieUtil.getCookie(request, "ssoSessionID");
        if(cookie != null) {
            ssoSessionId = cookie.getValue();
        } else {
            ssoSessionId = request.getParameter("ssoSessionID");
        }

        return ssoSessionId;
    }
}
