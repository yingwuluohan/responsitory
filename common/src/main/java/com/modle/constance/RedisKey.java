package com.modle.constance;

/**
 * @Description:所有存在redis中的key生成
 */
public class RedisKey {

    /**
     * 获取登录图片验证码key
     * @param sessionId
     * @return
     */
    public static final String getLoginVerifyImage(String sessionId){
        return "loginImageCode_"+sessionId;//session key:登录图片验证码
    }
    /**
     * 获取注册图片验证码key
     * @param sessionId
     * @return
     */
	public static final String getRegistVerifyImage(String sessionId) {
		return "registImageCode_"+sessionId;//session key:注册图片验证码
	}
    /**
     * 获取手机验证码key
     * @param mobile
     * @return
     */
	public static final String mobileVerifyCode(String mobile) {
		return "self_mobi_code_"+mobile;
	}

    /**
     * 
    * @title: mobileVerifyCodeTimes
    * @description: 手机验证码发送次数
    * @param mobile
    * @return
    * @throws
     */
    public static final String mobileVerifyCodeTimes(String mobile){
        return "cee_code_times_"+mobile;
    }
    
    /**
     * 获取注册图片验证码key
     * @param sessionId
     * @return
     */
	public static final String getPcenterVerifyCodeEmail(String sessionId) {
		return "pcenter_emails_"+sessionId;//session key:注册图片验证码
	}
}
