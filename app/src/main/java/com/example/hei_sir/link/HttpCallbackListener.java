package com.example.hei_sir.link;

/**
 * Created by liuzi on 2018/3/14.
 */

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
