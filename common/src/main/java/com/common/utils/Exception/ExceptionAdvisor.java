/**
 * 
 */
package com.common.utils.Exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;

/**
 * 异常拦截类
 * <P>File name : ExceptionAdvisor.java </P>

 */
public class ExceptionAdvisor implements ThrowsAdvice {
	private static final Log LOG = LogFactory.getLog(ExceptionAdvisor.class);
	/**
	 * 
	 * <BR>ExceptionAdvisor.afterThrowing()<BR>
	 * @param method
	 * @param args
	 * @param target
	 * @param ex
	 * @throws Throwable
	 */
	public void afterThrowing(Method method, Object[] args, Object target,
			Exception ex) throws Throwable {
		LOG.debug("开始异常回滚 ");
		//WmsRedisManager.getInstance().redisRollback();
		LOG.debug("结束异常回滚 ");
	}
	
}
