package com.fang.global.service.impl;

import com.modle.User;

/**
 * Created by fn on 2017/9/5.
 */
public class ChildUser extends User {

    private String childName ;
    private int tokenId;

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }
}
