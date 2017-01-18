package com.yao.dependence.rongyun;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.yao.dependence.R;
import com.yao.dependence.ui.BaseButterActivity;
import com.yao.dependence.widget.titlebar.TitleBarLayout;
import com.yao.devsdk.utils.SdkUtil;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

public class ConversationActivity extends BaseButterActivity {

    //Library 中的 R 文件 id 非 final,故不能用此方法Bind View
//    @Bind(R.id.tbl_title_bar)
    TitleBarLayout tbl_title_bar;

    /**
     * 目标 Id
     */
    private String mTargetId;
    private String mTitle;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    @Override
    protected void initIntent(Intent intent) {

        if (intent!=null){
            Uri intentData = intent.getData();
            mTargetId = intentData.getQueryParameter("targetId");
            mTitle = intentData.getQueryParameter("title");
        }



    }

    @Override
    protected void initTitleBar() {
        tbl_title_bar = (TitleBarLayout) findViewById(R.id.tbl_title_bar);
        if (tbl_title_bar!=null){
            tbl_title_bar.setTitleName(mTitle);
        }

    }

    @Override
    protected int contentViewLayoutId() {
        return R.layout.activity_rongyun_conversation;
    }

    @Override
    protected void initViews() {
        //添加对话列表Containaer
        ConversationFragment fragment = new ConversationFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_conversation_container,fragment).commit();



    }

}