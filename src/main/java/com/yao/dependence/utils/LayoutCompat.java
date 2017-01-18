package com.yao.dependence.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * Created by huichuan on 16/8/9.
 */
public class LayoutCompat {


    /**
     * 填充 RecyclerView 的 item
     * @param context
     * @param resId
     * @param parent
     */
    public static final View inflateRecyclerViewItem(Context context, int resId, ViewGroup parent){
        View view = LayoutInflater.from(context).inflate(resId, parent, false);
        return view;
    }

}
