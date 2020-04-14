package com.shsxt.crm.query;

import com.shsxt.base.BaseQuery;
import com.shsxt.crm.vo.User;

public class UserQuery extends BaseQuery {
    String userName;
    String phone;
    String trueName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}
