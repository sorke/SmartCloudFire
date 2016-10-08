package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Camera;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpAreaResult;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/9/21.
 */
public class ShopInfoFragmentPresenter extends BasePresenter<ShopInfoFragmentView> {
    public ShopInfoFragmentPresenter(ShopInfoFragmentView view){
        attachView(view);
    }

    public void getAllSmoke(String userId, String privilege, String page, final List<Smoke> list, final int type){
        mvpView.showLoading();
        Observable mObservable = apiStores1.getAllSmoke(userId,privilege,page);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                int result=model.getErrorCode();
                if(result==0){
                    List<Smoke> smokeList = model.getSmoke();
                    if(type==1){
                        if(list==null||list.size()==0){
                            mvpView.getDataSuccess(smokeList);
                        }else if(list!=null&&list.size()>=20){
                            mvpView.onLoadingMore(smokeList);
                        }
                    }else{
                        List<Smoke> mSmokeList = new ArrayList<Smoke>();
                        for(Smoke smoke : smokeList){
                            int netState = smoke.getNetState();
                            if(netState==0){
                                mSmokeList.add(smoke);
                            }
                        }
                        if(mSmokeList.size()>0){
                            mvpView.getOffLineData(mSmokeList);
                        }else{
                            mvpView.getDataFail("无数据");
                        }
                    }
                }else{
                    mvpView.getDataFail("无数据");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

    public void getAllCamera(String userId, String privilege, String page, final List<Camera> list){
        mvpView.showLoading();
        Observable mObservable = apiStores1.getAllCamera(userId,privilege,page);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                int resule = model.getErrorCode();
                if(resule==0){
                    List<Camera> cameraList = model.getCamera();
                    if(list==null||list.size()==0){
                        mvpView.getAllCamera(cameraList);
                    }else if(list!=null&&list.size()>=20){
                        mvpView.getCameraOnLoadingMore(cameraList);
                    }
                }else{
                    mvpView.getDataFail("无数据");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

    //type:1表示查询商铺类型，2表示查询区域类型
    public void getPlaceTypeId(String userId, String privilege, final int type){
        Observable mObservable = null;
        if(type==1){
            mObservable= apiStores1.getPlaceTypeId(userId,privilege,"").map(new Func1<HttpError,ArrayList<Object>>() {
                @Override
                public ArrayList<Object> call(HttpError o) {
                    return o.getPlaceType();
                }
            });
        }else{
            mObservable= apiStores1.getAreaId(userId,privilege,"").map(new Func1<HttpAreaResult,ArrayList<Object>>() {
                @Override
                public ArrayList<Object> call(HttpAreaResult o) {
                    return o.getSmoke();
                }
            });
        }
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ArrayList<Object>>() {
            @Override
            public void onSuccess(ArrayList<Object> model) {
                if(type==1){
                    if(model!=null&&model.size()>0){
                        mvpView.getShopType(model);
                    }else{
                        mvpView.getShopTypeFail("无数据");
                    }
                }else{
                    if(model!=null&&model.size()>0){
                        mvpView.getAreaType(model);
                    }else{
                        mvpView.getAreaTypeFail("无数据");
                    }
                }
            }
            @Override
            public void onFailure(int code, String msg) {
                mvpView.getAreaTypeFail("网络错误");
            }
            @Override
            public void onCompleted() {
            }
        }));
    }

    public void getNeedSmoke(String userId, String privilege, String areaId, String placeTypeId, final int type){
        mvpView.showLoading();
        Observable mObservable = apiStores1.getNeedSmoke(userId,privilege,areaId,"",placeTypeId);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                if(model!=null){
                    int errorCode = model.getErrorCode();
                    if(errorCode==0){
                        List<Smoke> smokes = model.getSmoke();
                        if(type==0){
                            mvpView.getDataSuccess(smokes);
                        }else if(type==2){
                            List<Smoke> mSmokeList = new ArrayList<>();
                            for(Smoke smoke : smokes){
                                int netState = smoke.getNetState();
                                if(netState==0){
                                    mSmokeList.add(smoke);
                                }
                            }
                            if(mSmokeList.size()>0){
                                mvpView.getOffLineData(mSmokeList);
                            }else{
                                mvpView.getDataFail("无数据");
                            }
                        }
                    }else {
                        mvpView.getAreaTypeFail("无数据");
                    }
                }else{
                    mvpView.getAreaTypeFail("无数据");
                }
            }
            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail("网络错误");
            }
            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }
}
