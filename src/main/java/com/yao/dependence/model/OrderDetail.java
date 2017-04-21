package com.yao.dependence.model;

import android.text.TextUtils;

import com.yao.devsdk.SdkConfig;
import com.yao.devsdk.log.LoggerUtil;
import com.yao.devsdk.utils.StringKit;

import java.io.Serializable;

/**
 * 订单详情
 * Created by huichuan on 16/6/11.
 */
public class OrderDetail implements Serializable{
    private static final String TAG = "OrderDetail";

    public static final int STATUS_CREATE = 0;//下单
    public static final int STATUS_PAYED = 1;//已支付
    public static final int STATUS_SELLER_CONFIRM = 2;//卖家已接单
    public static final int STATUS_SELLER_FINISH = 3;//卖家确认完成
    public static final int STATUS_ORDER_DONE = 4;//订单完成，可以打款给卖家
    public static final int STATUS_ORDER_CLOSE = 5;//订单未支付，超时被关闭
    public static final int STATUS_REFUND_APPLIED = 6;//退款申请已提交
    public static final int STATUS_REFUND_FINISH = 7;//退款已完成
    public static final int STATUS_ORDER_DONE_CLOSE = 8;//订单已完成，钱已打给卖家


    public static final int PAY_TYPE_UNSELECTED = 0;//未确定
    public static final int PAY_TYPE_ZHIFUBAO = 1;//支付宝
    public static final int PAY_TYPE_WEIXIN = 2;//微信

    public String id;//":"13",
    public String contact;//":"",
    public String buyer_mobile;//":"13300000000",
    public String status;//":"0",
    public String pay_type;//":"0",
    public String price;//":"10",
    public String buyer_comment;//":"",
    public String buyer_tags;//":"",
    public String buyer_evaluate;//":"",
    public String buyer_stars;//":"0",
    public String seller_comment;//":"",
    public String seller_tags;//":"",
    public String seller_evaluate;//":"",
    public String seller_stars;//":"0",
    public String remark;//":"",
    public String created_at;//":"2016-06-08 17:30:40",
    public ServiceDetail service;
    public User buyer;

    /**
     * 是否已支付
     * @return
     */
    public boolean hasPayed(){
        //非下单状态，就都是已支付状态
        return !StringKit.isEquals(status,STATUS_CREATE);
    }

    /**
     * 订单状态
     * @return
     */
    public int getStatus(){
        try {
            int state = Integer.parseInt(status);
            return state;
        } catch (Exception e) {
            LoggerUtil.e(TAG,"格式化异常",e);
            return -1;
        }
    }

    /**
     * 获取订单状态文字描述
     * @return
     */
    public String getStateText(){
        int state = getStatus();

        try {
            String stateText;
            switch (state){
                case STATUS_CREATE:
                    stateText = "已下单";
                    break;
                case STATUS_PAYED:
                    stateText = "已付款";
                    break;
                case STATUS_SELLER_CONFIRM:
                    stateText = "卖家已接单";
                    break;
                case STATUS_SELLER_FINISH:
                    stateText = "卖家确认订单完成";
                    break;
                case STATUS_ORDER_DONE:
                    stateText = "订单已完成";
                    break;
                case STATUS_ORDER_CLOSE:
                    stateText = "订单超时已关闭";
                    break;
                case STATUS_REFUND_APPLIED:
                    stateText = "退款申请已提交";
                    break;
                case STATUS_REFUND_FINISH:
                    stateText = "退款成功";
                    break;
                case STATUS_ORDER_DONE_CLOSE:
                    stateText = "订单完成已关闭";
                    break;
                default:
                    stateText = "订单状态异常("+state+")";
                    break;
            }
            return stateText;

        }catch (Exception e){
            LoggerUtil.e(TAG,"状态异常",e);
            return "未知订单状态("+state+")";
        }

    }


    public String getSellerId(){
        String sellerId = null;
        if (service!=null){
            User user = service.getSeller();
            if (user!=null){
                sellerId = user.getId();
            }
        }

        return sellerId;
    }


    public String getSellerName(){
        String sellerName = null;
        if (service!=null){
            User user = service.getSeller();
            if (user!=null){
                sellerName = user.getName();
            }
        }

        if (TextUtils.isEmpty(sellerName)){
            sellerName = "未知销售者";
        }
        return sellerName;
    }



    public String getSellerTel(){
        String sellerTel = null;
        if (service!=null){
            User user = service.getSeller();
            if (user!=null){
//                sellerTel = user.getId();
            }
        }

        return "110";
    }

    /**
     * 购买者名字
     * @return
     */
    public String getBuyerName(){
        String buyerName = null;
        if (buyer!=null){
            buyerName = buyer.getName();
        }
        if (TextUtils.isEmpty(buyerName)){
            buyerName = "未知购买者";
        }
        return buyerName;
    }

    /**
     * 购买者的id
     * @return
     */
    public String getBuyerId(){
        String buyerId = null;
        if (buyer!=null){
            buyerId = buyer.getId();
        }
        return buyerId;
    }

    /**
     * 是否是我发布的服务
     * @return
     */
    public boolean isMyService(){
        String sellerId = getSellerId();
        return (TextUtils.equals(sellerId, SdkConfig.uid));
    }

    @Override
    public String toString() {
        return "OrderDetailResult{" +
                "id='" + id + '\'' +
                ", contact='" + contact + '\'' +
                ", buyer_mobile='" + buyer_mobile + '\'' +
                ", status='" + status + '\'' +
                ", pay_type='" + pay_type + '\'' +
                ", price='" + price + '\'' +
                ", buyer_comment='" + buyer_comment + '\'' +
                ", buyer_tags='" + buyer_tags + '\'' +
                ", buyer_evaluate='" + buyer_evaluate + '\'' +
                ", buyer_stars='" + buyer_stars + '\'' +
                ", seller_comment='" + seller_comment + '\'' +
                ", seller_tags='" + seller_tags + '\'' +
                ", seller_evaluate='" + seller_evaluate + '\'' +
                ", seller_stars='" + seller_stars + '\'' +
                ", remark='" + remark + '\'' +
                ", created_at='" + created_at + '\'' +
                ", service=" + service +
                ", buyer=" + buyer +
                '}';
    }
}
