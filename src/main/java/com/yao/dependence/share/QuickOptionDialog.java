package com.yao.dependence.share;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.yao.dependence.R;


public class QuickOptionDialog extends Dialog {

	public QuickOptionDialog(Context context) {
		this(context, R.style.quick_option_dialog);
	}

	public QuickOptionDialog(Context context, int theme) {
		super(context, theme);
	}

	public QuickOptionDialog(Context context, View view) {
		this(context, R.style.quick_option_dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//为对话框填充内容
		setContentView(view);
	}
	
	public QuickOptionDialog(Context context, boolean cancelable,
							 OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置弹出的快速面板在屏幕底部
		getWindow().setGravity(Gravity.BOTTOM);
		//把标题栏宽度设置为屏幕宽度
		WindowManager windowManager = getWindow().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		LayoutParams attributes = getWindow().getAttributes();
		attributes.width = display.getWidth();
		getWindow().setAttributes(attributes);
	}
	
}
