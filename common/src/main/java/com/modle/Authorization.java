package com.modle;

import java.io.Serializable;

/**
 * Created by fn on 2017/2/27.
 */
public class Authorization implements Serializable {
    private static final long serialVersionUID = 5066192269230090007L;
    private int id;
    private String name;
    private String channel;
    private int concurrentNum = 1;
    private String mobile;
    private String email;
    private boolean emailVerified;
    private boolean mobileVerified;

    public boolean isEmailVerified() {
        return this.emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isMobileVerified() {
        return this.mobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Authorization() {
    }

    public Authorization(int id, String name, String channel) {
        this.id = id;
        this.name = name;
        this.channel = channel;
        this.concurrentNum = 1;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getChannel() {
        return this.channel;
    }

    public int hashCode() {
        return this.id;
    }

    public boolean equals(Object object) {
        if(object != null) {
            if(object == this) {
                return true;
            }

            if(object instanceof Authorization && object.hashCode() == this.hashCode()) {
                return true;
            }
        }

        return false;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConcurrentNum() {
        return this.concurrentNum;
    }

    public void setConcurrentNum(int concurrentNum) {
        this.concurrentNum = concurrentNum;
    }
}
