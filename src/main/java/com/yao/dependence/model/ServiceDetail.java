package com.yao.dependence.model;

import android.text.TextUtils;

import com.yao.dependence.utils.PriceUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 服务详情
 * Created by huichuan on 16/6/11.
 */
public class ServiceDetail implements Serializable{


    private String id;//":"1",
    public String text;//":"This is a post",
    public String title;//":"title",
    public String cover;//": "0",
    private String feed_type;//
    private String created_at;//":"2016-05-23 14:57:32",
    private String source;//":"1",
    private String category;//":"1",
    private String remark;//":"test",
//    private String geo;//":"",
    private List<String> pic_ids;//",
    public String price;//":"10",
    public String price_type;//":"次",
    private String city;//":"1",
    private String likes_count;//":0,
    public User user;


    public String getId() {
        return id;
    }


    public String getPrice() {
        return price;
    }

    public String getCover() {
        return cover;
    }


    //出售服务uid
    public String getSellerUid() {
        if (user == null || TextUtils.isEmpty(user.getId())){
            return null;
        }else{
            return user.getId();
        }
    }


    //用户名字
    public String getSellerName() {
        if (user == null || TextUtils.isEmpty(user.getName())){
            return "未知发布者";
        }else{
            return user.getName();
        }
    }

    public String getSellerProfession() {
        return "服务器无职业字段";
    }

    public String getSellerAvatar() {
        String avatar = null;
        if (user!=null){
            avatar = user.getProfile_image_url();
        }
        return avatar;
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

    public User getSeller() {
        return user;
    }

    /**
     * 价格的文字
     * @return
     */
    public String getPriceText(){
        String priceInfo = "￥ " + PriceUtils.convertServerPrice(price) + "/" + price_type;
        return priceInfo;
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
