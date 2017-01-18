package com.yao.dependence.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子详情
 * Created by huichuan on 16/6/11.
 */
public class PostDetail implements Serializable{


    private String id;//":"1",
    private String text;//":"This is a post",
    private String title;//":"title",
    private String cover;//": "0",
    private String feed_type;//
    private String created_at;//":"2016-05-23 14:57:32",
    private String source;//":"1",
    private String category;//":"1",
    private String remark;//":"test",
//    private String geo;//":"",
    private List<String> pic_ids;//",
    private String price;//":"10",
    private String price_type;//":"次",
    private String city;//":"1",
    private String likes_count;//":0,
    private User user;


    public String getId() {
        return id;
    }


    public String getPrice() {
        return price;
    }

    public String getCover() {
        return cover;
    }

    //用户名字
    public String getUserName() {
        if (user == null || TextUtils.isEmpty(user.getName())){
            return "未知发布者";
        }else{
            return user.getName();
        }
    }

    public String getPrice_type() {
        return price_type;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "ServiceDetail{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", created_at='" + created_at + '\'' +
                ", source='" + source + '\'' +
                ", category='" + category + '\'' +
                ", remark='" + remark + '\'' +
                ", pic_ids=" + pic_ids +
                ", price='" + price + '\'' +
                ", price_type='" + price_type + '\'' +
                ", city='" + city + '\'' +
                ", likes_count='" + likes_count + '\'' +
                ", user=" + user +
                '}';
    }
}
