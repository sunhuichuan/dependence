package com.yao.dependence.utils;

import com.yao.devsdk.log.LoggerUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SchemeKit{
	
	private static final String TAG = "ForwardUtils";
	

	public static void startActivity(Context context, String scheme){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(scheme));
		if (context instanceof Activity) {
			try {
				context.startActivity(intent);
			} catch (Exception e) {
				LoggerUtil.e(TAG, "start Activity Exception ,应该是Activity没有找到");// TODO: handle exception
			}
		} else {
			LoggerUtil.e(TAG, "context is not an Activity");
		}
	}

	/**
	 * 通过微博SchemeUtils打开scheme
	 * @param context
	 * @param scheme
     */
	public static void openScheme(Context context, String scheme){
//		SchemeUtils.openScheme(context,scheme);
	}
	
}
