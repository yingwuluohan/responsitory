package com.fang.controller.interceptor;

import com.common.utils.utils.CacheTools;
import com.fang.common.project.CommonConstant;
import com.fang.common.project.CookieUtil;
import com.modle.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fn on 2017/3/1.
 */
public class UrlInterceptor extends HandlerInterceptorAdapter {

    private static Log logger = LogFactory.getLog(UrlInterceptor.class);


    /**
     * 被拦截
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isFire = false;
        String loginUrl = "/loginPage";
        Cookie authCookie = CookieUtil.getCookie(request, "global");
        String authId = "0";
        if (authCookie != null) {
            authId = authCookie.getValue();
        }
        User ue = CacheTools.getCacheForever(authId, User.class);
        if (ue != null) {
            isFire = true;
        } else {
            CacheTools.delCache(authId);
        }
        if (ue != null) {
            System.out.println("authId==" + authId + "userid=" + ue.getId() + "   username=" + ue.getUserName() + "  usertype=" + ue.getType() + "   isFire===" + isFire);
        } else {
            System.out.println("authId==" + authId + "ue==null   isFire=" + isFire);
        }
        if (!isFire) {
            response.sendRedirect(request.getContextPath() + loginUrl);
        }

        return isFire;
    }

    /**
     * preHandle返回true调用
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}