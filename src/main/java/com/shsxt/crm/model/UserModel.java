package com.shsxt.crm.model;

public class UserModel {

    private String userIdStr;

    private String userName;

    private Integer trueName;

    public UserModel() {
    }

    public UserModel(String userIdStr, String userName, Integer trueName) {
        this.userIdStr = userIdStr;
        this.userName = userName;
        this.trueName = trueName;
    }

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getTrueName() {
        return trueName;
    }

    public void setTrueName(Integer trueName) {
        this.trueName = trueName;
    }
}
