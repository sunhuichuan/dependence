package com.yao.dependence.model;

/**
 *
 * Created by huichuan on 16/8/30.
 */
public class RongyunToken {

    public String code;//": 200,
    public String userId;//": "31",
    public String token;//": "o1GfqjZM1oRJFfeE+UwOLWWS86NK5NGB/ucLYS7yTT6tK5dwM0bNYCB8VLhwDys3LDDbHJwGlZMyjojJzTmCSA=="


    @Override
    public String toString() {
        return "RongyunToken{" +
                "code='" + code + '\'' +
                ", userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
