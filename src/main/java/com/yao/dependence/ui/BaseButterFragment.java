package com.yao.dependence.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * fragment的基类
 */
public abstract class BaseButterFragment extends BaseFragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(contentViewResourceId(), container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    protected abstract int contentViewResourceId();

    /**
     * 初始化子View
     * @param viewRoot
     */
    protected abstract void initViews(View viewRoot);


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
