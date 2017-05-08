package com.fang.common.project.filter;

/**
 * Created by   on 2017/4/28.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.common.utils.utils.CommonUtil;
import com.common.utils.utils.EncryptUtil;
import com.common.utils.utils.SystemGlobals;
import com.common.utils.utils.WebUtil;
import com.fang.common.project.CommonConstant;
import com.fang.common.project.CookieUtil;
import com.modle.Authorization;
import com.modle.User;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.fang.common.project.CommonConstant.HEADER_REFER;


/**
 * @Description:sso登陆filter，客户端使用
 * @author liuyi
 * @date 2013-12-30
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public abstract class LoginFilter extends HttpServlet implements Filter {

    private static final long serialVersionUID = 3790304348005628945L;

    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    private static final String NEXT_PAGE_NAME = "next_page";

    //加解密key
    private static final byte[] desKey = { 64, -77, 35, -45, 16, -22, 121, -15 };

    //sso域名key
    private static String SSO_HOST = "sso.host";
    private static String SSO_HOST2 = "hosts.sso";

    //是否允许匿名登录
    private static String ALLOW_ANONYMOUS = "sso.anonymous";

    protected FilterConfig filterConfig;

    protected String loginURL;

    protected String toLoginURL;

    private String ssoHost;

    protected String authenticateURL;

    protected String validateURL;

    protected boolean allowAnonymous = false; // 是否允许匿名登陆，是：则用户未登录情况不跳转到sso登陆页

    protected List<String> urlPatternInclude;// 如果配置了include，则只对配置中的url做过滤(除了exclude之外的)

    protected List<String> urlPatternExclude;// 如果配置了exclude，则不对配置中的url做过滤

    protected List<String> urlMustLogin;// 在用户未登录时，需要强制跳转到sso登陆页的url

    protected boolean needXdfLogin;//是否需要集团用户回跳自动登陆

    protected String thirdAutoLogin;//是否需要第三方站点回跳自动登录（包括 needXdfLogin )

    private List<ThirdDomainDTO> thirdAutoLoginList = null;

    @SuppressWarnings("unchecked")
    private static Map cacheMap = Collections.synchronizedMap(new HashMap());

    public void init(FilterConfig filterConfig) throws ServletException {

        this.filterConfig = filterConfig;
        ssoHost = filterConfig.getInitParameter(SSO_HOST);
        if (ssoHost == null) {
            ssoHost = filterConfig.getServletContext().getInitParameter(SSO_HOST);
        }
        if (ssoHost == null) {
            try{
                ssoHost = SystemGlobals.getPreference(SSO_HOST);
            }catch(Exception e){
            }
        }
        if (ssoHost == null) {
            ssoHost = filterConfig.getServletContext().getInitParameter(SSO_HOST2);
        }
        if (ssoHost == null) {
            ssoHost = SystemGlobals.getPreference(SSO_HOST2);
        }
        if (StringUtils.isBlank(ssoHost)) {
            throw new RuntimeException("Missing configuration '" + SSO_HOST2 + "'!");
        }
        if (!ssoHost.endsWith("/")) {
            ssoHost += "/";
        }
        if(!ssoHost.endsWith("/sso/")){
            ssoHost += "sso/";
        }

        if(StringUtils.isBlank(this.loginURL)){
            this.loginURL = ssoHost + "login.do";
        }
        if(StringUtils.isBlank(this.toLoginURL)){
            this.toLoginURL = ssoHost + "toLogin.do";
        }
        if(StringUtils.isBlank(this.authenticateURL)){
            this.authenticateURL = ssoHost + "authenticate.do";
        }
        if(StringUtils.isBlank(this.validateURL)){
            this.validateURL = ssoHost + "validate.do";
        }

        if(logger.isDebugEnabled()){
            logger.debug("loginURL:"+loginURL);
            logger.debug("toLoginURL:"+toLoginURL);
            logger.debug("authenticaateURL:"+authenticateURL);
            logger.debug("validateURL:"+validateURL);
        }

        this.allowAnonymous = "true".equals(filterConfig.getInitParameter(ALLOW_ANONYMOUS));
        urlPatternInclude = CommonUtil.strToList(filterConfig.getInitParameter("sso.urlPatternInclude"), ",");
        urlPatternExclude = CommonUtil.strToList(filterConfig.getInitParameter("sso.urlPatternExclude"), ",");
        urlMustLogin = CommonUtil.strToList(filterConfig.getInitParameter("sso.mustLoginUrl"), ",");
        String temp = filterConfig.getInitParameter("sso.needXdfLogin");
        thirdAutoLogin = filterConfig.getInitParameter("sso.thirdAutoLogin");
        if("true".equals(temp)){
            needXdfLogin = true;
        }


        //是否需要第三方站点回跳自动登录（包括 needXdfLogin )
        thirdAutoLoginList = parseThirdAutoLoginDomain(thirdAutoLogin);
        //兼容needXdfLogin
        if (needXdfLogin) {
            if (thirdAutoLoginList == null) {
                thirdAutoLoginList = new ArrayList<ThirdDomainDTO>();
            }

            boolean findXdf = false;
            for (ThirdDomainDTO thirdDomainDTO : thirdAutoLoginList) {
                if (thirdDomainDTO != null) {
                    if ("xdf.cn".equals(thirdDomainDTO.getDomain())) {
                        findXdf = true;
                        break;
                    }
                }
            }
            if (!findXdf) {
                ThirdDomainDTO thirdDomain = new ThirdDomainDTO();
                thirdDomain.setDomain("xdf.cn");
                thirdDomain.setType("xdf");
                thirdAutoLoginList.add(thirdDomain);
            }
        }

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
            ServletException {
        if (!(request instanceof HttpServletRequest && response instanceof HttpServletResponse)) {
            filterChain.doFilter(request, response);
            return;
        }
        HttpServletRequest hRequest = (HttpServletRequest) request;
        HttpServletResponse hResponse = (HttpServletResponse) response;
        String host_name = hRequest.getHeader("host");
        if (host_name == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String uri = hRequest.getRequestURI();
        if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean match = !isMatch(uri, urlPatternExclude);
        if (urlPatternInclude != null && !urlPatternInclude.isEmpty())
            match = match && isMatch(uri, urlPatternInclude);
        if (!match) {
            filterChain.doFilter(request, response);
            return;
        }
        if(logger.isDebugEnabled()){
            logger.debug("URI:"+uri);
        }

        Cookie cookie = CookieUtil.getCookie(hRequest, CommonConstant.SSO_ID);
        String ssoId = null;
        if (cookie != null)
            ssoId = cookie.getValue();

        if (ssoId == null || "".equals(ssoId)) {
            // 对于跨域的情况，用Parameter来处理
            ssoId = request.getParameter(CommonConstant.SSO_ID);
            if (ssoId != null && !"".equals(ssoId)) {
                CookieUtil.setCookie(hRequest, hResponse, CommonConstant.SSO_ID, ssoId);
            }
        }
        // 没有ssoId 表示没有登录过
        if (StringUtils.isBlank(ssoId)) {
            if(logger.isDebugEnabled()){
                logger.debug("ssoId is blank");
            }
            try{
                //看是否有自动登陆的key
                Cookie autoLoginCookie = CookieUtil.getCookie(hRequest, CommonConstant.COOKIE_AUTOLOGIN_KEY);
                Cookie cNamePasswd = CookieUtil.getCookie(hRequest, CommonConstant.COOKIE_NAMEPASS_KEY);
                if (autoLoginCookie != null && cNamePasswd != null) {
                    String autoLogin = autoLoginCookie.getValue();
                    if (CommonConstant.COOKIE_AUTOLOGIN_ON.equals(autoLogin)) {
                        String namePasswd = cNamePasswd.getValue();
                        String descNamePasswd = EncryptUtil.decryptString(namePasswd);
                        int pos = descNamePasswd.indexOf('\002');
                        if (pos != -1) {//基本可以确认自动登陆可以成功
                            if(logger.isDebugEnabled()){
                                logger.debug("autoLogin cookie is ok!");
                            }
                            redirectToSSO(hRequest, hResponse, false, true, filterChain);
                            return;
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            handleLogout(hRequest, hResponse, filterChain);
            return;
        }
        String userId = null;
        String password = null;
        String descssoId = decryptString(ssoId);
        if(StringUtils.isBlank(descssoId)){
            handleLogout(hRequest, hResponse, filterChain);
            return;
        }
        int pos = descssoId.indexOf('\002');
        if (pos != -1) {
            userId = descssoId.substring(0, pos);
            password = descssoId.substring(pos + 1);
        }else{
            handleLogout(hRequest, hResponse, filterChain);
            return;
        }
        String ssoIdInSession = (String) hRequest.getSession().getAttribute(CommonConstant.SSO_ID);
        // 如果session过期、失效，重置session(需要对session过期的处理应该在此进行)
        if (!descssoId.equals(ssoIdInSession)) {
            hRequest.getSession().setAttribute(CommonConstant.SSO_ID, descssoId);
        }
        if(logger.isDebugEnabled()){
            logger.debug("decrypt ssoId, userId:"+userId);
        }

        // 问问client是否还有用户信息
        boolean needLogin = false;
        Authorization auth = getLoginUser(hRequest, hResponse);
        if (auth == null || auth.getId() != Integer.valueOf(userId==null?"0":userId)) {
            if (auth != null) {
                logout(hRequest, hResponse);// 调用客户端的退出
            }
            if (logger.isDebugEnabled()) {
                logger.debug("requestAuthenticate, userId=" + userId + ", password=" + password);
            }
            needLogin = true;
        }
        User user = null;
        String sessionId = hRequest.getSession().getId();
        if (descssoId.equals(ssoIdInSession)) {
            //ssoId正确时直接取cache中的用户即可
            user = getCache(userId, password, sessionId);
            if(logger.isDebugEnabled()){
                logger.debug("from cache, user=" + user);
            }
        }
        if(user == null){
            user = requestAuthenticate(hRequest, hResponse, userId, password, false);
            if(logger.isDebugEnabled()){
                logger.debug("user is null, requestAutheticate:"+user);
            }
            if (user != null) {
                String ssoSessionId = WebUtil.getSSOSessionId((HttpServletRequest)request);
                if(isKick(user) && (!"0000000000000000".equals(user.getSessionId()) && !ssoSessionId.equals(user.getSessionId()))){
                    //被踢掉了
                    if(logger.isDebugEnabled()){
                        logger.debug("user is kicked!");
                    }
                    handleLogout((HttpServletRequest)request, (HttpServletResponse)response, true, filterChain);
                    return;
                }
                if(logger.isDebugEnabled()){
                    logger.debug("nocache, requestAuthenticate, userId=" + userId + ", password=" + password);
                }
                setCache(userId, password, sessionId, user);
            }
            needLogin = true;
        }
        if (user != null) {
            if(needLogin){
                if (logger.isDebugEnabled()) {
                    logger.debug("login, user=" + user);
                }
                login(hRequest, hResponse, user);
            }
        } else {
            handleLogout(hRequest, hResponse, filterChain);
            return;
        }
        filterChain.doFilter(request, response);
    }

    public abstract boolean isKick(User user);

    private void handleLogout(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        handleLogout(request, response, false, filterChain);
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response, boolean isKicked, FilterChain filterChain) throws IOException, ServletException {
        if(logger.isDebugEnabled()){
            logger.debug("handleLogout ...");
        }
        // 先注销
        try {
            request.getSession().removeAttribute(CommonConstant.SSO_ID);
            Authorization auth = getLoginUser(request, response);
            // 注销流程
            if (auth != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("do logout");
                }
                logout(request, response);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Logout error!", e);
        }
        //清空cookie
        CookieUtil.deleteCookie(request, response, CommonConstant.SSO_ID);
        CookieUtil.deleteCookie(request, response, "ssoSessionID");
        CookieUtil.deleteCookie(request, response, "autoLogin");
        CookieUtil.deleteCookie(request, response, "namePasswd");
        CookieUtil.deleteCookie(request, response, "login_from" );

        if(isKicked){
            //被踢掉的 无条件跳转到登陆页面并提示
            redirectToSSO(request, response, true, false, filterChain);
            return;
        }

        //不管是不是需要必须登陆
        if (thirdAutoLoginList != null && thirdAutoLoginList.size() > 0) {
            //1. 判断从哪儿来
            String refer = request.getParameter( HEADER_REFER );
            if (StringUtils.isBlank(refer)) {
                refer = request.getHeader( "Referer" );
            }

            if (logger.isDebugEnabled()) {
                logger.debug("refer is:" + refer);
            }

            if (StringUtils.isNotBlank(refer) && StringUtils.isBlank(request.getParameter("UnLoginBack"))) {
                String referHost = null;
                try {
                    referHost = new URL(refer).getHost();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(StringUtils.isNotBlank(referHost)){
                    for (ThirdDomainDTO tdd : thirdAutoLoginList) {
                        if (tdd != null && tdd.getDomain() != null && referHost.endsWith(tdd.getDomain())) {
                            try {
                                String url = getPageURL(request);
                                //url = new BASE64Encoder().encode(url.getBytes("utf-8"));
                                //url = Base64.encodeBase64String(url.getBytes("utf-8"));
                                url = new String(Base64.encodeBase64(url.getBytes("UTF-8")), "UTF-8");

                                //2. request token
                                StringBuilder redirectUrlBuilder = new StringBuilder();
                                redirectUrlBuilder.append(getMainDomain(ssoHost));
                                redirectUrlBuilder.append("requestToken.do?type=");
                                redirectUrlBuilder.append(tdd.getType());
                                redirectUrlBuilder.append("&UnLoginBack=");
                                redirectUrlBuilder.append( "1" );
                                redirectUrlBuilder.append("&");
                                redirectUrlBuilder.append(NEXT_PAGE_NAME);
                                redirectUrlBuilder.append("=");
                                redirectUrlBuilder.append(url);
                                response.sendRedirect(redirectUrlBuilder.toString());
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        String uri = request.getRequestURI();
        if (!allowAnonymous || isMatch(uri, urlMustLogin)) {
            // 不允许匿名登录
            redirectToSSO(request, response, filterChain);
        } else {
            // 匿名登陆,即不跳转到sso登陆页,什么也不做
            if(logger.isDebugEnabled()){
                logger.debug("not must login url!");
            }
            filterChain.doFilter(request, response);
        }
    }

    private String getMainDomain(String ssoHost) {
        return ssoHost.replace("koo.cn", "koolearn.com");
    }

    private void redirectToSSO(HttpServletRequest request, HttpServletResponse response, FilterChain f) throws IOException {
        redirectToSSO(request, response, false, false, f);
    }

    private void redirectToSSO(HttpServletRequest request,
                               HttpServletResponse response, boolean isKicked, boolean isAutoLogin, FilterChain f) throws IOException {
        String url = getPageURL(request);
        url = URLEncoder.encode(url, "utf-8");

        String redirectUrl = this.toLoginURL + "?" + NEXT_PAGE_NAME + "=" + url;
        if(isKicked){
            //踢人的情况需要根据请求类型，如果是ajax请求则根据是否jsonp返回一个字符串，如果是浏览器发起的请求才redirect
            String requestType = request.getHeader("X-Requested-With");
            if(logger.isDebugEnabled()){
                logger.debug("request type:"+requestType);
            }
            if ("XMLHttpRequest".equals(requestType)) {
                // ajax请求
                printWriterAjax(request, response, "error.user.kicked");
                if(logger.isDebugEnabled()){
                    logger.debug("ajax ret:error.user.kicked");
                }
                return;
            } else{
                redirectUrl = redirectUrl + "&errCode=error.user.kicked";
            }
        }
        if(isAutoLogin || !unloginHandle(request, response, isKicked, f)){
            if(logger.isDebugEnabled()){
                logger.debug("redirectToSSO, url:"+redirectUrl);
            }
            response.sendRedirect(redirectUrl);
        }
    }

    //是否有客户端定制的未登录处理方式, 返回true表示客户端已经处理了，false表示
    protected boolean unloginHandle(HttpServletRequest request, HttpServletResponse response, boolean isKicked, FilterChain f){
        return false;
    }

    private void printWriterAjax(HttpServletRequest request, HttpServletResponse response, String message) {

        String type = request.getParameter("type");
        String callback = request.getParameter("callback");
        if ("jsonp".equals(type)) {
            message = callback + "(\"" + message + "\")";
        }
        response.setContentType("text/html; charset=utf-8");
//        response.setCharacterEncoding("utf-8");
        try {
            PrintWriter pwriter = response.getWriter();
            pwriter.print(message);
            pwriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getPageURL(HttpServletRequest request) {
        StringBuffer url = new StringBuffer();
        url.append(request.getRequestURL().toString());
        if (StringUtils.isNotBlank(request.getQueryString())) {
            url.append("?" + request.getQueryString());
        }
        return url.toString();
    }

    private boolean isMatch(String url, List<String> matchedPatterns) {
        if (url == null)
            return false;
        if (matchedPatterns == null || matchedPatterns.isEmpty())
            return false;

        Iterator<String> lit = matchedPatterns.iterator();
        while (lit.hasNext()) {
            String matchedPattern = (String) lit.next();
            if (isMatch(url, matchedPattern))
                return true;
        }
        return false;
    }

    private boolean isMatch(String url, String matchedPattern) {
        if (matchedPattern.endsWith("*")) {
            matchedPattern = matchedPattern.substring(0, matchedPattern.length() - 1);
            return url.startsWith(matchedPattern);
        } else if (matchedPattern.startsWith("*")) {
            matchedPattern = matchedPattern.substring(1, matchedPattern.length());
            return url.endsWith(matchedPattern);
        } else {
            return url.endsWith(matchedPattern);
        }
    }

    private static final String decryptString(String source) {
        // 用密钥解密密文
        String tmp = null;
        try {
            byte[] bytes = EncryptUtil.decodeHex(source);

            DESKeySpec dks = new DESKeySpec(desKey);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            Cipher c1 = Cipher.getInstance("DE");
            c1.init(Cipher.DECRYPT_MODE, key);
            byte[] cipherByte = c1.doFinal(bytes);

            tmp = new String(cipherByte);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }

    private static User getCache(String userId, String password, String sessionId) {
        if (userId == null || password == null)
            return null;
        if (userId.length() == 0 || password.length() == 0)
            return null;

        String key = generateCacheKey(userId, password, sessionId);
        Object[] elements = (Object[]) cacheMap.get(key);
        if (elements == null){
            return null;
        }

        User user = (User) elements[0];
        Date d = (Date) elements[1];
        if (user == null || d == null) {
            cacheMap.remove(key);
            return null;
        }

        if (d.before(new Date())) {
            cacheMap.remove(key);
            return null;
        } else {
            return user;
        }
    }

    /**
     * 将下面格式的字符串解析成实体对象ThirdDomainDTO。
     * domain=xdf.cn,type=xdf;domain=zhiup.com,type=zhiup
     *
     * @param str 待解析字符串
     * @return List<ThirdDomainDTO>
     */
    private static List<ThirdDomainDTO> parseThirdAutoLoginDomain(String str) {
        List<ThirdDomainDTO> thirdDomainList = null;
        if (StringUtils.isNotBlank(str)) {
            thirdDomainList = new ArrayList<ThirdDomainDTO>();
            String[] domains = str.split(";");
            if (domains.length > 0) {
                String[] domainInfoArr;
                for (String domainEle : domains) {
                    if (StringUtils.isNotBlank(domainEle)) {

                        //domain=xdf.cn,type=xdf
                        domainInfoArr = domainEle.split(",");

                        if (domainInfoArr.length > 0) {
                            ThirdDomainDTO thirdDomainDTO = new ThirdDomainDTO();
                            thirdDomainList.add(thirdDomainDTO);
                            String[] eles;
                            for (String domainInfo : domainInfoArr) {
                                if (StringUtils.isNotBlank(domainInfo)) {
                                    eles = domainInfo.split("=");
                                    if (eles.length > 1) {
                                        eles[0] = StringUtils.lowerCase(StringUtils.trim(eles[0]));
                                        eles[1] = StringUtils.lowerCase(StringUtils.trim(eles[1]));
                                        if ("domain".equals(eles[0])) {
                                            thirdDomainDTO.setDomain(eles[1]);
                                        } else if ("type".equals(eles[0])) {
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

    public static void removeCache(String userId, String password, String sessionId) {
        if (userId == null || password == null)
            return;
        if (userId.length() == 0 || password.length() == 0)
            return;

        String key = generateCacheKey(userId, password, sessionId);
        cacheMap.remove(key);
    }

    @SuppressWarnings("unchecked")
    private static void setCache(String userId, String password,String sessionId, User user) {
        if (userId == null || password == null || user == null)
            return;
        if (userId.length() == 0 || password.length() == 0)
            return;

        Date d = new Date(new Date().getTime() + 180000L);
        Object[] elements = new Object[2];
        elements[0] = user;
        elements[1] = d;

        String key = generateCacheKey(userId, password, sessionId);
        cacheMap.put(key, elements);
    }

    private static String generateCacheKey(String userId, String password, String sessionId){
        return userId+","+password+","+sessionId;
    }

    /**
     * 得到当前登录用户<br/>
     * 本方法应该在应用系统中的sso filter实现
     */
    public abstract Authorization getLoginUser(HttpServletRequest request, HttpServletResponse response);

    /**
     * 应用系统的登录处理 本方法应该在应用系统中的sso filter实现
     */
    public abstract void login(HttpServletRequest request, HttpServletResponse response, User user);

    /**
     * 应用系统的退出登录处理<br/>
     * 本方法应该在应用系统中的sso filter实现
     */
    public abstract void logout(HttpServletRequest request, HttpServletResponse response);

    /**
     * 本方法请求SSO系统，返回用户信息 SSOFilterLocal、SSOFilterRemote中实现，用于区分本地认证和远程认证<br/>
     */
    protected abstract User requestAuthenticate(HttpServletRequest request, HttpServletResponse response,
                                                 String userId, String password, boolean loadFromDB);

}