package com.yao.dependence.model;

/**
 *
 * Created by huichuan on 16/6/5.
 */
public class Comment {

    public String id;//": "149",
    public String text;//": "新评论",
    public String pid;//": "155",
    public String post_type;//": "0",
    public String created_at;//": "2016-09-09 16:11:06",
    public User user;

    /**
     * 取得uid
     * @return
     */
    public String getUid(){

        if(user!=null){
            return user.getId();
        }else{
            return null;
        }

    }

    public User getUser() {
        return user;
    }
}
