package com.yao.dependence.model;

/**
 * 测试用的feed对象
 * Created by huichuan on 16/4/27.
 */
public class Feed {


    public static final int VIEW_TYPE_LARGE = 3;
    //feed的viewType
    public int viewType;

    public Feed(){
        //默认是item
        viewType = VIEW_TYPE_LARGE;
    }

    //服务feed
    public static final int FEED_TYPE_SERVICE = 0;
    //帖子feed
    public static final int FEED_TYPE_POST = 1;



    public String id;
    public String text;
    public String title;
    public String category;
    public String remark;
    public int feed_type;
    public User user;
    public int price;//: 100,
    public String created_at;//: "Mon Jul 16 14:40:11 +0800 2012",
    public String source;
    //封面图片
    public String cover;
    public boolean truncated;//: false,
//    pic_ids: [ ],
//    geo: [ ],
    public int reposts_count;//: 0,
    public int likes_count;//: 0


    public int getFeed_type() {
        return feed_type;
    }

    //是否是服务feed
    public boolean isServiceFeed(){
        return  feed_type == FEED_TYPE_SERVICE;
    }
    //是否是帖子feed
    public boolean isPostFeed(){
        return  feed_type == FEED_TYPE_POST;
    }

    /**
     * 是View的type,不是对象真的类型
     * @return
     */
    public int getViewType(){
        return VIEW_TYPE_LARGE;
    }


}
