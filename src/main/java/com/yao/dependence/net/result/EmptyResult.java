package com.yao.dependence.net.result;

/**
 * 空对象，服务器只返回了status和message，没有data
 * Created by huichuan on 16/4/15.
 */
public class EmptyResult {


    @Override
    public String toString() {
        return "EmptyResult{请求成功，data为空}";
    }
}
