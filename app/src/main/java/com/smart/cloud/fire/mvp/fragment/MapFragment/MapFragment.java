package com.smart.cloud.fire.mvp.fragment.MapFragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.overlayutil.MyOverlayManager;
import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.Contact;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.ui.ApMonitorActivity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.view.ShowAlarmDialog;
import com.smart.cloud.fire.view.ShowSmokeDialog;
import com.smart.cloud.fire.view.XCDropDownListViewMapSearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/9/21.
 */
public class MapFragment extends MvpFragment<MapFragmentPresenter> implements MapFragmentView {

    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.bmapView)
    MapView mMapView;
    @Bind(R.id.lin1)
    LinearLayout lin1;
    @Bind(R.id.search_fire)
    ImageView search_fire;
    @Bind(R.id.add_fire)
    ImageView add_fire;
    @Bind(R.id.area_condition)
    XCDropDownListViewMapSearch areaCondition;
    @Bind(R.id.shop_type_condition)
    XCDropDownListViewMapSearch shopTypeCondition;
    private BaiduMap mBaiduMap;
    private Context mContext;
    private String userID;
    private int privilege;
    private ShopType mShopType;
    private Area mArea;
    private String areaId = "";
    private String shopTypeId = "";
    private Smoke normalSmoke;
    private AlertDialog dialog,doAlarmdDialog;
    private MapFragmentPresenter mMapFragmentPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container,
                false);
        ButterKnife.bind(this, view);
        mBaiduMap = mMapView.getMap();// 获得MapView
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        if (privilege == 1) {
            add_fire.setVisibility(View.GONE);
        } else {
            add_fire.setVisibility(View.VISIBLE);
            add_fire.setImageResource(R.drawable.search);
        }
        mvpPresenter.getAllSmoke(userID, privilege + "");
        regFilter();
    }

    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("MAP_FRAGMENT_CLICK_MAP_POINT");
        filter.addAction("GET_AREA_ACTION");
        filter.addAction("GET_SHOP_TYPE_ACTION");
        mContext.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context,
                              Intent intent) {
            if (intent.getAction().equals("MAP_FRAGMENT_CLICK_MAP_POINT")) {
                Serializable object = intent.getExtras().getBundle("mNormalSmoke").getSerializable("mNormalSmoke");
                boolean result = object instanceof Smoke;
                if (result) {
                    normalSmoke = (Smoke) object;
                    int stutes = normalSmoke.getIfDealAlarm();
                    if (stutes == 1) {//无未处理报警信息，地图图标不闪
                        showSmokeDialog(normalSmoke);
                    } else {//有未处理报警信息，地图图标闪动
                        showAlarmDialog(normalSmoke);
                    }
                } else {
                    Camera camera = (Camera) object;
                    Contact mContact = new Contact();
                    mContact.contactType = 0;
                    mContact.contactId = camera.getCameraId();
                    mContact.contactPassword = camera.getCameraPwd();
                    mContact.contactName = camera.getCameraName();
                    mContact.apModeState = 1;
                    Intent monitor = new Intent();
                    monitor.setClass(mContext, ApMonitorActivity.class);
                    monitor.putExtra("contact", mContact);
                    monitor.putExtra("connectType", ConstantValues.ConnectType.P2PCONNECT);
                    monitor.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(monitor);
                }
            }
            //获取商店类型
            if (intent.getAction().equals("GET_SHOP_TYPE_ACTION")) {
                mShopType = (ShopType) intent.getExtras().getSerializable("mShopType");
                if (mShopType != null && mShopType.getPlaceTypeId() != null) {
                    add_fire.setVisibility(View.GONE);
                    search_fire.setVisibility(View.VISIBLE);
                }
                if (mShopType.getPlaceTypeId() == null && mArea == null) {
                    add_fire.setVisibility(View.VISIBLE);
                    search_fire.setVisibility(View.GONE);
                } else if (mShopType.getPlaceTypeId() == null && mArea != null && mArea.getAreaId() == null) {
                    add_fire.setVisibility(View.VISIBLE);
                    search_fire.setVisibility(View.GONE);
                }
            }
            //获取区域
            if (intent.getAction().equals("GET_AREA_ACTION")) {
                mArea = (Area) intent.getExtras().getSerializable("mArea");
                if (mArea != null && mArea.getAreaId() != null) {
                    add_fire.setVisibility(View.GONE);
                    search_fire.setVisibility(View.VISIBLE);
                }
                if (mArea.getAreaId() == null && mShopType == null) {
                    add_fire.setVisibility(View.VISIBLE);
                    search_fire.setVisibility(View.GONE);
                } else if (mArea.getAreaId() == null && mShopType != null && mShopType.getPlaceTypeId() == null) {
                    add_fire.setVisibility(View.VISIBLE);
                    search_fire.setVisibility(View.GONE);
                }
            }
        }
    };

    private void showSmokeDialog(Smoke mNormalSmoke) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.user_smoke_address_mark, null);
        new ShowSmokeDialog(getActivity(),view,mNormalSmoke);
    }

    private void showAlarmDialog(Smoke mNormalSmoke){
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.user_do_alarm_msg_dialog, null);
       new ShowAlarmDialog(getActivity(),view,mNormalSmoke,mMapFragmentPresenter,userID);
    }

    @Override
    protected MapFragmentPresenter createPresenter() {
        mMapFragmentPresenter = new MapFragmentPresenter(MapFragment.this);
        return mMapFragmentPresenter;
    }

    @Override
    public String getFragmentName() {
        return "Map";
    }

    @Override
    public void onDestroyView() {
        mMapView.onDestroy();
        super.onDestroyView();
        mContext.unregisterReceiver(mReceiver);
        if(shopTypeCondition!=null){
            if(shopTypeCondition.ifShow()){
                shopTypeCondition.closePopWindow();
            }
        }
        if(areaCondition!=null){
            if(areaCondition.ifShow()){
                areaCondition.closePopWindow();
            }
        }
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        mMapView.setVisibility(View.VISIBLE);
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.setVisibility(View.INVISIBLE);
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void getDataSuccess(List<Smoke> smokeList) {
        mBaiduMap.clear();
        final MyOverlayManager mMyOverlayManager = new MyOverlayManager(mBaiduMap, smokeList, MyApp.app);
        mMyOverlayManager.removeFromMap();
        mBaiduMap.setOnMarkerClickListener(mMyOverlayManager);
        mMyOverlayManager.addToMap();
        mMyOverlayManager.zoomToSpan();
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMyOverlayManager.zoomToSpan();
            }
        });
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext, msg);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void getShopType(ArrayList<Object> shopTypes) {
        shopTypeCondition.setItemsData(shopTypes);
        shopTypeCondition.showPopWindow();
        shopTypeCondition.setClickable(true);
        shopTypeCondition.closeLoading();
    }

    @Override
    public void getShopTypeFail(String msg) {
        T.showShort(mContext, msg);
        shopTypeCondition.setClickable(true);
        shopTypeCondition.closeLoading();
    }

    @Override
    public void getAreaType(ArrayList<Object> shopTypes) {
        areaCondition.setItemsData(shopTypes);
        areaCondition.showPopWindow();
        areaCondition.setClickable(true);
        areaCondition.closeLoading();
    }

    @Override
    public void getAreaTypeFail(String msg) {
        T.showShort(mContext, msg);
        areaCondition.setClickable(true);
        areaCondition.closeLoading();
    }

    private boolean visibility = false;

    @OnClick({R.id.search_fire, R.id.add_fire, R.id.area_condition, R.id.shop_type_condition})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_fire:
                if (shopTypeCondition.ifShow()) {
                    shopTypeCondition.closePopWindow();
                }
                if (areaCondition.ifShow()) {
                    areaCondition.closePopWindow();
                }
                if ((mShopType != null && mShopType.getPlaceTypeId() != null) || (mArea != null && mArea.getAreaId() != null)) {
                    lin1.setVisibility(View.GONE);
                    search_fire.setVisibility(View.GONE);
                    add_fire.setVisibility(View.VISIBLE);
                    areaCondition.searchClose();
                    shopTypeCondition.searchClose();
                    visibility = false;
                    if (mArea != null && mArea.getAreaId() != null) {
                        areaId = mArea.getAreaId();
                    } else {
                        areaId = "";
                    }
                    if (mShopType != null && mShopType.getPlaceTypeId() != null) {
                        shopTypeId = mShopType.getPlaceTypeId();
                    } else {
                        shopTypeId = "";
                    }
                    mvpPresenter.getNeedSmoke(userID, privilege + "", areaId, shopTypeId);
                }
                break;
            case R.id.add_fire:
                if (visibility) {
                    visibility = false;
                    lin1.setVisibility(View.GONE);
                    if (areaCondition.ifShow()) {
                        areaCondition.closePopWindow();
                    }
                    if (shopTypeCondition.ifShow()) {
                        shopTypeCondition.closePopWindow();
                    }
                } else {
                    visibility = true;
                    areaCondition.setEditText("");
                    shopTypeCondition.setEditText("");
                    areaCondition.setEditTextHint("区域");
                    shopTypeCondition.setEditTextHint("类型");
                    lin1.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.shop_type_condition:
                if (shopTypeCondition.ifShow()) {
                    shopTypeCondition.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 1);
                    shopTypeCondition.setClickable(false);
                    shopTypeCondition.showLoading();
                }
                break;
            case R.id.area_condition:
                if (areaCondition.ifShow()) {
                    areaCondition.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 2);
                    areaCondition.setClickable(false);
                    areaCondition.showLoading();
                }
                break;
            default:
                break;
        }
    }

}
