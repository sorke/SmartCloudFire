package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment;

import com.smart.cloud.fire.mvp.fragment.MapFragment.Camera;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public interface ShopInfoFragmentView {
    void getDataSuccess(List<Smoke> smokeList);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

    void onLoadingMore(List<Smoke> smokeList);

    void getOffLineData(List<Smoke> smokeList);

    void getAllCamera(List<Camera> cameraList);

    void getCameraOnLoadingMore(List<Camera> cameraList);

    void getShopType(ArrayList<Object> shopTypes);

    void getShopTypeFail(String msg);

    void getAreaType(ArrayList<Object> shopTypes);

    void getAreaTypeFail(String msg);

    void unSubscribe(String type);
}
