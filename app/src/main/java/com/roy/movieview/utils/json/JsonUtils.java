package com.roy.movieview.utils.json;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/2/15.
 */

public class JsonUtils {

    public static <T>T Json2JavaBean(String jsonString,Class<T> clazz){
        Gson gson = new Gson();
        T t = gson.fromJson(jsonString,clazz);
        return t;
    }

    public static String JavaBean2Json(Object o){
        Gson gson = new Gson();
        return gson.toJson(o);
    }
}
