package com.example.storage.modules.sys.pojo.vo.back;


import java.util.Date;

import org.apache.shiro.authc.AuthenticationToken;

public class TokenEntity implements AuthenticationToken {

    private String uuid;

    private String userId;

    private Date timeOut;

    private boolean badToken = true;

    public TokenEntity() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Date timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isBadToken() {
        return badToken;
    }

    public void setBadToken(boolean badToken) {
        this.badToken = badToken;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
