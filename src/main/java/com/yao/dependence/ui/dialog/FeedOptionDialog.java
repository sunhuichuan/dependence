package com.yao.dependence.ui.dialog;

import android.content.Context;

/**
 * feed流中需要弹出的sheet菜单dialog
 * //TODO 测试使用，未验证
 * Created by huichuan on 16/9/20.
 */
public class FeedOptionDialog  extends OptionBottomDialog{


    public FeedOptionDialog(Context context) {
        super(context);
    }

    @Override
    protected OptionItem createCancelItem() {
        FeedOptionItem item = new FeedOptionItem(FeedOptionItem.TYPE_CANCEL);
        return item;
    }

    /**
     * item包装的对象
     */
    public static class FeedOptionItem extends OptionItem<String>{
        //增加一些item类型
        //打回文章
        public static final int TYPE_REJECT_ARTICLE = 3;
        //返回首页
        public static final int TYPE_GO_WEIBO_HOME = 4;



        public FeedOptionItem(int type) {
            super(type);
        }

        public FeedOptionItem(int type, String scheme) {
            super(type, scheme);
        }

        @Override
        public String getTypeText() {

            String text;
            switch (type){
                case TYPE_REJECT_ARTICLE:
                    text = "打回文章";
                    break;
                case TYPE_GO_WEIBO_HOME:
                    text = "返回首页";
                    break;
                default:
                    text = "未知条目";
                    break;
            }
            return text;
        }
    }



}
