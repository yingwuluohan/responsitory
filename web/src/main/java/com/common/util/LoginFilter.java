package com.common.util;

import com.common.utils.modle.ThirdDomainDTO;
import com.modle.User;
import com.common.utils.utils.CommonUtil;
import com.common.utils.utils.StringAnalyUtils;
import com.common.utils.utils.SystemGlobals;
import com.fang.common.project.CommonConstant;
import com.fang.common.project.CookieUtil;
import com.modle.Authorization;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class LoginFilter extends HttpServlet implements Filter {
	private static final long serialVersionUID = 896191300709327492L;
	Logger logger = Logger.getLogger(this.getClass());
	private static Map cacheMap = Collections.synchronizedMap(new HashMap());
	private List<ThirdDomainDTO> thirdAutoLoginList = null;

	private static final byte[] desKey = new byte[]{(byte)64, (byte)-77, (byte)35, (byte)-45, (byte)16, (byte)-22, (byte)121, (byte)-15};

	private static String SSO_HOST = "sso.host";
	private static String SSO_HOST2 = "hosts.sso";
	private static String ALLOW_ANONYMOUS = "sso.anonymous";
	protected FilterConfig filterConfig;
	protected String loginURL;
	protected String toLoginURL;
	private String ssoHost;
	protected String authenticateURL;
	protected String validateURL;
	protected boolean allowAnonymous = false;
	protected List<String> urlPatternInclude;
	protected List<String> urlPatternExclude;
	protected List<String> urlMustLogin;
	protected String thirdAutoLogin;
	protected boolean needXdfLogin;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.ssoHost = filterConfig.getInitParameter(SSO_HOST);
		if(this.ssoHost == null) {
			this.ssoHost = filterConfig.getServletContext().getInitParameter(SSO_HOST);
		}

		if(this.ssoHost == null) {
			try {
				this.ssoHost = SystemGlobals.getPreference(SSO_HOST);
			} catch (Exception var6) {
				;
			}
		}

		if(this.ssoHost == null) {
			this.ssoHost = filterConfig.getServletContext().getInitParameter(SSO_HOST2);
		}

		if(this.ssoHost == null) {
			this.ssoHost = SystemGlobals.getPreference(SSO_HOST2);
		}

		if(StringUtils.isBlank(this.ssoHost)) {
			System.out.println( "Missing configuration \'" + SSO_HOST2 + "\'!" );
			System.out.println( "Missing configuration \'" + SSO_HOST2 + "\'!" );
			ssoHost = "";
			//throw new RuntimeException("Missing configuration \'" + SSO_HOST2 + "\'!");
		} else {
			if(!this.ssoHost.endsWith("/")) {
				this.ssoHost = this.ssoHost + "/";
			}

			if(!this.ssoHost.endsWith("/sso/")) {
				this.ssoHost = this.ssoHost + "sso/";
			}

			if(StringUtils.isBlank(this.loginURL)) {
				this.loginURL = this.ssoHost + "login.do";
			}

			if(StringUtils.isBlank(this.toLoginURL)) {
				this.toLoginURL = this.ssoHost + "toLogin.do";
			}

			if(StringUtils.isBlank(this.authenticateURL)) {
				this.authenticateURL = this.ssoHost + "authenticate.do";
			}

			if(StringUtils.isBlank(this.validateURL)) {
				this.validateURL = this.ssoHost + "validate.do";
			}

			if(logger.isDebugEnabled()) {
				logger.debug("loginURL:" + this.loginURL);
				logger.debug("toLoginURL:" + this.toLoginURL);
				logger.debug("authenticaateURL:" + this.authenticateURL);
				logger.debug("validateURL:" + this.validateURL);
			}

			this.allowAnonymous = "true".equals(filterConfig.getInitParameter(ALLOW_ANONYMOUS));
			this.urlPatternInclude = CommonUtil.strToList(filterConfig.getInitParameter("sso.urlPatternInclude"), ",");
			this.urlPatternExclude = CommonUtil.strToList(filterConfig.getInitParameter("sso.urlPatternExclude"), ",");
			this.urlMustLogin = CommonUtil.strToList(filterConfig.getInitParameter("sso.mustLoginUrl"), ",");
			String temp = filterConfig.getInitParameter("sso.needXdfLogin");
			this.thirdAutoLogin = filterConfig.getInitParameter("sso.thirdAutoLogin");
			if("true".equals(temp)) {
				this.needXdfLogin = true;
			}

			this.thirdAutoLoginList = parseThirdAutoLoginDomain(this.thirdAutoLogin);
			if(this.needXdfLogin) {
				if(this.thirdAutoLoginList == null) {
					this.thirdAutoLoginList = new ArrayList();
				}

				boolean findXdf = false;
				Iterator thirdDomain = this.thirdAutoLoginList.iterator();

				while(thirdDomain.hasNext()) {
					ThirdDomainDTO thirdDomainDTO = (ThirdDomainDTO)thirdDomain.next();
					if(thirdDomainDTO != null && "xdf.cn".equals(thirdDomainDTO.getDomain())) {
						findXdf = true;
						break;
					}
				}

				if(!findXdf) {
					ThirdDomainDTO thirdDomain1 = new ThirdDomainDTO();
					thirdDomain1.setDomain("xdf.cn");
					thirdDomain1.setType("xdf");
					this.thirdAutoLoginList.add(thirdDomain1);
				}
			}

		}
	}

	private static List<ThirdDomainDTO> parseThirdAutoLoginDomain(String str) {
		ArrayList thirdDomainList = null;
		if(StringUtils.isNotBlank(str)) {
			thirdDomainList = new ArrayList();
			String[] domains = str.split(";");
			if(domains.length > 0) {
				String[] var4 = domains;
				int var5 = domains.length;

				for(int var6 = 0; var6 < var5; ++var6) {
					String domainEle = var4[var6];
					if(StringUtils.isNotBlank(domainEle)) {
						String[] domainInfoArr = domainEle.split(",");
						if(domainInfoArr.length > 0) {
							ThirdDomainDTO thirdDomainDTO = new ThirdDomainDTO();
							thirdDomainList.add(thirdDomainDTO);
							String[] var10 = domainInfoArr;
							int var11 = domainInfoArr.length;

							for(int var12 = 0; var12 < var11; ++var12) {
								String domainInfo = var10[var12];
								if(StringUtils.isNotBlank(domainInfo)) {
									String[] eles = domainInfo.split("=");
									if(eles.length > 1) {
										eles[0] = StringUtils.lowerCase(StringUtils.trim(eles[0]));
										eles[1] = StringUtils.lowerCase(StringUtils.trim(eles[1]));
										if("domain".equals(eles[0])) {
											thirdDomainDTO.setDomain(eles[1]);
										} else if("type".equals(eles[0])) {
											thirdDomainDTO.setType(eles[1]);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return thirdDomainList;
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

/*
		if(request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			HttpServletRequest hRequest = (HttpServletRequest)request;
			HttpServletResponse hResponse = (HttpServletResponse)response;
			String host_name = hRequest.getHeader("host");
			if(host_name == null) {
				filterChain.doFilter(request, response);
			} else {
				String uri = hRequest.getRequestURI();
				if(request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
					filterChain.doFilter(request, response);
				} else {
					boolean match = !this.isMatch(uri, this.urlPatternExclude);
					if(this.urlPatternInclude != null && !this.urlPatternInclude.isEmpty()) {
						match = match && this.isMatch(uri, this.urlPatternInclude);
					}

					if(!match) {
						filterChain.doFilter(request, response);
					} else {
						if(logger.isDebugEnabled()) {
							logger.debug("URI:" + uri);
						}

						Cookie cookie = CookieUtil.getCookie(hRequest, "sso.ssoId");
						String ssoId = null;
						if(cookie != null) {
							ssoId = cookie.getValue();
						}

						if(ssoId == null || "".equals(ssoId)) {
							ssoId = request.getParameter("sso.ssoId");
							if(ssoId != null && !"".equals(ssoId)) {
								CookieUtil.setCookie(hRequest, hResponse, "sso.ssoId", ssoId);
							}
						}

						String descssoId;
						String ssoIdInSession;
						if(StringUtils.isBlank(ssoId)) {
							if(logger.isDebugEnabled()) {
								logger.debug("ssoId is blank");
							}

							try {
								Cookie userId1 = CookieUtil.getCookie(hRequest, "autoLogin");
								Cookie password1 = CookieUtil.getCookie(hRequest, "namePasswd");
								if(userId1 != null && password1 != null) {
									descssoId = userId1.getValue();
									if(CommonConstant.COOKIE_AUTOLOGIN_ON.equals(descssoId)) {
										String pos1 = password1.getValue();
										ssoIdInSession = StringAnalyUtils.decryptString(pos1);
										int needLogin1 = ssoIdInSession.indexOf(2);
										if(needLogin1 != -1) {
											if(logger.isDebugEnabled()) {
												logger.debug("autoLogin cookie is ok!");
											}

											this.redirectToSSO(hRequest, hResponse, false, true, filterChain);
											return;
										}
									}
								}
							} catch (Exception var21) {
								var21.printStackTrace();
							}

							this.handleLogout(hRequest, hResponse, filterChain);
						} else {
							String userId = null;
							String password = null;
							descssoId = decryptString(ssoId);
							if(StringUtils.isBlank(descssoId)) {
								this.handleLogout(hRequest, hResponse, filterChain);
							} else {
								int pos = descssoId.indexOf(2);
								if(pos == -1) {
									this.handleLogout(hRequest, hResponse, filterChain);
								} else {
									userId = descssoId.substring(0, pos);
									password = descssoId.substring(pos + 1);
									ssoIdInSession = (String)hRequest.getSession().getAttribute("sso.ssoId");
									if(!descssoId.equals(ssoIdInSession)) {
										hRequest.getSession().setAttribute("sso.ssoId", descssoId);
									}

									if(logger.isDebugEnabled()) {
										logger.debug("decrypt ssoId, userId:" + userId);
									}

									boolean needLogin = false;
									Authorization auth = this.getLoginUser(hRequest, hResponse);
									if(auth == null || auth.getId() != Integer.valueOf(userId == null?"0":userId).intValue()) {
										if(auth != null) {
											this.logout(hRequest, hResponse);
										}

										if(logger.isDebugEnabled()) {
											logger.debug("requestAuthenticate, userId=" + userId + ", password=" + password);
										}

										needLogin = true;
									}

									User user = null;
									String sessionId = hRequest.getSession().getId();
									if(descssoId.equals(ssoIdInSession)) {
										user = getCache(userId, password, sessionId);
										if(logger.isDebugEnabled()) {
											logger.debug("from cache, user=" + user);
										}
									}

									if(user == null) {
										user = this.requestAuthenticate(hRequest, hResponse, userId, password, false);
										if(logger.isDebugEnabled()) {
											logger.debug("user is null, requestAutheticate:" + user);
										}

										if(user != null) {
											String ssoSessionId = WebUtil.getSSOSessionId((HttpServletRequest)request);
											if(this.isKick(user) && !"0000000000000000".equals(user.getSessionId()) && !ssoSessionId.equals(user.getSessionId())) {
												if(logger.isDebugEnabled()) {
													logger.debug("user is kicked!");
												}

												this.handleLogout((HttpServletRequest)request, (HttpServletResponse)response, true, filterChain);
												return;
											}

											if(logger.isDebugEnabled()) {
												logger.debug("nocache, requestAuthenticate, userId=" + userId + ", password=" + password);
											}

											setCache(userId, password, sessionId, user);
										}

										needLogin = true;
									}

									if(user != null) {
										if(needLogin) {
											if(logger.isDebugEnabled()) {
												logger.debug("login, user=" + user);
											}

											this.login(hRequest, hResponse, user);
										}

										filterChain.doFilter(request, response);
									} else {
										this.handleLogout(hRequest, hResponse, filterChain);
									}
								}
							}
						}
					}
				}
			}
		} else {
			filterChain.doFilter(request, response);
		}
*/

	}

	private static void setCache(String userId, String password, String sessionId, User user) {
		if(userId != null && password != null && user != null) {
			if(userId.length() != 0 && password.length() != 0) {
				Date d = new Date((new Date()).getTime() + 180000L);
				Object[] elements = new Object[]{user, d};
				String key = generateCacheKey(userId, password, sessionId);
				cacheMap.put(key, elements);
			}
		}
	}

	public boolean isKick(User user) {
		return true;
	}
	protected  User requestAuthenticate(HttpServletRequest var1, HttpServletResponse var2, String var3, String var4, boolean var5){
		return null;
	}
	private void handleLogout(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		this.handleLogout(request, response, false, filterChain);
	}

	private void handleLogout(HttpServletRequest request, HttpServletResponse response, boolean isKicked, FilterChain filterChain) throws IOException, ServletException {
		if(logger.isDebugEnabled()) {
			logger.debug("handleLogout ...");
		}

		try {
			request.getSession().removeAttribute("sso.ssoId");
			Authorization uri = this.getLoginUser(request, response);
			if(uri != null) {
				if(logger.isDebugEnabled()) {
					logger.debug("do logout");
				}

				this.logout(request, response);
			}
		} catch (Exception var12) {
			var12.printStackTrace();
			logger.error("Logout error!", var12);
		}

		CookieUtil.deleteCookie(request, response, "sso.ssoId");
		CookieUtil.deleteCookie(request, response, "ssoSessionID");
		CookieUtil.deleteCookie(request, response, "autoLogin");
		CookieUtil.deleteCookie(request, response, "namePasswd");
		CookieUtil.deleteCookie(request, response, "login_from");
		if(isKicked) {
			this.redirectToSSO(request, response, true, false, filterChain);
		} else {
			String uri1;
			if(this.thirdAutoLoginList != null && this.thirdAutoLoginList.size() > 0) {
				uri1 = request.getParameter("Referer");
				if(StringUtils.isBlank(uri1)) {
					uri1 = request.getHeader("Referer");
				}

				if(logger.isDebugEnabled()) {
					logger.debug("refer is:" + uri1);
				}

				if(StringUtils.isNotBlank(uri1) && StringUtils.isBlank(request.getParameter("UnLoginBack"))) {
					String referHost = null;

					try {
						referHost = (new URL(uri1)).getHost();
					} catch (Exception var11) {
						var11.printStackTrace();
					}

					if(StringUtils.isNotBlank(referHost)) {
						Iterator e = this.thirdAutoLoginList.iterator();

						label95:
						while(true) {
							ThirdDomainDTO tdd;
							do {
								do {
									do {
										if(!e.hasNext()) {
											break label95;
										}

										tdd = (ThirdDomainDTO)e.next();
									} while(tdd == null);
								} while(tdd.getDomain() == null);
							} while(!referHost.endsWith(tdd.getDomain()));

							try {
								String e1 = this.getPageURL(request);
								e1 = new String(Base64.encodeBase64(e1.getBytes("UTF-8")), "UTF-8");
								StringBuilder redirectUrlBuilder = new StringBuilder();
								redirectUrlBuilder.append("requestToken.do?type=");
								redirectUrlBuilder.append(tdd.getType());
								redirectUrlBuilder.append("&UnLoginBack=");
								redirectUrlBuilder.append("1");
								redirectUrlBuilder.append("&");
								redirectUrlBuilder.append("next_page");
								redirectUrlBuilder.append("=");
								redirectUrlBuilder.append(e1);
								response.sendRedirect(redirectUrlBuilder.toString());
								return;
							} catch (Exception var13) {
								var13.printStackTrace();
							}
						}
					}
				}
			}

			uri1 = request.getRequestURI();
			if(this.allowAnonymous && !this.isMatch(uri1, this.urlMustLogin)) {
				if(logger.isDebugEnabled()) {
					logger.debug("not must login url!");
				}

				filterChain.doFilter(request, response);
			} else {
				this.redirectToSSO(request, response, filterChain);
			}

		}
	}

	private void redirectToSSO(HttpServletRequest request, HttpServletResponse response, FilterChain f) throws IOException {
		this.redirectToSSO(request, response, false, false, f);
	}



	private static User getCache(String userId, String password, String sessionId) {
		if(userId != null && password != null) {
			if(userId.length() != 0 && password.length() != 0) {
				String key = generateCacheKey(userId, password, sessionId);
				Object[] elements = (Object[])((Object[])cacheMap.get(key));
				if(elements == null) {
					return null;
				} else {
					User user = (User)elements[0];
					Date d = (Date)elements[1];
					if(user != null && d != null) {
						if(d.before(new Date())) {
							cacheMap.remove(key);
							return null;
						} else {
							return user;
						}
					} else {
						cacheMap.remove(key);
						return null;
					}
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	private static String generateCacheKey(String userId, String password, String sessionId) {
		return userId + "," + password + "," + sessionId;
	}
	private void redirectToSSO(HttpServletRequest request, HttpServletResponse response, boolean isKicked, boolean isAutoLogin, FilterChain f) throws IOException {
		String url = this.getPageURL(request);
		url = URLEncoder.encode(url, "utf-8");
		String redirectUrl = this.toLoginURL + "?" + "next_page" + "=" + url;
		if(isKicked) {
			String requestType = request.getHeader("X-Requested-With");
			if(logger.isDebugEnabled()) {
				logger.debug("request type:" + requestType);
			}

			if("XMLHttpRequest".equals(requestType)) {
				this.printWriterAjax(request, response, "error.user.kicked");
				if(logger.isDebugEnabled()) {
					logger.debug("ajax ret:error.user.kicked");
				}

				return;
			}

			redirectUrl = redirectUrl + "&errCode=error.user.kicked";
		}

		if(isAutoLogin || !this.unloginHandle(request, response, isKicked, f)) {
			if(logger.isDebugEnabled()) {
				logger.debug("redirectToSSO, url:" + redirectUrl);
			}

			response.sendRedirect(redirectUrl);
		}

	}
	protected boolean unloginHandle(HttpServletRequest request, HttpServletResponse response, boolean isKicked, FilterChain f) {
		return false;
	}
	private void printWriterAjax(HttpServletRequest request, HttpServletResponse response, String message) {
		String type = request.getParameter("type");
		String callback = request.getParameter("callback");
		if("jsonp".equals(type)) {
			message = callback + "(\"" + message + "\")";
		}

		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("utf-8");

		try {
			PrintWriter e = response.getWriter();
			e.print(message);
			e.close();
		} catch (Exception var7) {
			var7.printStackTrace();
		}

	}
	private String getPageURL(HttpServletRequest request) {
		StringBuffer url = new StringBuffer();
		url.append(request.getRequestURL().toString());
		if(StringUtils.isNotBlank(request.getQueryString())) {
			url.append("?" + request.getQueryString());
		}

		return url.toString();
	}


	private boolean isMatch(String url, List<String> matchedPatterns) {
		if(url == null) {
			return false;
		} else if(matchedPatterns != null && !matchedPatterns.isEmpty()) {
			Iterator lit = matchedPatterns.iterator();

			String matchedPattern;
			do {
				if(!lit.hasNext()) {
					return false;
				}

				matchedPattern = (String)lit.next();
			} while(!this.isMatch(url, matchedPattern));

			return true;
		} else {
			return false;
		}
	}

	private boolean isMatch(String url, String matchedPattern) {
		if(matchedPattern.endsWith("*")) {
			matchedPattern = matchedPattern.substring(0, matchedPattern.length() - 1);
			return url.startsWith(matchedPattern);
		} else if(matchedPattern.startsWith("*")) {
			matchedPattern = matchedPattern.substring(1, matchedPattern.length());
			return url.endsWith(matchedPattern);
		} else {
			return url.endsWith(matchedPattern);
		}
	}

	private static final String decryptString(String source) {
		String tmp = null;

		try {
			byte[] e = StringAnalyUtils.decodeHex(source);
			DESKeySpec dks = new DESKeySpec(desKey);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(dks);
			Cipher c1 = Cipher.getInstance("DES");
			c1.init(2, key);
			byte[] cipherByte = c1.doFinal(e);
			tmp = new String(cipherByte);
		} catch (Exception var8) {
			var8.printStackTrace();
		}

		return tmp;
	}
	public Authorization getLoginUser(HttpServletRequest request,
									  HttpServletResponse response) {
		Object obj = request.getSession().getAttribute("AUTHORIZATION");
		if(obj!=null){
			Authorization auth = (Authorization) obj;
			return auth;
		}else{
			return null;
		}
	}


	public void login(HttpServletRequest request, HttpServletResponse response,
			User user) {
		if(user!=null){
			Authorization au = new Authorization();
			au.setId(user.getId());
			au.setName(user.getUserName());
			au.setChannel("cloud");
			request.getSession().setAttribute("AUTHORIZATION",au);
		}else{
			//sso登录不成功,清除云平台登录信息
			Cookie authCookie = CookieUtil.getCookie(request, CommonConstant.AUTH_STR);
	    	String authId = "0";
	        if(authCookie!=null){
	        	authId = authCookie.getValue();
	        }

	       // CacheTools.addCache(authId,60 * 5, null);
			request.getSession().setAttribute("AUTHORIZATION",null);
			System.out.println("not find users");
		}
	}


	public void logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("AUTHORIZATION");
	}

}
