package com.common.utils.modle;

/**
 * Created by fn on 2017/2/28.
 */
public class ThirdDomainDTO {
    private String domain;
    private String type;

    public ThirdDomainDTO() {
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return "ThirdDomainDTO{domain=\'" + this.domain + '\'' + ", type=\'" + this.type + '\'' + '}';
    }
}
