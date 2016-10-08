package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment;

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

import com.smart.cloud.fire.adapter.ShopInfoCameraAdapter;
import com.smart.cloud.fire.adapter.ShopInfoFragmentAdapter;
import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Camera;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.refreshlistview.OnRefreshListener;
import com.smart.cloud.fire.refreshlistview.RefreshListView;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;
import com.smart.cloud.fire.view.TopIndicator;
import com.smart.cloud.fire.view.XCDropDownListViewMapSearch;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/9/21.
 */
public class ShopInfoFragment extends MvpFragment<ShopInfoFragmentPresenter> implements ShopInfoFragmentView, TopIndicator.OnTopIndicatorListener {
    @Bind(R.id.top_indicator)
    TopIndicator topIndicator;
    @Bind(R.id.area_condition)
    XCDropDownListViewMapSearch areaCondition;
    @Bind(R.id.shop_type_condition)
    XCDropDownListViewMapSearch shopTypeCondition;
    @Bind(R.id.lin1)
    LinearLayout lin1;
    @Bind(R.id.refreshlistview)
    RefreshListView refreshlistview;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.add_fire)
    ImageView addFire;
    @Bind(R.id.search_fire)
    ImageView searchFire;
    private Context mContext;
    private ShopInfoFragmentPresenter mShopInfoFragmentPresenter;
    private String userID;
    private int privilege;
    private String page;
    private ShopInfoFragmentAdapter shopInfoFragmentAdapter;
    private boolean research=false;
    private int devType=0;
    private List<Smoke> list;
    private List<Camera> mCameraList;
    private ShopInfoCameraAdapter mShopInfoCameraAdapter;
    private boolean visibility = false;
    private ShopType mShopType;
    private Area mArea;
    private String areaId = "";
    private String shopTypeId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_info, container, false);
        ButterKnife.bind(this, view);
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
        page="1";
        list = new ArrayList<>();
        mCameraList = new ArrayList<>();
        mvpPresenter.getAllSmoke(userID,privilege+"",page,list,1);
        topIndicator.setOnTopIndicatorListener(this);
        refreshlistview();
        addFire.setVisibility(View.VISIBLE);
        addFire.setImageResource(R.drawable.search);
        regFilter();
    }

    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("GET_AREA_ACTION");
        filter.addAction("GET_SHOP_TYPE_ACTION");
        mContext.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context,
                              Intent intent) {
            //获取商店类型
            if (intent.getAction().equals("GET_SHOP_TYPE_ACTION")) {
                mShopType = (ShopType) intent.getExtras().getSerializable("mShopType");
                if (mShopType != null && mShopType.getPlaceTypeId() != null) {
                    addFire.setVisibility(View.GONE);
                    searchFire.setVisibility(View.VISIBLE);
                }
                if (mShopType.getPlaceTypeId() == null && mArea == null) {
                    addFire.setVisibility(View.VISIBLE);
                    searchFire.setVisibility(View.GONE);
                } else if (mShopType.getPlaceTypeId() == null && mArea != null && mArea.getAreaId() == null) {
                    addFire.setVisibility(View.VISIBLE);
                    searchFire.setVisibility(View.GONE);
                }
            }
            //获取区域
            if (intent.getAction().equals("GET_AREA_ACTION")) {
                mArea = (Area) intent.getExtras().getSerializable("mArea");
                if (mArea != null && mArea.getAreaId() != null) {
                    addFire.setVisibility(View.GONE);
                    searchFire.setVisibility(View.VISIBLE);
                }
                if (mArea.getAreaId() == null && mShopType == null) {
                    addFire.setVisibility(View.VISIBLE);
                    searchFire.setVisibility(View.GONE);
                } else if (mArea.getAreaId() == null && mShopType != null && mShopType.getPlaceTypeId() == null) {
                    addFire.setVisibility(View.VISIBLE);
                    searchFire.setVisibility(View.GONE);
                }
            }
        }
    };

    @OnClick({R.id.add_fire,R.id.area_condition,R.id.shop_type_condition,R.id.search_fire})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.add_fire:
                if(visibility){
                    visibility=false;
                    lin1.setVisibility(View.GONE);
                    if(areaCondition.ifShow()){
                        areaCondition.closePopWindow();
                    }
                    if(shopTypeCondition.ifShow()){
                        shopTypeCondition.closePopWindow();
                    }
                }else{
                    visibility=true;
                    areaCondition.setEditText("");
                    shopTypeCondition.setEditText("");
                    areaCondition.setEditTextHint("区域");
                    shopTypeCondition.setEditTextHint("类型");
                    lin1.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.area_condition:
                if(areaCondition.ifShow()){
                    areaCondition.closePopWindow();
                }else{
                    mvpPresenter.getPlaceTypeId(userID,privilege+"",2);
                    areaCondition.setClickable(false);
                    areaCondition.showLoading();
                }
                break;
            case R.id.shop_type_condition:
                if(shopTypeCondition.ifShow()){
                    shopTypeCondition.closePopWindow();
                }else{
                    mvpPresenter.getPlaceTypeId(userID,privilege+"",1);
                    shopTypeCondition.setClickable(false);
                    shopTypeCondition.showLoading();
                }
                break;
            case R.id.search_fire:
                if(!Utils.isNetworkAvailable(getActivity())){
                    return;
                }
                if(shopTypeCondition.ifShow()){
                    shopTypeCondition.closePopWindow();
                }
                if(areaCondition.ifShow()){
                    areaCondition.closePopWindow();
                }
                if ((mShopType!=null&&mShopType.getPlaceTypeId() != null) || (mArea!=null&&mArea.getAreaId() != null)) {
                    lin1.setVisibility(View.GONE);
                    searchFire.setVisibility(View.GONE);
                    addFire.setVisibility(View.VISIBLE);
                    areaCondition.searchClose();
                    shopTypeCondition.searchClose();
                    visibility=false;
                    if(mArea!=null&&mArea.getAreaId()!=null){
                        areaId= mArea.getAreaId();
                    }else{
                        areaId="";
                    }
                    if(mShopType!=null&&mShopType.getPlaceTypeId()!=null){
                        shopTypeId=mShopType.getPlaceTypeId();
                    }else{
                        shopTypeId="";
                    }
                    research=true;
                    page="1";
                    mvpPresenter.getNeedSmoke(userID,privilege+"",areaId,shopTypeId,devType);
                    mShopType=null;
                    mArea=null;
                }else{
                    lin1.setVisibility(View.GONE);
                    return;
                }
                break;
            default:
                break;
        }
    }

    private void refreshlistview(){
        refreshlistview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                research = false;
                page = "1";
                switch (devType){
                    case 0:
                        list.clear();
                        mvpPresenter.getAllSmoke(userID,privilege+"",page,list,1);
                        break;
                    case 1:
                        mCameraList.clear();
                        mvpPresenter.getAllCamera(userID,privilege+"",page,mCameraList);
                        break;
                    case 2:
                        mvpPresenter.getAllSmoke(userID,privilege+"","",list,2);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onLoadingMore() {
                if(research){
                    refreshlistview.hideFooterView();
                    return;
                }
                page = Integer.parseInt(page)+1+"";
                switch (devType){
                    case 0:
                        mvpPresenter.getAllSmoke(userID,privilege+"",page,list,1);
                        break;
                    case 1:
                        mvpPresenter.getAllCamera(userID,privilege+"",page,mCameraList);
                        break;
                    case 2:
                        refreshlistview.hideFooterView();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected ShopInfoFragmentPresenter createPresenter() {
        mShopInfoFragmentPresenter = new ShopInfoFragmentPresenter(ShopInfoFragment.this);
        return mShopInfoFragmentPresenter;
    }

    @Override
    public String getFragmentName() {
        return "ShopInfoFragment";
    }

    @Override
    public void onIndicatorSelected(int index) {
        topIndicator.setTabsDisplay(mContext, index);
        switch (index) {
            case 0:
                research = false;
                page="1";
                devType=0;
                list.clear();
                mvpPresenter.getAllSmoke(userID,privilege+"",page,list,1);
                break;
            case 1:
                research = false;
                page="1";
                devType=1;
                mCameraList.clear();
                mvpPresenter.getAllCamera(userID,privilege+"",page,mCameraList);
                break;
            case 2:
                research = false;
                page="1";
                devType=2;
                mvpPresenter.getAllSmoke(userID,privilege+"","",list,2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        mContext.unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public void getDataSuccess(List<Smoke> smokeList) {
        list.clear();
        list.addAll(smokeList);
        shopInfoFragmentAdapter = new ShopInfoFragmentAdapter(mContext,list);
        refreshlistview.setAdapter(shopInfoFragmentAdapter);
        refreshlistview.hideHeaderView();
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext,msg);
        refreshlistview.hideHeaderView();
        refreshlistview.hideFooterView();
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
    public void onLoadingMore(List<Smoke> smokeList) {
        list.addAll(smokeList);
        shopInfoFragmentAdapter.notifyDataSetChanged();
        refreshlistview.hideFooterView();
    }

    @Override
    public void getOffLineData(List<Smoke> smokeList) {
        shopInfoFragmentAdapter = new ShopInfoFragmentAdapter(mContext,smokeList);
        refreshlistview.setAdapter(shopInfoFragmentAdapter);
        refreshlistview.hideHeaderView();
    }

    @Override
    public void getAllCamera(List<Camera> cameraList) {
        mCameraList.clear();
        mCameraList.addAll(cameraList);
        mShopInfoCameraAdapter = new ShopInfoCameraAdapter(mContext,mCameraList);
        refreshlistview.setAdapter(mShopInfoCameraAdapter);
        refreshlistview.hideHeaderView();
    }

    @Override
    public void getCameraOnLoadingMore(List<Camera> cameraList) {
        mCameraList.addAll(cameraList);
        mShopInfoCameraAdapter.notifyDataSetChanged();
        refreshlistview.hideFooterView();
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
}
