package com.common.utils.quartz;

import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
* @author  dfn  : 
* @date 创建时间：2016年1月6日 下午4:08:06 
* @version 1.0 
* @parameter   
* @return  
*/
public class QuartzUtil {
	/*

	 public static final String TRIGGER_DATA_METHOD_NAME = "callbackMethod";
	    public static final String TRIGGER_DATA_METHOD_ARG = "methodArg";
	    static final String JOB_DATA_BEAN_CLASS = "beanClass";
	    public static final String RETRY_COUNT = "retryCount";
	    public static final String RETRY_INTERVAL_MILLIS = "retryIntervalMillis";

	    static TriggerKey createTriggerKey(JobDetailImpl jobDetail, Method method) {
	        return new TriggerKey(method.getName(), jobDetail.getFullName());
	    }

	    static JobDetailImpl createJobDetail(Class<?> targetClass, String description) {
	        JobDetailImpl jobDetail = createJobDetail(targetClass);
	        jobDetail.setDescription(description);
	        return jobDetail;
	    }

	    static JobDetailImpl createJobDetail(Class<?> targetClass) {
	        JobDataMap jobDataMap = new JobDataMap();
	        jobDataMap.put(JOB_DATA_BEAN_CLASS, targetClass.getName());
	        jobDataMap.putAsString(RETRY_COUNT, 3);
	        jobDataMap.putAsString(RETRY_INTERVAL_MILLIS, 30);
	        JobKey jobKey = getJobKey(targetClass);
	        return createJobDetail(jobKey, jobDataMap);
	    }

	    private static JobKey getJobKey(Class<?> targetClass) {
	        return new JobKey(targetClass.getSimpleName(), targetClass.getPackage().getName());
	    }

	    private static JobDetailImpl createJobDetail(JobKey jobKey, JobDataMap jobDataMap) {
	        JobDetailImpl jobDetail = new JobDetailImpl();
	        jobDetail.setKey(jobKey);
	        jobDetail.getJobDataMap().putAll(jobDataMap);
	        jobDetail.setJobClass(BeanMethodInvokingJob.class);
	        jobDetail.setDurability(true);
	        //recovery job会造成SimpleTrigger重复执行的问题
	        jobDetail.setRequestsRecovery(false);
	        return jobDetail;
	    }

	    static Method getRealMethod(Method method, Object bean) {
	        if (AopUtils.isJdkDynamicProxy(bean)) {
	            try {
	                // Found a @QuartzScheduled method on the target class for this JDK proxy ->
	                // is it also present on the proxy itself?
	                method = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
	            } catch (SecurityException ex) {
	                ReflectionUtils.handleReflectionException(ex);
	            } catch (NoSuchMethodException ex) {
	                throw new IllegalStateException(String.format(
	                        "@QuartzScheduled method '%s' found on bean target class '%s' but not " +
	                        "found in any interface(s) for a dynamic proxy. Either pull the " +
	                        "method up to a declared interface or switch to subclass (CGLIB) " +
	                        "proxies by setting proxy-target-class/proxyTargetClass to 'true'",
	                        method.getName(), method.getDeclaringClass().getSimpleName()));
	            }
	        } else if (AopUtils.isCglibProxy(bean)) {
	            // Common problem: private methods end up in the proxy instance, not getting delegated.
	            if (Modifier.isPrivate(method.getModifiers())) {
	                throw new IllegalStateException(String.format(
	                        "@QuartzScheduled method '%s' found on CGLIB proxy for target class '%s' but cannot " +
	                        "be delegated to target bean. Switch its visibility to package or protected.",
	                        method.getName(), method.getDeclaringClass().getSimpleName()));
	            }
	        }
	        return method;
	    }

	    */
}
