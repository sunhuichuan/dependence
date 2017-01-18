package com.yao.dependence.web.cookie;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * 管理Cookie相关的类
 * //TODO 抄永军的，写的不好，用到时再改
 */
public class PreferenceCookie {

    public static final String COOKIE_PREFS = "cookie_prefs";

    private SharedPreferences cookiePreference;

    public PreferenceCookie(Context context) {
        cookiePreference = context.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE);
    }


    public void clear() {
        cookiePreference.edit().clear().apply();
    }

    public void putString(String domain, String encodeCookie) {
        cookiePreference.edit().putString(domain,encodeCookie).apply();
    }

    public Map<String,?> getAll() {
        Map<String, ?> all = cookiePreference.getAll();
        return all;
    }

    public String getString(String cookieExpire, String defaultStr) {
        String expireTime = cookiePreference.getString(cookieExpire, defaultStr);
        return expireTime;
    }
}
