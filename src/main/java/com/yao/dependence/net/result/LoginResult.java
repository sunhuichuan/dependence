package com.yao.dependence.net.result;

import com.yao.dependence.model.User;

/**
 * 登录成功返回的信息，注册成功也会返回此格式
 * Created by huichuan on 16/4/20.
 */
public class LoginResult {
    private String session_key;//": "0b74bcd8721ab506976dbbed85039268",
    private User user;

    public String getSession_key() {
        return session_key;
    }

    public User getUser() {
        return user;
    }


    @Override
    public String toString() {
        return "RegisterResult{" +
                "session_key='" + session_key + '\'' +
                ", user=" + user +
                '}';
    }
}
