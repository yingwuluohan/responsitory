package com.fang.controller.basecontroller;


import com.common.utils.utils.CacheTools;
import com.fang.common.project.CommonConstant;
import com.fang.common.project.CookieUtil;
import com.fang.common.project.KooJedisClient;
import com.fang.service.CacheToolsService;
import com.fang.service.DemoService;
import com.modle.User;
import com.modle.constance.RedisKey;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Controller
public class LoginController extends BaseController {
	private static Log logger = LogFactory.getLog(LoginController.class);
	private String host = "global";
	@Autowired
	private CacheToolsService cacheToolsService;
	@Autowired
	private DemoService demoService;
	//@Autowired
	private KooJedisClient kooJedisClient;

	public KooJedisClient getKooJedisClient() {
		return kooJedisClient;
	}

	public void setKooJedisClient(KooJedisClient kooJedisClient) {
		this.kooJedisClient = kooJedisClient;
	}

	@RequestMapping("/loginPage")
	public String loginPage(HttpServletRequest request){
		request.setAttribute("ssoUrl", host);
		String uuid = UUID.randomUUID().toString().replace("-", "");
		request.setAttribute("uuid", uuid);
		return "/login/loginTest";
	}

	/**
	 * 登录验证码正确性验证
	 * @return
	 */
	@RequestMapping("/randomLogin")
	@ResponseBody
	public Map randomLogin(HttpServletRequest request,HttpServletResponse response){
		String uuid = request.getParameter("uuid");
		Map map = new HashMap();
		map.put("success", true);
		String random = getParamter("random");
		String randomCache = cacheToolsService.getCacheNoTime(RedisKey.getLoginVerifyImage(uuid), String.class);
		if(StringUtils.isBlank(random) || StringUtils.isBlank(randomCache) || !random.equalsIgnoreCase(randomCache)){
			map.put("success", false);
		}
		return map;
	}
	@RequestMapping("/login")
	public String login(HttpServletRequest request,HttpServletResponse response){
		String random = getParamter("random");
		String uuid = request.getParameter("uuid");
		System.out.println( "uuid:" + uuid );
		String mobileEmail = getParamter("mobileEmail");
		User ue = demoService.findUser(mobileEmail);
		if(ue!=null){
			CookieUtil.setCookie(request, response, CommonConstant.AUTH_STR , ue.getId().toString());
			kooJedisClient.set(ue.getId().toString(), ue);
		}
		return "redirect:/chat/goHttpChat";
	}

    @RequestMapping(value="/addUser" , method = RequestMethod.POST)
    public String addUserRoleAndGoHome(HttpServletRequest request, HttpServletResponse response){
        return "redirect:/makeSureUserRole";
    }
	/**
	 * @title: image
	 * @description: 获取图片验证码
	 */
	@RequestMapping("/random")
	public String genVerifyImage(HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		String uuid = request.getParameter("uuid");
		String keyCode = "";
		if("2".equals(type)){//注册时验证码类型
			keyCode = RedisKey.getRegistVerifyImage(uuid);
		}else{
			keyCode = RedisKey.getLoginVerifyImage(uuid);
		}
		try {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			int width = 104, height = 40;
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			OutputStream os = response.getOutputStream();
			Graphics g = image.getGraphics();
			Random random = new Random();
			g.setColor(getRandColor(200, 250));
			g.fillRect(0, 0, width, height);

			g.setFont(new Font("Times New Roman", Font.PLAIN, 30));
			g.setColor(getRandColor(160, 200));
			for (int i = 0; i < 155; i++) {
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int xl = random.nextInt(12);
				int yl = random.nextInt(12);
				g.drawLine(x, y, x + xl, y + yl);
			}
			String sRand = "";
			for (int i = 0; i < 4; i++) {
				int randomInt = Math.abs(random.nextInt()) % 36;
				char code = '0';
				if (randomInt > 9) {
					code = (char) ('A' + randomInt - 10);
					// 大写字母按照计划转小写字母
					if (Math.abs(random.nextInt()) % 2 == 1) {
						code = (char) (code + 32);
					}
				} else {
					code = (char) ('0' + randomInt);
				}
				String rand = String.valueOf(code);
				sRand += rand;
				g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
				g.drawString(rand, 18 * i + 18, 28);
			}
            //五分钟失效缓存
			cacheToolsService.addCache(keyCode, 60*5 , sRand);
			g.dispose();
			ImageIO.write(image, "JPEG", os);
			os.flush();
			os.close();
		}
		catch (IllegalStateException e) {
			logger.error("image validate code error");
			e.printStackTrace();
		}
		catch (IOException e) {
			logger.error("image validate code error");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 注册页面
	 */
	@RequestMapping("/registPage")
	public String registerJsp(HttpServletRequest request){
		int regType = getParamterForInt("role", 2);
		request.setAttribute("regType", regType);
		request.setAttribute("ssoUrl", CommonConstant.HOSTS_SSO);
		String uuid = UUID.randomUUID().toString().replace("-", "");
		request.setAttribute("uuid", uuid);
		return "/login/register";
	}
	/**
	 * 注册方法
	 */
	@RequestMapping("/regist")
	@ResponseBody
	public Map regist(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//本地用户表信息录入
		User ue = new User();
		int ueId = 0;//loginService.insertUser(ue);
		ue.setId(ueId);
		//存储业务系统用户信息
		CookieUtil.setCookie(request, response, host, ue.getId().toString());
		cacheToolsService.addCacheForever(ueId+"", ue);
		return null;
	}
	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255 || fc < 0) {
			fc = 255;
		}
		if (bc > 255 || bc < 0) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response,User  loginUser){
		Cookie authCookie = CookieUtil.getCookie(request, host );
    	String authId = authCookie.getValue();
		cacheToolsService.delCache(authId);
		CookieUtil.deleteCookie(request, response, "sso.ssoId");
		CookieUtil.deleteCookie(request, response, "ssoSessionID");
		return "redirect:/loginPage";
	}
}
