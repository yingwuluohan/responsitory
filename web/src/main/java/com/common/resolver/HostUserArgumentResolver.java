package com.common.resolver;

import com.common.utils.utils.CacheTools;
import com.modle.User;

import com.fang.common.project.CommonConstant;
import com.fang.common.project.CookieUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class HostUserArgumentResolver implements WebArgumentResolver {

	
	@SuppressWarnings("unchecked")
	@Override
	public Object resolveArgument(MethodParameter arg0, NativeWebRequest arg1)
			throws Exception {
		HttpServletRequest request = arg1.getNativeRequest(HttpServletRequest.class);
		if (arg0.getParameterType().equals(User.class)) {
			Cookie authCookie = CookieUtil.getCookie(request, CommonConstant.AUTH_STR);
	    	String authId = authCookie.getValue();

			User ue = CacheTools.getCacheForever(authId, User.class);
			return ue;
		}
		return UNRESOLVED;
	}
}