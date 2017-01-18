package com.yao.dependence.web.cookie;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;


import com.yao.devsdk.SdkConfig;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class CookieTask {

    private static final String COOKIE_EXPIRE = "cookie_expire";

    private static CookieTask sInstance;
    private PreferenceCookie preferenceCookie;
    private Context mContext;

    private List<LoadCookieListener> mListeners = new ArrayList<>();

    private CookieTask(Context ctx) {
        this.mContext = ctx.getApplicationContext();
        preferenceCookie = new PreferenceCookie(mContext);
    }

    public static CookieTask getInstance(Context ctx) {
        if (sInstance == null) {
            synchronized (CookieTask.class) {
                if (sInstance == null) {
                    sInstance = new CookieTask(ctx);
                }
            }
        }
        return sInstance;
    }

    public void saveCookieData(CookieData cookieData) {
        if (cookieData == null
                || cookieData.getCookieList() == null
                || cookieData.getCookieList().isEmpty()) {
            return;
        }
        preferenceCookie.clear();
        for (Pair<String, String> pair : cookieData.getCookieList()) {
            String domain = pair.first;
            String cookie = pair.second;
            if (!TextUtils.isEmpty(domain)
                    && !TextUtils.isEmpty(cookie)) {
                String encodeCookie;
                try {
                    encodeCookie = URLEncoder.encode(cookie, "UTF-8");
                    if (!TextUtils.isEmpty(encodeCookie)) {
                        preferenceCookie.putString(domain, encodeCookie);
                    }
                } catch (UnsupportedEncodingException e) {
                }
            }
        }

        long expire = cookieData.getExpire();
        preferenceCookie.putString(COOKIE_EXPIRE, String.valueOf(expire));
    }

    public CookieData loadCookieFromCache() {
        CookieData cookieData = new CookieData();
        List<Pair<String, String>> cookieList = new ArrayList<>();

        Map<String, ?> cookieMap = preferenceCookie.getAll();
        if(!SdkConfig.isLogin()){
            preferenceCookie.clear();
            removeCookies();
            return null;
        }
        if (cookieMap != null && !cookieMap.isEmpty()) {
            Set<String> keySet = cookieMap.keySet();
            if (keySet != null && !keySet.isEmpty()) {
                for (String key : keySet) {

                    String domain = key;
                    Object cookieObj = cookieMap.get(key);
                    if (cookieObj == null || !(cookieObj instanceof String)) {
                        continue;
                    }
                    String cookie = (String)cookieObj;
                    if (TextUtils.isEmpty(cookie)) {
                        continue;
                    }
                    if(COOKIE_EXPIRE.equals(key)){
                        long expire = parseLongSafely(cookie);
                        cookieData.setExpire(expire);
                        continue;
                    }

                    String decodeCookie;
                    try {
                        decodeCookie = URLDecoder.decode(cookie, "UTF-8");
                        if (!TextUtils.isEmpty(decodeCookie)) {
                            Pair<String, String> pair = new Pair<>(domain, decodeCookie);
                            cookieList.add(pair);
                        }
                    } catch (UnsupportedEncodingException e) {
                    }
                }
            }
        }
        cookieData.setCookieList(cookieList);
        return cookieData;
    }

    public long getExpireFromCache(){
        String  longStr = preferenceCookie.getString(COOKIE_EXPIRE, null);

        return parseLongSafely(longStr);
    }

    private long parseLongSafely(String longStr){
        long expire = 0;

        if(TextUtils.isEmpty(longStr)){
            return expire;
        }

        try{
            expire = Long.parseLong(longStr);
        }catch(NumberFormatException e){
        }
        return expire;
    }

    public void loadCookieFromNet(boolean refresh){

        if(SdkConfig.isLogin()){
            if(refresh){
                new LoadCookieTask().execute();
                return;
            }
            long expire = getExpireFromCache();
            if(isExpire(expire)){
                new LoadCookieTask().execute();
            }
        }
    }

    private boolean isExpire(long timeSecond){
        long now = System.currentTimeMillis();
        long newTime = timeSecond*1000l;
        if(now > newTime){
            return true;
        }

        return true;
    }

    public boolean isCookieExpired(){

        long timeSecond = getExpireFromCache();
        return isExpire(timeSecond);
    }

    public void setAndSyncCookie(){
        setCookie();
        CookieSyncManager.getInstance().sync();
    }

    public void setCookie() {
        CookieData cookieData = loadCookieFromCache();
        if (cookieData == null || cookieData.getCookieList().isEmpty()) {
            return;
        }
        CookieManager cookieManager = CookieManager.getInstance();
        for (Pair<String, String> pair : cookieData.getCookieList()) {
            String domain = pair.first;
            String cookie = pair.second;
            try {
                JSONObject cookieObj=new JSONObject(cookie);
                String sub=cookieObj.optString("SUB");
                String subp=cookieObj.optString("SUBP");
                if(!TextUtils.isEmpty(sub)) {
                    setCookie(cookieManager, domain, "SUB="+sub, cookieData.getExpire());
                }
                if(!TextUtils.isEmpty(subp)) {
                    setCookie(cookieManager, domain, "SUBP="+subp, cookieData.getExpire());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void setCookie(CookieManager cookieManager, String domain, String cookie, long expire){

        String newCookie = appendExpire(cookie,expire);
        if(!TextUtils.isEmpty(newCookie)){
            cookie = newCookie;
        }

        cookieManager.setCookie(domain, cookie);
    }

    private String appendExpire(String cookieString,long expire){

        if(TextUtils.isEmpty(cookieString)){
            return null;
        }

        if(expire <= 0){
            return null;
        }

        if(cookieString.contains("expires=")){
            return null;
        }

        String httpDate = covertCookieExpire( expire);

        StringBuilder sb = new StringBuilder();
        sb.append(cookieString).append("; ")
                .append("expires=").append(httpDate);

        return sb.toString();
    }

    private String covertCookieExpire(long expire){
        Date d = new Date(expire*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss 'GMT'",Locale.US);
        return sdf.format(d);
    }

    public void removeCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    public void removeCookiesOutOfBrowser() {
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    public void setLoadCookieListener(LoadCookieListener listener){
        mListeners.add(listener);
    }

    public void removeLoadCookieListener(LoadCookieListener listener){
        mListeners.remove(listener);
    }

    private class LoadCookieTask extends AsyncTask<String, Void, CookieData> {

        protected CookieData doInBackground( String... args ) {
            Map<String,String> params=new HashMap<>();
            params.put("token", SdkConfig.token);
            //TODO 通过网络请求获取cookie数据
            CookieData cookieData = null;
//            cookieData = NetRequestController.getInstance().getCookie(params);

            return cookieData;
        }

        @Override
        protected void onPostExecute( CookieData result ) {
            if(result != null){
                saveCookieData(result);
                notifyLoadSuccess(result);
            }else{
                notifyLoadError();
            }
        }

        @Override
        protected void onCancelled() {
            notifyLoadCancelled();
        }
    }

    private void notifyLoadSuccess(CookieData data){
        for (LoadCookieListener listener : mListeners) {
            listener.onLoadSuccess(data);
        }
    }

    private void notifyLoadError(){
        for (LoadCookieListener listener : mListeners) {
            listener.onLoadError();
        }
    }

    private void notifyLoadCancelled(){
        for (LoadCookieListener listener : mListeners) {
            listener.onLoadCancelled();
        }
    }

    public interface LoadCookieListener{
         void onLoadSuccess(CookieData data);
         void onLoadError();
         void onLoadCancelled();
    }
}

