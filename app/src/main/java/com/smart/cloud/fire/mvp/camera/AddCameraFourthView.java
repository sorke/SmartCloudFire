package com.smart.cloud.fire.mvp.camera;

import com.baidu.location.BDLocation;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/27.
 */
public interface AddCameraFourthView {
    void getLocationData(BDLocation location);
    void showLoading();
    void hideLoading();
    void getDataFail(String msg);
    void getDataSuccess(String msg);
    void getShopType(ArrayList<Object> shopTypes);
    void getShopTypeFail(String msg);
    void getAreaType(ArrayList<Object> shopTypes);
    void getAreaTypeFail(String msg);
}
