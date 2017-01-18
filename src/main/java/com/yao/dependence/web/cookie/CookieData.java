package com.yao.dependence.web.cookie;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CookieData implements Serializable {

    private static final long serialVersionUID = -264343991749765119L;

    private long expire;

    @SerializedName("cookie")
    private Map<String, String> cookieMap;

    private transient List<Pair<String, String>> cookieList;

    public CookieData(JSONObject jsonObj) {
        initFromJsonObject(jsonObj);
    }
    public CookieData(){}

    public CookieData initFromJsonObject(JSONObject jsonObj) {
        if (jsonObj == null) return null;

        cookieList = new ArrayList<>();
        expire = jsonObj.optLong("expire", 0);

        JSONObject cookieObj = jsonObj.optJSONObject("cookies");
        if (cookieObj != null) {
            Iterator<String> it = cookieObj.keys();
            while(it.hasNext()) {
                String key = it.next();
                String cookie = cookieObj.optString(key, "");
                Pair<String, String> pair = new Pair<>(key,cookie);
                cookieList.add(pair);
            }
        }
        return this;
    }

    public List<Pair<String, String>> getCookieList() {
        return this.cookieList;
    }

    public long getExpire() {
        return this.expire;
    }


    public void setCookieList(List<Pair<String, String>> cookieList) {
        this.cookieList = cookieList;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }


    public Map<String, String> getCookieMap() {
        return cookieMap;
    }
}

