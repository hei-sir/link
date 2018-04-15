package com.example.hei_sir.link.helper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzi on 2018/3/19.
 */

public class GsonTools {
    public  static String ip="192.168.0.8";
    private static Gson mGson = new Gson();

    /**
     * 将json字符串转化成实体对象
     * @param json
     * @param classOfT
     * @return
     */
    public static Object stringToObject( String json , Class classOfT){
        return  mGson.fromJson( json , classOfT ) ;
    }

    /**
     * 将对象准换为json字符串 或者 把list 转化成json
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String objectToString(T object) {
        return mGson.toJson(object);
    }

    /**
     * 把json 字符串转化成list
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> stringToList(String json , Class<T> cls  ){
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            list.add(gson.fromJson(elem, cls));
        }
        return list ;
    }
    public static <T> T getPerson(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return t;
    }
}
