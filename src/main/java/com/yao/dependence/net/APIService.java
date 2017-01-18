package com.yao.dependence.net;

import com.yao.dependence.net.result.CategoryConfigResult;
import com.yao.dependence.model.City;
import com.yao.dependence.model.Comment;
import com.yao.dependence.net.result.EmptyResult;
import com.yao.dependence.model.Feed;
import com.yao.dependence.net.result.HelpCenterUrlResult;
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
import com.yao.devsdk.net.response.HttpResult;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

//没法挪到dependce工程，因为有很多Bean没法一起挪动
public interface APIService {
    String GATEWAY_PATH = "gateway.php";
    String TEST_ECHO_PATH = "echo.php";

    String HOST_OFFICIAL = "http://182.92.5.164/slash/";
    String HOST_YAO_WEI = "http://182.92.5.164/slash_yaowei/";

    /**
     * app的host
     */
    String HOST = HOST_OFFICIAL;
    String PATH = GATEWAY_PATH;


    //配置分类
    String CONFIG_CATEGORY = "config_category";
    //profile页面的完整城市列表
    String CONFIG_PROFILE_CITY = "config_profile_city";
    //发布服务的城市列表
    String CONFIG_CITY = "config_city";
    //获取验证码
    String SIGNUP_CODE = "signup_code";
    //检查手机号是否已经注册
    String SIGNUP_CHECK = "signup_check";
    //提交注册信息
    String SIGNUP_SUBMIT = "signup_submit";
    //获取用户信息流
    String FEED_STREAM = "feed_stream";
    //登陆验证
    String SESSION_LOGIN = "session_login";
    //上传图片
    String UPLOAD_IMAGE = "image_upload";
    //发表帖子
    String PUBLISH_ARTICLE = "post_add";
    //帖子详情
    String POST_SHOW = "post_show";
    //发布服务
    String PUBLISH_SERVICE = "service_add";
    //查看服务
    String SERVICE_SHOW = "service_show";
    //买家订单列表
    String ORDER_BUY_LIST = "order_list";
    //卖家订单列表
    String ORDER_SELL_LIST = "order_sell_list";
    //订单消息列表
    String ORDER_MESSAGE_LIST = "order_message_list";
    //获取订单
    String ORDER_SHOW = "order_show";
    //新增订单
    String ORDER_ADD = "order_add";
    //微信支付订单
    String PAY_ORDER_WX = "pay_order_wx";
    //支付宝支付订单
    String PAY_ORDER_ALIPAY = "pay_order_alipay";
    //卖家确认接单
    String ORDER_SELLER_CONFIRMED = "order_seller_confirmed";
    //卖家确认完成订单
    String ORDER_SELLER_FINISHED = "order_seller_finished";
    //买家确认完成订单,订单结束
    String ORDER_BUYER_CONFIRM_DONE = "order_done";
    //买家申请退款
    String ORDER_BUYER_REFUND = "order_refund_applied";
    //给用户点赞
    String PRAISE_USER = "nod_add";
    //取消用户点赞
    String CANCEL_PRAISE_USER = "nod_delete";
    //是否给用户点赞
    String IS_PRAISE_USER = "nod_exist";
    //我的赞列表
    String MY_PRAISE_LIST = "nod_my_list";
    //赞我的列表
    String PRAISE_ME_LIST = "nod_to_me_list";
    //用户信息
    String USER_INFO = "user_show";
    //融云token
    String RONGYUN_TOKEN = "rongcloud_gettoken";

    //热搜词接口
    String HOT_SEARCH_WORD = "search_hot_word";
    //搜索帖子或服务接口
    String SEARCH_FEED = "search_feed";
    //搜索用户接口
    String SEARCH_USER = "search_user";
    //服务列表接口
    String SERVICE_LIST = "service_user_list";

    //帖子列表接口
    String POST_LIST = "post_user_list";
    //评论的列表接口
    String COMMENT_LIST = "comment_list";
    //我收到的评论接口
    String COMMENT_TO_ME = "comment_to_me_list";
    //评论服务帖子接口
    String COMMENT_SERVICE_POST = "comment_add";
    //赞服务帖子接口
    String PRAISE_SERVICE_POST = "like_add";
    //取消赞服务帖子接口
    String CANCEL_PRAISE_SERVICE_POST = "like_delete";
    //是否赞服务帖子接口
    String IS_PRAISE_SERVICE_POST = "like_exist";
    //服务中心的接口
    String HELP_CENTER_URL = "help_center";


    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

//    @GET("/users/{username}")
//    Call<User> getUser(@Path("username") String username);
//
//    @GET("/group/{id}/users")
//    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);
//
//    @POST("/users/new")
//    Call<User> createUser(@Body User user);


    /**
     * 测试post请求写法
     *
     * @return
     */
    @FormUrlEncoded
    @POST("gateway.php")
    Observable<HttpResult<CategoryConfigResult>> testPostCheckCode(@Field("action") String action, @Field("mobile_phone") String mobilePhone, @Field("scene") String scene);


    /**
     * 请求城市列表
     *
     * @param action
     * @return
     */
    @GET("gateway.php")
    Observable<HttpResult<List<City>>> getConfigCity(@Query("action") String action);

    /**
     * 请求分类配置
     *
     * @param action
     * @return
     */
    @GET("gateway.php")
    Observable<HttpResult<CategoryConfigResult>> getConfigCategory(@Query("action") String action);

    /**
     * 请求验证码
     *
     * @param action
     * @param mobilePhone 用户提交的手机号
     * @param scene       需要使用验证码的场景 1: 用户注册 2: 找回密码
     * @return
     */
    @GET("gateway.php")
    Observable<HttpResult<EmptyResult>> getCheckCode(@Query("action") String action, @Query("mobile_phone") String mobilePhone, @Query("scene") String scene);

    /**
     * 检查手机号是否被注册
     *
     * @param action
     * @param mobilePhone
     * @return
     */
    @GET("gateway.php")
    Observable<HttpResult<EmptyResult>> checkSignUpState(@Query("action") String action, @Query("mobile_phone") String mobilePhone);

    /**
     * 提交注册
     *
     * @param action      signup_submit
     * @param mobilePhone 用户提交的手机号
     * @param nickname    用户昵称（2-9个中文英文数字和下划线字符）
     * @param password    用户密码（已使用md5 32位加密） 用户密码强度的检查需在前端做出
     * @param code        手机验证码 （验证码有可能以0开头，所以验证码是字符串而不是int）
     * @return
     */
    @FormUrlEncoded
    @POST("gateway.php")
    Observable<HttpResult<LoginResult>> register(@Field("action") String action, @Field("mobile_phone") String mobilePhone, @Field("nickname") String nickname, @Field("password") String password, @Field("code") String code, @Field("type") int type, @Field("xuid") String xuid, @Field("auth_data") String auth_data);


    /**
     * 获取用户feed流
     *
     * @param params 请求需要的参数
     * @return
     */
    @GET("gateway.php")
    Observable<HttpResult<HomeFeedResult>> getFeedStream(@QueryMap Map<String, String> params);

    /**
     * 登陆验证
     *
     * @param action
     * @param mobilePhone
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST(PATH)
    Observable<HttpResult<LoginResult>> login(@Field("action") String action, @Field("mobile_phone") String mobilePhone, @Field("password") String password, @Field("type") int type, @Field("xuid") String xuid, @Field("auth_data") String auth_data);

    /**
     * 上传图片
     *
     * @param action
     * @param file
     * @return
     */
    @Multipart
    @POST(PATH)
    Call<HttpResult<UploadImageResult>> uploadImage(@Part("action") RequestBody action, @Part("imageData") RequestBody file);

    /**
     * 发布帖子
     *
     * @param action
     * @return
     */
    @FormUrlEncoded
    @POST(GATEWAY_PATH)
    Observable<HttpResult<EmptyResult>> publishArticle(@Field("action") String action, @FieldMap Map<String, String> params);

    /**
     * 帖子详情
     *
     * @param action
     * @return
     */
    @GET(GATEWAY_PATH)
    Observable<HttpResult<PostDetail>> postDetail(@Query("action") String action,  @Query("id") String postId);

    /**
     * 发布服务
     *
     * @param action
     * @return
     */
    @FormUrlEncoded
    @POST(GATEWAY_PATH)
    Call<HttpResult<ServiceDetail>> publishService(@Field("action") String action, @FieldMap Map<String, String> params);

    /**
     * 获取服务详情
     *
     * @param action
     * @return
     */
    @GET(GATEWAY_PATH)
    Observable<HttpResult<ServiceDetail>> getServiceDetail(@Query("action") String action, @Query("id") String serviceId);


    /**
     * 获取买家订单列表
     *
     * @param action
     * @return
     */
    @GET(GATEWAY_PATH)
    Observable<HttpResult<List<OrderDetail>>> getConsumerOrderList(@Query("action") String action, @QueryMap Map<String, String> params);


    /**
     * 获取订单消息列表
     *
     * @param action
     * @return
     */
    @GET(GATEWAY_PATH)
    Observable<HttpResult<List<OrderDetail>>> orderMessageList(@Query("action") String action, @Query("uid_type") String uid_type, @QueryMap Map<String, String> params);

    /**
     * 获取卖家订单列表
     *
     * @param action
     * @return
     */
    @GET(GATEWAY_PATH)
    Observable<HttpResult<List<OrderDetail>>> getMerchantOrderList(@Query("action") String action, @QueryMap Map<String, String> params);


    /**
     * 获取订单详情
     *
     * @param action
     * @return
     */
    @GET(GATEWAY_PATH)
    Observable<HttpResult<OrderDetail>> getOrderDetail(@Query("action") String action, @Query("id") String orderId);

    /**
     * 新增订单
     *
     * @param action
     * @return
     */
    @FormUrlEncoded
    @POST(PATH)
    Call<HttpResult<OrderDetail>> addOrder(@Field("action") String action, @Field("service_id") String service_id, @Field("contract") String contract, @Field("buyer_mobile") String buyer_mobile, @Field("remark") String remark);

    /**
     * 微信支付订单
     *
     * @param action
     * @return
     */
    @FormUrlEncoded
    @POST(PATH)
    Call<HttpResult<WeixinOrderResult>> payOrderWeixin(@Field("action") String action, @Field("order_id") String order_id);


    /**
     * 支付宝支付订单
     */
    @GET(PATH)
    Observable<HttpResult<AlipayOrder>> payOrderAlipay(@Query("action") String action, @Query("order_id") String orderId);

    /**
     * 卖家确认订单
     */
    @GET(PATH)
    Observable<HttpResult<EmptyResult>> orderSellerConfirmed(@Query("action") String action, @Query("order_id") String orderId);

    /**
     * 买家确认订单完成
     */
    @FormUrlEncoded
    @POST(PATH)
    Observable<HttpResult<EmptyResult>> orderBuyerConfirmDone(@Field("action") String action, @Field("order_id") String orderId, @Field("buyer_stars") String buyer_stars);
    /**
     * 买家退单
     */
    @GET(PATH)
    Observable<HttpResult<EmptyResult>> orderBuyerRefund(@Query("action") String action, @Query("order_id") String orderId);

    /******---------以上 订单--------------*******/

    /**
     * 对用户点赞
     *
     * @param action
     * @return
     */
    @FormUrlEncoded
    @POST(PATH)
    Observable<HttpResult<EmptyResult>> praiseUser(@Field("action") String action, @Field("to_uid") String userId);

    /**
     * 取消用户点赞
     *
     * @param action
     * @return
     */
    @FormUrlEncoded
    @POST(PATH)
    Observable<HttpResult<EmptyResult>> cancelPraiseUser(@Field("action") String action, @Field("to_uid") String userId);

    /**
     * 是否对用户点赞
     *
     * @param action
     * @return
     */
    @GET(PATH)
    Observable<HttpResult<EmptyResult>> isPraiseUser(@Query("action") String action, @Query("to_uid") String userId);

    /**
     * 我赞的列表
     *
     * @param action
     * @return
     */
    @GET(PATH)
    Observable<HttpResult<List<Praise>>> praiseFromMeList(@Query("action") String action, @QueryMap Map<String,String> params);

    /**
     * 赞我的列表
     *
     * @param action
     * @return
     */
    @GET(PATH)
    Observable<HttpResult<List<Praise>>> praiseMeList(@Query("action") String action, @QueryMap Map<String,String> params);



    /**
     * 用户信息
     *
     * @param action
     * @return
     */
    @GET(PATH)
    Call<HttpResult<User>> userInfo(@Query("action") String action, @Query("id") String userId);



    /**
     * 获取融云token
     *
     * @param action
     * @return
     */
    @GET(PATH)
    Observable<HttpResult<RongyunToken>> rongyunToken(@Query("action") String action, @Query("userId") String userId, @Query("name") String name, @Query("portraitUri") String portraitUri);

    /**
     * 热搜词接口
     *
     * @param action
     * @return
     */
    @GET(PATH)
    Observable<HttpResult<List<String>>> hotSearchWord(@Query("action") String action);

    /**
     * 搜索帖子或服务接口
     *
     * @param action
     * @return
     */
    @GET(PATH)
    Observable<HttpResult<List<Feed>>> searchFeed(@Query("action") String action, @Query("keyword") String keyword, @Query("type") int type, @QueryMap Map<String, String> params);

    /**
     * 搜索用户接口
     *
     * @param action
     * @return
     */
    @GET(PATH)
    Observable<HttpResult<List<User>>> searchUser(@Query("action") String action, @Query("keyword") String keyword, @QueryMap Map<String, String> params);



    /**
     * 帖子列表
     */
    @GET(PATH)
    Observable<HttpResult<List<PostDetail>>> postList(@Query("action") String action, @Query("to_uid") String uid, @QueryMap Map<String, String> params);




    /**
     * 服务列表接口
     *
     * @param action
     * @return
     */
    @GET(PATH)
    Observable<HttpResult<List<ServiceDetail>>> serviceList(@Query("action") String action, @Query("to_uid") String uid, @QueryMap Map<String, String> params);

    /**
     * 评论列表的接口
     *
     * @param action
     * @param post_type 是帖子还是服务
     * @return
     */
    @GET(PATH)
    Observable<HttpResult<List<Comment>>> commentList(@Query("action") String action, @Query("post_type") int post_type, @Query("pid") String pid, @QueryMap Map<String, String> params);

    /**
     * 评论我的列表
     *
     */
    @GET(PATH)
    Observable<HttpResult<List<Comment>>> commentToMeList(@Query("action") String action, @QueryMap Map<String, String> params);

    /**
     * 新增评论
     *
     */
    @GET(PATH)
    Observable<HttpResult<EmptyResult>> addComment(@Query("action") String action, @Query("post_type") int post_type, @Query("pid") String pid, @Query("text") String text);

    /**
     * 赞服务帖子
     *
     */
    @GET(PATH)
    Observable<HttpResult<EmptyResult>> praiseServicePost(@Query("action") String action, @Query("post_type") int post_type, @Query("pid") String pid);
    /**
     * 取消赞服务帖子
     *
     */
    @GET(PATH)
    Observable<HttpResult<EmptyResult>> cancelPraiseServicePost(@Query("action") String action, @Query("post_type") int post_type, @Query("pid") String pid);
    /**
     * 是否赞服务帖子
     *
     */
    @GET(PATH)
    Observable<HttpResult<EmptyResult>> isPraiseServicePost(@Query("action") String action, @Query("post_type") int post_type, @Query("pid") String pid);

    /**
     * 获取服务中心url
     *
     */
    @GET(PATH)
    Observable<HttpResult<HelpCenterUrlResult>> getHelpCenterUrl(@Query("action") String action);




}