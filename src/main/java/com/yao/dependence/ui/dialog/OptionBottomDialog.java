package com.yao.dependence.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yao.dependence.R;
import com.yao.dependence.utils.SchemeKit;
import com.yao.devsdk.SdkConfig;
import com.yao.devsdk.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by huichuan on 16/9/20.
 */
public abstract class OptionBottomDialog<T> extends BottomDialog{

    protected T dialogInfo;
    //每个item的高度
    public static final int ITEM_COUNT = DisplayUtil.dip2px(SdkConfig.getAppContext(),52);
    protected OptionBottomDialog thisDialog;
    private LinearLayout ll_titleContent;
    private LinearLayout ll_dialogContent;
    //dialog条目的点击事件
    private OnItemClickListener mOnItemClickListener;



    public OptionBottomDialog(Context context) {
        super(context);
        thisDialog = this;
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_choice_sheet_menu, null);
        setContentView(contentView);
        initViews(contentView);
    }

    void initViews(View rootView){
        //title内容
        ll_titleContent = (LinearLayout) findViewById(R.id.ll_titleContent);
//        ll_titleContent.removeAllViews();
        ll_titleContent.setVisibility(View.GONE);
        //item存放的容器
        ll_dialogContent = (LinearLayout) findViewById(R.id.ll_dialogContent);
        ll_dialogContent.removeAllViews();

        View bottom_cancel = findViewById(R.id.bottom_cancel);
        final OptionItem item = createCancelItem();
        bottom_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.doAction(thisContext,thisDialog,dialogInfo,mOnItemClickListener);
                dismiss();
            }
        });

    }



    /**
     * 设置操作对象
     * @param info
     */
    public void setOperateInfo(T info){
        dialogInfo = info;
    }


    /**
     * 设置内容的item
     */
    public void setTitleText(String titleText){
        TextView tv_titleText = (TextView) ll_titleContent.findViewById(R.id.tv_titleText);
        if (tv_titleText!=null){
            tv_titleText.setText(titleText);
            ll_titleContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏标题内容
     */
    public void hideTitleText(){
        ll_titleContent.setVisibility(View.GONE);
    }

    /**
     * 设置内容的item
     */
    public void setContentItem(OptionItem item){
        List<OptionItem> itemList = new ArrayList<>();
        itemList.add(item);
        setContentItem(itemList);
    }

    /**
     * 设置内容的item
     */
    public void setContentItem(List<OptionItem> itemList){
        ll_dialogContent.removeAllViews();
        for (OptionItem item : itemList){
            boolean isDangerous = item.isDangerItem();
            View itemView = createItem(item,isDangerous);
            ll_dialogContent.addView(itemView);
        }
    }


    private View createItem(OptionItem item,boolean isDangerous){
        return createItem(item,isDangerous,true);
    }

    /**
     * 创建dialog的item
     * @param item
     * @param addDivider
     * @return
     */
    private View createItem(final OptionItem item,boolean isDangerous,boolean addDivider){
        LinearLayout ll_item = new LinearLayout(thisContext);
        ll_item.setBackgroundResource(R.drawable.item_bg_color);
        ll_item.setOrientation(LinearLayout.VERTICAL);
        //增加dialog的item
        TextView tv_contentText = new TextView(thisContext);
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(-1, ITEM_COUNT);
        tv_contentText.setLayoutParams(contentParams);
        tv_contentText.setGravity(Gravity.CENTER);
        if (isDangerous){
            tv_contentText.setTextColor(Color.RED);
        }else{
            tv_contentText.setTextColor(Color.parseColor("#545454"));
        }
        tv_contentText.setTextSize(16);
        tv_contentText.setText(item.getText());
        ll_item.addView(tv_contentText);
        if (addDivider){
            //增加分割线
            View v_divider = new View(thisContext);
            LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(-1, 1);
            v_divider.setLayoutParams(dividerParams);
            v_divider.setBackgroundColor(Color.parseColor("#dbdbdb"));
            ll_item.addView(v_divider);
        }
        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.doAction(thisContext,thisDialog,dialogInfo,mOnItemClickListener);
            }
        });
        return ll_item;
    }

    /**
     * 让子类创建一个取消item对象
     * @return
     */
    protected abstract OptionItem createCancelItem();



    /**
     * 设置条目点击事件
     * @param clickListener
     */
    public void setOnItemClickListener(OnItemClickListener clickListener){
        mOnItemClickListener = clickListener;
    }


    public interface OnItemClickListener<T>{
        /**
         * 把所有的参数都返回给调用方，可以自定义处理类型
         * @param type 因type调用频繁，把type单独提出来供外部调用
         */
        void onClick(OptionBottomDialog dialog, T cardInfo, int type, OptionItem item);

    }




    /**
     * item包装的对象
     */
    public static abstract class OptionItem<T>{


        //确定
        public static final int TYPE_CONFIRM = 1;
        //取消
        public static final int TYPE_CANCEL = 2;


        //item需要显示的文字
        protected String text;
        //item的类型
        protected int type;
        //需要操作的scheme
        protected String scheme;

        public OptionItem(int type){
            this(type,"");
        }
        public OptionItem(int type,String scheme){
            this.type = type;
            this.scheme = scheme;
            equipText();
        }

        public void doAction(Context context,OptionBottomDialog dialog,T cardInfo,OnItemClickListener clickListener){
            if (!TextUtils.isEmpty(scheme)){
                //有scheme直接处理
                SchemeKit.openScheme(context,scheme);
            }
            //通知listener
            if (clickListener!=null){
                clickListener.onClick(dialog,cardInfo,type,this);
            }
        }

        /**
         * text
         * @return
         */
        public String getText() {
            return text;
        }

        /**
         * 设置对应文字
         */
        public void equipText() {
            switch (type){
                case TYPE_CONFIRM:
                    text = "确定";
                    break;
                case TYPE_CANCEL:
                    text = "取消";
                    break;
                default:
                    text = "未知条目";
                    break;
            }
        }

        /**
         * 是否是危险的item
         * @return
         */
        public boolean isDangerItem(){
            return (type == TYPE_CONFIRM);
        }

        /**
         * 类型对应的文字描述
         * @return
         */
        public abstract String getTypeText();
    }



}
