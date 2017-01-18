package com.yao.dependence.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.yao.dependence.model.Category;

import java.io.Serializable;

/**
 * 项目用到的user对象
 * Created by huichuan on 16/4/20.
 */
@DatabaseTable
public class User implements Serializable {
    /**
     * 性别男
     */
    public static final int GENDER_MAN = 0;
    /**
     * 性别女
     */
    public static final int GENDER_WOMAN = 1;


    @DatabaseField(id = true)
    private String id;//: 2489518277,
    @DatabaseField
    private String name;//: "虎贲Geek",
    @DatabaseField
    private String profile_image_url;//": "http=>//tp2.sinaimg.cn/2489518277/50/5617069329/1",
    @DatabaseField
    private int gender;

    private String province;
    private String city;
    private String location;
    private String description;
    private String url;
    private String created_at;
    private boolean verified;
    private int verified_type;
    private String verified_reason;

    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    public Category category;



    public User(){}
    public User(String uid){
        this.id = uid;
    }

    public User(String id,String name,String profile_image_url,int gender){
        this.id = id;
        this.name = name;
        this.profile_image_url = profile_image_url;
        this.gender = gender;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    //TODO
    public String getProfession() {
        return "服务器没有提供职业字段";
    }

    //TODO
    public String getSummary() {
        return "服务器没有提供个人简介字段";
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public int getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
