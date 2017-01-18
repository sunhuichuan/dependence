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

/**
 * 底部弹出的dialog
 */
public class BottomDialog extends Dialog {


    protected Context appContext = SdkConfig.getAppContext();
    protected Activity thisContext;


	public BottomDialog(Context context, View view) {
		this(context);
		//为对话框填充内容
		setContentView(view);
	}

	public BottomDialog(Context context) {
		super(context, R.style.ChoiceDialogTheme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(true);

        if (context instanceof Activity){
            thisContext = (Activity) context;
        }else{
            throw new IllegalStateException("dialog context should extends Activity ");
        }
	}


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置弹出的快速面板在屏幕底部
		getWindow().setGravity(Gravity.BOTTOM);
		//把标题栏宽度设置为屏幕宽度
		WindowManager windowManager = getWindow().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams attributes = getWindow().getAttributes();
		attributes.width = display.getWidth();
		getWindow().setAttributes(attributes);
	}




}