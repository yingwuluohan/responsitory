package com.dubbo.util.dto;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * dubbo的所有字段属性
 * Dubbo Invoke Base Entity
 *
 * @author Created by miaoyoumeng on 2015/12/18.
 */
public class DubboInvoke implements Serializable {

    private static final long serialVersionUID = -5390912914194741697L;

    private String id;

    private Date invokeDate;

    private String service;

    private String method;

    private String consumer;

    private String provider;

    private String type;

    private double success;

    private double failure;

    private double elapsed;

    private double concurrent;

    private double maxElapsed;

    private double maxConcurrent;

    private double invokeTime;

    private String application;

    // ====================查询辅助参数===================
    /**
     * 统计时间粒度(毫秒)
     */
    private long timeParticle = 60000;

    private Date invokeDateFrom;

    private Date invokeDateTo;

    public Date getInvokeDate() {
        return invokeDate;
    }

    public void setInvokeDate(Date invokeDate) {
        this.invokeDate = invokeDate;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getType() {
        if (StringUtils.isEmpty(type)) {
            return "consumer";
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTimeParticle() {
        return timeParticle;
    }

    public void setTimeParticle(Long timeParticle) {
        this.timeParticle = timeParticle;
    }

    public Date getInvokeDateFrom() {
        return invokeDateFrom;
    }

    public void setInvokeDateFrom(Date invokeDateFrom) {
        this.invokeDateFrom = invokeDateFrom;
    }

    public Date getInvokeDateTo() {
        return invokeDateTo;
    }

    public void setInvokeDateTo(Date invokeDateTo) {
        this.invokeDateTo = invokeDateTo;
    }


    public double getSuccess() {
        return success;
    }

    public void setSuccess(double success) {
        this.success = success;
    }

    public double getFailure() {
        return failure;
    }

    public void setFailure(double failure) {
        this.failure = failure;
    }

    public double getElapsed() {
        return elapsed;
    }

    public void setElapsed(double elapsed) {
        this.elapsed = elapsed;
    }

    public double getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(double concurrent) {
        this.concurrent = concurrent;
    }

    public double getMaxElapsed() {
        return maxElapsed;
    }

    public void setMaxElapsed(double maxElapsed) {
        this.maxElapsed = maxElapsed;
    }

    public double getMaxConcurrent() {
        return maxConcurrent;
    }

    public void setMaxConcurrent(double maxConcurrent) {
        this.maxConcurrent = maxConcurrent;
    }

    public void setTimeParticle(long timeParticle) {
        this.timeParticle = timeParticle;
    }

    public double getInvokeTime() {
        return invokeTime;
    }

    public void setInvokeTime(double invokeTime) {
        this.invokeTime = invokeTime;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

}
