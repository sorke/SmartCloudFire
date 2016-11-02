package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Camera;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpAreaResult;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.AllDevFragment.AllDevFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.OffLineDevFragment.OffLineDevFragment;
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
    private ShopInfoFragment shopInfoFragment;
    public ShopInfoFragmentPresenter(ShopInfoFragmentView view,ShopInfoFragment shopInfoFragment){
        this.shopInfoFragment = shopInfoFragment;
        attachView(view);
    }

    public void getAllSmoke(String userId, String privilege, String page, final List<Smoke> list, final int type,boolean refresh){
        if(!refresh){
            mvpView.showLoading();
        }
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
                    }
                }else{
                    List<Smoke> mSmokeList = new ArrayList<>();
                    mvpView.getDataSuccess(mSmokeList);
                    mvpView.getDataFail("无数据");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                if(type!=1){
                    List<Smoke> mSmokeList = new ArrayList<>();
                    mvpView.getDataSuccess(mSmokeList);
                }
                mvpView.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

    public void getAllCamera(String userId, String privilege, String page, final List<Camera> list,boolean refresh){
        if(!refresh){
            mvpView.showLoading();
        }
        Observable mObservable = apiStores1.getAllCamera(userId,privilege,page);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                int resule = model.getErrorCode();
                if(resule==0){
                    List<Camera> cameraList = model.getCamera();
                    if(list==null||list.size()==0){
                        mvpView.getDataSuccess(cameraList);
                    }else if(list!=null&&list.size()>=20){
                        mvpView.onLoadingMore(cameraList);
                    }
                }else{
                    List<Camera> cameraList = new ArrayList<>();
                    mvpView.getDataSuccess(cameraList);
                    mvpView.getDataFail("无数据");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                List<Camera> cameraList = new ArrayList<>();
                mvpView.getDataSuccess(cameraList);
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
                    if(model!=null&&model.size()>0){
                        mvpView.getAreaType(model,type);
                    }else{
                        mvpView.getAreaTypeFail("无数据",type);
                    }
            }
            @Override
            public void onFailure(int code, String msg) {
                    mvpView.getAreaTypeFail("网络错误",type);
            }
            @Override
            public void onCompleted() {
            }
        }));
    }

    public void getNeedLossSmoke(String userId, String privilege, String areaId, String placeTypeId, final String page, boolean refresh, final OffLineDevFragment offLineDevFragment){
        if(!refresh){
            mvpView.showLoading();
        }
        Observable mObservable = apiStores1.getNeedLossSmoke(userId,privilege,areaId,page,placeTypeId);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                int result=model.getErrorCode();
                if(result==0){
                    List<Smoke> smokeList = model.getSmoke();
                    if(smokeList.size()>0){
                        offLineDevFragment.getDataSuccess(smokeList);
                    }
                }else{
                    List<Smoke> mSmokeList = new ArrayList<>();
                    offLineDevFragment.getDataSuccess(mSmokeList);
                    offLineDevFragment.getDataFail("无数据");
                }

            }

            @Override
            public void onFailure(int code, String msg) {
                offLineDevFragment.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

    public void getNeedSmoke(String userId, String privilege, String areaId, String placeTypeId, final AllDevFragment allDevFragment){
        mvpView.showLoading();
        Observable mObservable = apiStores1.getNeedSmoke(userId,privilege,areaId,"",placeTypeId);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                if(model!=null){
                    int errorCode = model.getErrorCode();
                    if(errorCode==0){
                        List<Smoke> smokes = model.getSmoke();
                        allDevFragment.getDataSuccess(smokes);
                    }else {
                        mvpView.getDataFail("无数据");
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

    public void unSubscribe(String type){
        mvpView.hideLoading();
        onUnsubscribe();
        mvpView.unSubscribe(type);
    }

    @Override
    public void getShop(ShopType shopType) {
        super.getShop(shopType);
        mvpView.getChoiceShop(shopType);
    }

    @Override
    public void getArea(Area area) {
        super.getArea(area);
        mvpView.getChoiceArea(area);
    }
}
