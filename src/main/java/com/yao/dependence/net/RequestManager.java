package com.yao.dependence.net;

import com.yao.dependence.net.request.LoginBean;
import com.yao.dependence.net.request.RegisterBean;
import com.yao.dependence.net.result.CategoryConfigResult;
import com.yao.dependence.model.City;
import com.yao.dependence.model.Comment;
import com.yao.dependence.net.result.EmptyResult;
import com.yao.dependence.model.Feed;
import com.yao.dependence.net.result.HomeFeedResult;
import com.yao.dependence.net.result.LoginResult;
import com.yao.dependence.model.OrderDetail;
import com.yao.dependence.model.PostDetail;
import com.yao.dependence.model.Praise;
import com.yao.dependence.model.RongyunToken;
import com.yao.dependence.model.ServiceDetail;
import com.yao.dependence.net.result.UploadImageResult;
import com.yao.dependence.model.User;
import com.yao.dependence.net.result.WeixinOrderResult;
import com.yao.dependence.model.AlipayOrder;
import com.yao.devsdk.log.LogUtil;
import com.yao.devsdk.net.HttpClient;
import com.yao.devsdk.net.StringRequestBody;
import com.yao.devsdk.net.response.HttpResult;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 请求管理者
 * Created by huichuan on 16/4/14.
 */
public class RequestManager {
    private static final String TAG = "RequestManager";
    private static RequestManager ourInstance = new RequestManager();

    public static RequestManager getInstance() {
        return ourInstance;
    }

    APIService mRequestService;
    APIService mRequestServiceProxy;

    private RequestManager() {
        HttpClient<APIService> request = HttpClient.getInstance(APIService.HOST,APIService.class);
        mRequestService = request.getRequestService();


        mRequestServiceProxy = (APIService) Proxy.newProxyInstance(APIService.class.getClassLoader(),
                new Class[]{APIService.class},
                new ApiServiceProxyHandler(mRequestService));

    }

    /**
     * 调用多个api组合时，应该会需要apiService的引用
     * @return
     */
    public APIService getRequestService(){
        return mRequestService;
    }

    /**
     * 返回Service的包装
     * @return
     */
    public APIService getRequestServiceProxy(){
        return mRequestServiceProxy;
    }


//    /**
//     * 获取配置的频道
//     */
//    public Observable<HttpResult<CategoryConfigResult>> getConfigCategory(){
//       return mRequestServiceProxy.getConfigCategory(APIService.CONFIG_CATEGORY)
//                .compose(new HttpClient.ConfigTransformer<HttpResult<CategoryConfigResult>>());
//    }



    /**
     * 用于Profile页面显示的完整城市列表
     */
    public Observable<HttpResult<List<City>>> cityListProfile(){
        return mRequestServiceProxy.getConfigCity(APIService.CONFIG_PROFILE_CITY)
                .compose(new HttpClient.ConfigTransformer<HttpResult<List<City>>>());

    }

    /**
     * 获取发布服务的城市列表
     * @param subscriber 由调用者传过来的观察者对象
     */
    public void cityListPublishService(Subscriber<List<City>> subscriber){
        mRequestServiceProxy.getConfigCity(APIService.CONFIG_CITY)
                .map(new HttpClient.HttpResultFunc<List<City>>())
                .compose(new HttpClient.ConfigTransformer<List<City>>())
                .subscribe(subscriber);

    }



    /**
     * 用于获取验证码
     * @param scene 1:用户注册 2:找回密码
     */
    public void getCheckCode(String mobilePhone,String scene,Subscriber<EmptyResult> subscriber){
        mRequestServiceProxy.getCheckCode(APIService.SIGNUP_CODE, mobilePhone, scene)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                //添加线程调度
                .compose(new HttpClient.ConfigTransformer<EmptyResult>())
                .subscribe(subscriber);
    }
    /**
     * 检查手机号是否被注册
     */
    public void checkSignUpState(String mobilePhone, Subscriber<EmptyResult> subscriber){
        mRequestServiceProxy.checkSignUpState(APIService.SIGNUP_CHECK, mobilePhone)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                //添加线程调度
                .compose(new HttpClient.ConfigTransformer<EmptyResult>())
                .subscribe(subscriber);
    }

//    /**
//     * 提交注册信息
//     */
//    public Observable<LoginResult> register(String mobilePhone, String nickname, String password, String code,int type,String xuid,String auth_data){
//        xuid = (xuid==null?"":xuid);
//        auth_data = (auth_data==null?"":auth_data);
//        return mRequestServiceProxy.register(APIService.SIGNUP_SUBMIT, mobilePhone, nickname, password, code,type,xuid,auth_data)
//                .map(new HttpClient.HttpResultFunc<LoginResult>())
//                .compose(new HttpClient.ConfigTransformer<LoginResult>());
//    }


    /**
     * 提交注册信息
     * 注册的请求bean
     * @param bean
     * @return
     */
    public Observable<LoginResult> register(RegisterBean bean){

        return mRequestServiceProxy.register(APIService.SIGNUP_SUBMIT, bean.mobile_phone, bean.nickname, bean.password, bean.code, bean.type, bean.xuid, bean.auth_data)
                .map(new HttpClient.HttpResultFunc<LoginResult>())
                .compose(new HttpClient.ConfigTransformer<LoginResult>());

    }

//    /**
//     * 获取用户feed流
//     * @param params 请求所需要的参数
//     */
//    public Observable<HomeFeedResult> getFeedStream(HashMap<String,String> params){
//        HashMap<String,String> paramMap = new HashMap<>();
//        paramMap.put("action", APIService.FEED_STREAM);
//        paramMap.putAll(params);
////                .put("loadmore",isLoadMore ? "1" : "0");
//        return mRequestServiceProxy.getFeedStream(paramMap)
//                .map(new HttpClient.HttpResultFunc<HomeFeedResult>())
//                .compose(new HttpClient.ConfigTransformer<HomeFeedResult>());
//    }
    /**
     * 登录
     */
    public Observable<LoginResult> login(LoginBean bean){
        return mRequestServiceProxy.login(APIService.SESSION_LOGIN, bean.mobile_phone, bean.password, bean.type, bean.xuid, bean.auth_data)
                .map(new HttpClient.HttpResultFunc<LoginResult>())
                .compose(new HttpClient.ConfigTransformer<LoginResult>());
    }

    /**
     * 上传图片
     */
    public Call<HttpResult<UploadImageResult>> uploadImage(RequestBody image, final String imageFilePath, final String imageCompressFilePath){
        //创建nickNameBody对象
        StringRequestBody actionBody = StringRequestBody.create(MediaType.parse("text/plain"),"action", APIService.UPLOAD_IMAGE);
        return mRequestServiceProxy.uploadImage(actionBody, image);
    }

    /**
     * 发布帖子
     */
    public Observable<EmptyResult> publishArticle(Map<String, String> postAddParams){

        return mRequestServiceProxy.publishArticle(APIService.PUBLISH_ARTICLE, postAddParams)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }

    /**
     * 发布服务
     */
    public Call<HttpResult<ServiceDetail>> publishService(Map<String,String> params){

        return mRequestServiceProxy.publishService(APIService.PUBLISH_SERVICE, params);
//                .map(new HttpClient.HttpResultFunc<EmptyResult>())
//                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }



    /**
     * 服务详情
     */
    public Observable<ServiceDetail> serviceDetail(String id){

        return mRequestServiceProxy.getServiceDetail(APIService.SERVICE_SHOW, id)
                .map(new HttpClient.HttpResultFunc<ServiceDetail>())
                .compose(new HttpClient.ConfigTransformer<ServiceDetail>());
    }

    /**
     * 帖子详情
     */
    public Observable<PostDetail> postDetail(String id){

        return mRequestServiceProxy.postDetail(APIService.POST_SHOW, id)
                .map(new HttpClient.HttpResultFunc<PostDetail>())
                .compose(new HttpClient.ConfigTransformer<PostDetail>());
    }





    /**
     * 获取订单
     */
    public Call<HttpResult<OrderDetail>> addOrder(String service_id, String contract, String buyer_mobile, String remark){

        return mRequestServiceProxy.addOrder(APIService.ORDER_ADD, service_id, contract, buyer_mobile, remark);
    }



    /**
     * 获取已付款订单列表
     */
    public Observable<List<OrderDetail>> getConsumerOrderList(Map<String,String> params){

        return mRequestServiceProxy.getConsumerOrderList(APIService.ORDER_BUY_LIST, params)
                .map(new HttpClient.HttpResultFunc<List<OrderDetail>>())
                .compose(new HttpClient.ConfigTransformer<List<OrderDetail>>());
    }



    /**
     * 订单消息列表
     */
    public Observable<List<OrderDetail>> orderMessageList(Map<String,String> params){
        String uidType = "buyer";//"seller"
        return mRequestServiceProxy.orderMessageList(APIService.ORDER_MESSAGE_LIST, uidType,params)
                .map(new HttpClient.HttpResultFunc<List<OrderDetail>>())
                .compose(new HttpClient.ConfigTransformer<List<OrderDetail>>());
    }

    /**
     * 获取已售出订单列表
     */
    public Observable<List<OrderDetail>> getMerchantOrderList(Map<String,String> params){

        return mRequestServiceProxy.getMerchantOrderList(APIService.ORDER_SELL_LIST, params)
                .map(new HttpClient.HttpResultFunc<List<OrderDetail>>())
                .compose(new HttpClient.ConfigTransformer<List<OrderDetail>>());
    }


    /**
     * 获取订单
     */
    public Observable<OrderDetail> getOrderDetail(String orderId){

        return mRequestServiceProxy.getOrderDetail(APIService.ORDER_SHOW, orderId)
                .map(new HttpClient.HttpResultFunc<OrderDetail>())
                .compose(new HttpClient.ConfigTransformer<OrderDetail>());
    }


    /**
     * 用微信支付订单
     */
    public Call<HttpResult<WeixinOrderResult>> payOrderByWeixin(String orderId){

        return mRequestServiceProxy.payOrderWeixin(APIService.PAY_ORDER_WX, orderId);
    }

    /**
     * 用支付宝支付订单
     */
    public Observable<AlipayOrder> payOrderByAlipay(String orderId){
        return mRequestServiceProxy.payOrderAlipay(APIService.PAY_ORDER_ALIPAY, orderId)
                .map(new HttpClient.HttpResultFunc<AlipayOrder>())
                .compose(new HttpClient.ConfigTransformer<AlipayOrder>());
    }
    /**
     * 卖家接单
     */
    public Observable<EmptyResult> orderSellerConfirmed(String orderId){
        return mRequestServiceProxy.orderSellerConfirmed(APIService.ORDER_SELLER_CONFIRMED, orderId)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }
    /**
     * 卖家确认订单完成
     */
    public Observable<EmptyResult> orderSellerFinished(String orderId){
        return mRequestServiceProxy.orderSellerConfirmed(APIService.ORDER_SELLER_FINISHED, orderId)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }
    /**
     * 买家确认订单完成
     * @param buyerStars 买家评分
     */
    public Observable<EmptyResult> orderBuyerConfirmDone(String orderId,String buyerStars){
        return mRequestServiceProxy.orderBuyerConfirmDone(APIService.ORDER_BUYER_CONFIRM_DONE, orderId,buyerStars)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }
    /**
     * 买家退款
     */
    public Observable<EmptyResult> orderBuyerRefund(String orderId){
        return mRequestServiceProxy.orderBuyerRefund(APIService.ORDER_BUYER_REFUND, orderId)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }


    /********------------------------------用户赞------------------------------*************/

    /**
     * 给用户点赞
     */
    public Observable<EmptyResult> praiseUser(String userId){
        return mRequestServiceProxy.praiseUser(APIService.PRAISE_USER, userId)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }

    /**
     * 取消用户点赞
     */
    public Observable<EmptyResult> cancelPraiseUser(String orderId){
        return mRequestServiceProxy.cancelPraiseUser(APIService.CANCEL_PRAISE_USER, orderId)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }

    /**
     * 是否给此用户点赞
     */
    public Observable<EmptyResult> isPraiseUser(String userId){
        return mRequestServiceProxy.cancelPraiseUser(APIService.IS_PRAISE_USER, userId)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }

    /**
     * 我的赞列表
     */
    public Observable<List<Praise>> myPraiseList(Map<String,String> params){
        return mRequestServiceProxy.praiseFromMeList(APIService.MY_PRAISE_LIST, params)
                .map(new HttpClient.HttpResultFunc<List<Praise>>())
                .compose(new HttpClient.ConfigTransformer<List<Praise>>());
    }
    /**
     * 赞我的列表
     */
    public Observable<List<Praise>> praiseMeList(Map<String,String> params){
        return mRequestServiceProxy.praiseFromMeList(APIService.PRAISE_ME_LIST, params)
                .map(new HttpClient.HttpResultFunc<List<Praise>>())
                .compose(new HttpClient.ConfigTransformer<List<Praise>>());
    }

    /***------------用户信息-------------------***/

    /**
     * 用户信息
     */
    public Call<HttpResult<User>> userInfo(String userId){
        return mRequestServiceProxy.userInfo(APIService.USER_INFO, userId);
//                .map(new HttpClient.HttpResultFunc<User>())
//                .compose(new HttpClient.ConfigTransformer<User>());
    }
    /**
     * 用户信息
     */
    public Observable<User> getUserInfo(String userId){

        Observable<User> userObservable = Observable.just(userId).map(new Func1<String, User>() {
            @Override
            public User call(String userId) {
                try {
                    Call<HttpResult<User>> httpResultCall = userInfo(userId);
                    Response<HttpResult<User>> resultResponse = httpResultCall.execute();
                    HttpResult<User> httpResult = resultResponse.body();

                    if (httpResult.getStatus() != 1) {
                        LogUtil.e(TAG, "获取用户信息失败：" + httpResult.getStatus() + "---" + httpResult.getMessage());
                        return null;
                    } else {
                        User user = httpResult.getData();
                        return user;
                    }

                } catch (IOException e) {
                    LogUtil.e(TAG, "获取用户信息异常", e);
                    return null;
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return userObservable;
    }
    /**
     * 融云token
     */
    public Observable<RongyunToken> rongyunToken(String userId, String nickName, String portraitUri){
        return mRequestServiceProxy.rongyunToken(APIService.RONGYUN_TOKEN, userId,nickName,portraitUri)
                .map(new HttpClient.HttpResultFunc<RongyunToken>())
                .compose(new HttpClient.ConfigTransformer<RongyunToken>());
    }


    /***----------------搜索相关-----------------------***/


    /**
     * 热搜词接口
     */
    public Observable<List<String>> hotSearchWord(){
        return mRequestServiceProxy.hotSearchWord(APIService.HOT_SEARCH_WORD)
                .map(new HttpClient.HttpResultFunc<List<String>>())
                .compose(new HttpClient.ConfigTransformer<List<String>>());
    }

    /**
     * 搜索服务或帖子
     */
    public Observable<List<Feed>> searchFeed(String keyword,int type,Map<String, String> params){
        return mRequestServiceProxy.searchFeed(APIService.SEARCH_FEED, keyword, type, params)
                .map(new HttpClient.HttpResultFunc<List<Feed>>())
                .compose(new HttpClient.ConfigTransformer<List<Feed>>());
    }
    /**
     * 搜索用户
     */
    public Observable<List<User>> searchUser(String keyword,Map<String, String> params){
        return mRequestServiceProxy.searchUser(APIService.SEARCH_USER, keyword, params)
                .map(new HttpClient.HttpResultFunc<List<User>>())
                .compose(new HttpClient.ConfigTransformer<List<User>>());
    }


    /****----------------我的相关------------------------***/
    /**
     * 服务列表
     */
    public Observable<List<ServiceDetail>> serviceList(String uid,Map<String,String> params){
        return mRequestServiceProxy.serviceList(APIService.SERVICE_LIST, uid, params)
                .map(new HttpClient.HttpResultFunc<List<ServiceDetail>>())
                .compose(new HttpClient.ConfigTransformer<List<ServiceDetail>>());
    }
    /**
     * 帖子列表
     */
    public Observable<List<PostDetail>> postList(String uid,Map<String,String> params){
        return mRequestServiceProxy.postList(APIService.POST_LIST, uid, params)
                .map(new HttpClient.HttpResultFunc<List<PostDetail>>())
                .compose(new HttpClient.ConfigTransformer<List<PostDetail>>());
    }

    /**
     * 服务的评论列表
     */
    public Observable<List<Comment>> serviceCommentList(String id, Map<String,String> params){
        return mRequestServiceProxy.commentList(APIService.COMMENT_LIST, Feed.FEED_TYPE_SERVICE, id, params)
                .map(new HttpClient.HttpResultFunc<List<Comment>>())
                .compose(new HttpClient.ConfigTransformer<List<Comment>>());
    }

    /**
     * 帖子的评论列表
     */
    public Observable<List<Comment>> postCommentList(String pid, Map<String,String> params){
        return mRequestServiceProxy.commentList(APIService.COMMENT_LIST, Feed.FEED_TYPE_POST, pid, params)
                .map(new HttpClient.HttpResultFunc<List<Comment>>())
                .compose(new HttpClient.ConfigTransformer<List<Comment>>());
    }
    /**
     * 对我的评论列表
     */
    public Observable<List<Comment>> commentToMeList(Map<String,String> params){
        return mRequestServiceProxy.commentToMeList(APIService.COMMENT_TO_ME, params)
                .map(new HttpClient.HttpResultFunc<List<Comment>>())
                .compose(new HttpClient.ConfigTransformer<List<Comment>>());
    }
    /**
     * 新增帖子的评论
     */
    public Observable<EmptyResult> addCommentToPost(String pid, String text){
        return mRequestServiceProxy.addComment(APIService.COMMENT_SERVICE_POST, Feed.FEED_TYPE_POST, pid, text)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }

    /**
     * 新增服务的评论
     */
    public Observable<EmptyResult> addCommentToService(String pid, String text){
        return mRequestServiceProxy.addComment(APIService.COMMENT_SERVICE_POST, Feed.FEED_TYPE_SERVICE, pid, text)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }

    /**
     * 赞帖子
     */
    public Observable<EmptyResult> praiseService(String pid){
        return mRequestServiceProxy.praiseServicePost(APIService.PRAISE_SERVICE_POST, Feed.FEED_TYPE_SERVICE, pid)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }

    /**
     * 赞服务
     */
    public Observable<EmptyResult> praisePost(String pid){
        return mRequestServiceProxy.praiseServicePost(APIService.PRAISE_SERVICE_POST, Feed.FEED_TYPE_POST, pid)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }

    /**
     * 赞帖子
     */
    public Observable<EmptyResult> cancelPraiseService(String pid){
        return mRequestServiceProxy.cancelPraiseServicePost(APIService.CANCEL_PRAISE_SERVICE_POST, Feed.FEED_TYPE_SERVICE, pid)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }

    /**
     * 赞服务
     */
    public Observable<EmptyResult> cancelPraisePost(String pid){
        return mRequestServiceProxy.cancelPraiseServicePost(APIService.CANCEL_PRAISE_SERVICE_POST, Feed.FEED_TYPE_POST, pid)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }
    /**
     * 是否赞帖子
     */
    public Observable<EmptyResult> isPraiseService(String pid){
        return mRequestServiceProxy.praiseServicePost(APIService.IS_PRAISE_SERVICE_POST, Feed.FEED_TYPE_SERVICE, pid)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }

    /**
     * 是否赞服务
     */
    public Observable<EmptyResult> isPraisePost(String pid){
        return mRequestServiceProxy.praiseServicePost(APIService.IS_PRAISE_SERVICE_POST, Feed.FEED_TYPE_POST, pid)
                .map(new HttpClient.HttpResultFunc<EmptyResult>())
                .compose(new HttpClient.ConfigTransformer<EmptyResult>());
    }




}
