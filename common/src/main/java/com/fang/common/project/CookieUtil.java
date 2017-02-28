package com.fang.common.project;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fn on 2017/2/27.
 */
public class CookieUtil {

        public CookieUtil() {
        }

        public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value) {
            String autoLogin = request.getParameter("autoLogin");
            String maxAge = request.getParameter("maxAge");
            if("on".equals(autoLogin) && maxAge != null && !"".equals(maxAge)) {
                setCooikeMaxAge(request, response, name, value, Integer.parseInt(maxAge));
            } else {
                setCooikeMaxAge(request, response, name, value, -1);
            }

        }

        public static void setCooikeMaxAge(HttpServletRequest request, HttpServletResponse response, String name, String value, int maxAge) {
            String domainName = getDomainName(request);
            if(domainName != null && !"".equals(domainName)) {
                setCookie(request, response, name, value, domainName, "/", maxAge);
            } else {
                setCookie(request, response, name, value, (String)null, "/", maxAge);
            }

        }

        public static Cookie getCookie(HttpServletRequest request, String name) {
            Cookie[] cookies = request.getCookies();
            if(cookies != null && name != null && name.length() != 0) {
                Cookie cookie = null;

                for(int i = 0; i < cookies.length; ++i) {
                    if(cookies[i].getName().equals(name)) {
                        cookie = cookies[i];
                        break;
                    }
                }

                return cookie;
            } else {
                return null;
            }
        }

        public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, String domainName, String path, int maxAge) {
            if(value == null) {
                value = "";
            }

            Cookie cookie = new Cookie(name, value);
            cookie.setPath(path);
            cookie.setMaxAge(maxAge);
            if(domainName != null && !"".equals(domainName)) {
                cookie.setDomain(domainName);
            }

            response.addCookie(cookie);
        }

        public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie) {
            if(cookie != null) {
                String path = request.getContextPath() == null?"/":request.getContextPath();
                if("".equals(path)) {
                    path = "/";
                }

                deleteCookie(request, response, (Cookie)cookie, (String)null, path);
            }

        }

        public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie, String path) {
            deleteCookie(request, response, (Cookie)cookie, (String)null, path);
        }

        public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie, String domainName, String path) {
            if(cookie != null) {
                cookie.setPath(path);
                cookie.setValue("");
                cookie.setMaxAge(0);
                if(domainName != null && !"".equals(domainName)) {
                    cookie.setDomain(domainName);
                }

                response.addCookie(cookie);
            }

        }

        public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name, String path) {
            if(name != null) {
                deleteCookie(request, response, getCookie(request, name), path);
            }

        }

        public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
            deleteCookie(request, response, (String)name, (String)null, "/");
            String domainName = getDomainName(request);
            if(domainName != null && !"".equals(domainName)) {
                deleteCookie(request, response, name, domainName, "/");
            }

        }

        public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name, String domainName, String path) {
            if(name != null) {
                deleteCookie(request, response, getCookie(request, name), domainName, path);
            }

        }

        private static String getDomainName(HttpServletRequest request) {
            String domainName = request.getServerName();
            if(domainName != null) {
                int pos = domainName.indexOf(".");
                if(pos > 0) {
                    domainName = domainName.substring(pos);
                    return domainName;
                }
            }

            return null;
        }
}
