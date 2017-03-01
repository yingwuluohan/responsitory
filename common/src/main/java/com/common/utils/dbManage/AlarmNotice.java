package com.common.utils.dbManage;

/**
 * 短信、邮件通知报警接口,后续加入消息队列统一处理
 * <P>File name : AlarmNotice.java </P>
 * <P>Date : 2013-4-7 </P>
 */
public interface AlarmNotice {
	 
	void sms();
	void email();
	void all();

}
