package com.common.utils.dbManage;


import com.alibaba.dubbo.common.logger.LoggerFactory;
import org.apache.log4j.Logger;

/**
 * 短信、邮件通知报警接口,后续加入消息队列统一处理
 * <P>File name : DefaultAlarmNotice.java </P>
 * <P>Date : 2013-4-7 </P>
 */
public class DefaultAlarmNotice implements AlarmNotice {
	public void email() {
		// TODO Auto-generated method stub

	}

	public void sms() {
		// TODO Auto-generated method stub

	}
	public void all() {
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
