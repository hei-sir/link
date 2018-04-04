package com.example.hei_sir.link.helper;

/**
 * Created by liuzi on 2018/3/17.
 */

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
