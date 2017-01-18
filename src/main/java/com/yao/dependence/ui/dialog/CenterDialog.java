package com.yao.dependence.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yao.dependence.R;
import com.yao.devsdk.SdkConfig;
import com.yao.devsdk.utils.DisplayUtil;

/**
 * 屏幕中间弹出的dialog
 */
public class CenterDialog extends Dialog {

	protected Context appContext = SdkConfig.getAppContext();
    protected Activity thisContext;

	public CenterDialog(Activity context, View view) {
		this(context);
		//为对话框填充内容
		setContentView(view);
	}

	public CenterDialog(Activity context) {
		super(context, R.style.ChoiceDialogTheme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(true);
        thisContext = context;
	}


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置弹出的快速面板在屏幕底部
		getWindow().setGravity(Gravity.CENTER);
		//把标题栏宽度设置为屏幕宽度
		WindowManager windowManager = getWindow().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams attributes = getWindow().getAttributes();
		attributes.width = display.getWidth();
		getWindow().setAttributes(attributes);
	}




}