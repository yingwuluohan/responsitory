package com.common.utils.dbManage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 短信、邮件通知报警接口,后续加入消息队列统一处理
 * <P>File name : DefaultAlarmNotice.java </P>
 * <P>Date : 2013-4-7 </P>
 */
public class DefaultAlarmNotice implements AlarmNotice {
	private static final Logger logger = LoggerFactory.getLogger(DefaultAlarmNotice.class);
	public void email() {
		// TODO Auto-generated method stub

	}

	public void sms() {
		// TODO Auto-generated method stub

	}
	public void all() {
		logger.debug("Call Send Email or Sms!");
		email();
		sms();
	}
	
	private static class LazyHolder {
		private static final AlarmNotice INSTANCE = new DefaultAlarmNotice();
	}

	public static AlarmNotice getInstance() {
		return LazyHolder.INSTANCE;
	}

}
