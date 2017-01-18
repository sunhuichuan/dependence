package com.yao.dependence.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.yao.dependence.R;

/**
 * Dialog的工具类
 * Created by huichuan on 16/9/17.
 */
public class DialogUtils {

    /**
     * 自定义View的dialog
     * @return
     */
    public static Dialog showContentViewDialog(Activity activity,String title,View contentView,String positiveStr,String negativeStr,
            final DialogInterface.OnClickListener positiveClickListener){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setView(contentView);
        builder.setPositiveButton(positiveStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                positiveClickListener.onClick(dialog,which);
            }
        });
        builder.setNegativeButton(negativeStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });



        AlertDialog alertDialog = builder.show();

        return alertDialog;

    }

    /**
     * 通用的警告提示
     */
    public static Dialog showAlertDialog(Activity activity,String message,
            final DialogInterface.OnClickListener positiveClickListener){
        return showAlertDialog(activity,"警告",message,"确定",positiveClickListener);
    }

    /**
     * 自定义View的dialog
     * @param activity
     * @param title
     * @param positiveStr
     * @param positiveClickListener
     * @return
     */
    public static Dialog showAlertDialog(Activity activity,String title,String message,String positiveStr,
            final DialogInterface.OnClickListener positiveClickListener){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(positiveStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                positiveClickListener.onClick(dialog,which);
            }
        });
        AlertDialog alertDialog = builder.show();

        return alertDialog;

    }
    /**
     * 自定义loading的dialog
     * @return
     */
    public static Dialog showLoadingDialog(Activity activity,String text){

        View contentView = View.inflate(activity, R.layout.dialog_loading_progress, null);
        TextView tv_text = (TextView) contentView.findViewById(R.id.tv_loading_text);
        tv_text.setText(text);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-2,-2);

        Dialog loadingDialog = new Dialog(activity,R.style.LoadingDialog);
        loadingDialog.setContentView(contentView,params);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        return loadingDialog;

    }


    /**
     * 弹出对话框
     *
     * @param title
     * @param message
     */
    public static Dialog showDialog(Activity activity, String title, String message) {
        AlertDialog dialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title).setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.show();
        return dialog;

    }

    /**
     * 弹出对话框
     *
     * @param title
     * @param message
     */
    public static Dialog showDialog(Activity activity, String title, String message, String confirmStr, String cancleStr, final DialogInterface.OnClickListener confirmListener, final DialogInterface.OnClickListener cancelListener) {
        AlertDialog dialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(confirmStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (confirmListener!=null){
                            confirmListener.onClick(dialog, which);
                        }

                        dialog.dismiss();
                    }
                })
                .setNegativeButton(cancleStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cancelListener!=null){
                            cancelListener.onClick(dialog, which);
                        }

                        dialog.dismiss();
                    }
                });

        dialog = builder.show();
        return dialog;

    }



}
