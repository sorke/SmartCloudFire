package com.smart.cloud.fire.base.presenter;

/**
 * Created by Administrator on 2016/9/19.
 */

public interface Presenter<V> {

    void attachView(V view);

    void detachView();
}
