package com.yao.dependence.ui;

import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;


public class BaseRongyunActivity extends BaseActivity{

    //@Bind fields must not be private or static  这都咋在编译的时候就能报错呢？

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
//            actionBar.setLogo(R.drawable.de_bar_logo);//actionbar 添加logo
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * 设置ActionBar的title
     * @param stringResourceId
     */
    protected void setActionBarTitle(int stringResourceId){
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar!=null){
            supportActionBar.setTitle(stringResourceId);
        }
    }
    /**
     * 设置ActionBar的title
     * @param title
     */
    protected void setActionBarTitle(CharSequence title){
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar!=null){
            supportActionBar.setTitle(title);
        }
    }


}
