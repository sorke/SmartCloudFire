package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smart.cloud.fire.adapter.ShopCameraAdapter;
import com.smart.cloud.fire.adapter.ShopSmokeAdapter;
import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Camera;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
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
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.add_fire)
    ImageView addFire;
    @Bind(R.id.search_fire)
    ImageView searchFire;
    @Bind(R.id.lost_count)
    TextView lostCount;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    private Context mContext;
    private ShopInfoFragmentPresenter mShopInfoFragmentPresenter;
    private String userID;
    private int privilege;
    private String page;
    private ShopSmokeAdapter shopSmokeAdapter;
    private boolean research = false;
    private int devType = 0;
    private List<Smoke> list;
    private List<Camera> mCameraList;
    private ShopCameraAdapter shopCameraAdapter;
    private boolean visibility = false;
    private ShopType mShopType;
    private Area mArea;
    private String areaId = "";
    private String shopTypeId = "";
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;

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
        page = "1";
        list = new ArrayList<>();
        mCameraList = new ArrayList<>();
        mvpPresenter.getAllSmoke(userID, privilege + "", page, list, 1,false);
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

    @OnClick({R.id.add_fire, R.id.area_condition, R.id.shop_type_condition, R.id.search_fire})
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.area_condition:
                if (areaCondition.ifShow()) {
                    areaCondition.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 2);
                    areaCondition.setClickable(false);
                    areaCondition.showLoading();
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
            case R.id.search_fire:
                if (!Utils.isNetworkAvailable(getActivity())) {
                    return;
                }
                if (shopTypeCondition.ifShow()) {
                    shopTypeCondition.closePopWindow();
                }
                if (areaCondition.ifShow()) {
                    areaCondition.closePopWindow();
                }
                if ((mShopType != null && mShopType.getPlaceTypeId() != null) || (mArea != null && mArea.getAreaId() != null)) {
                    lin1.setVisibility(View.GONE);
                    searchFire.setVisibility(View.GONE);
                    addFire.setVisibility(View.VISIBLE);
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
                    research = true;
                    page = "1";
                    switch (devType){
                        case 0:
                            mvpPresenter.getNeedSmoke(userID, privilege + "", areaId, shopTypeId, devType);
                            break;
                        case 1:
                            break;
                        case 2:
                            mvpPresenter.getNeedLossSmoke(userID, privilege + "", areaId, shopTypeId, "",false);
                            break;
                        default:
                            break;
                    }
                    mShopType = null;
                    mArea = null;
                } else {
                    lin1.setVisibility(View.GONE);
                    return;
                }
                break;
            default:
                break;
        }
    }

    private void refreshlistview() {
        //设置刷新时动画的颜色，可以设置4个
        swipereFreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipereFreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipereFreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                research = false;
                page = "1";
                switch (devType) {
                    case 0:
                        list.clear();
                        mvpPresenter.getAllSmoke(userID, privilege + "", page, list, 1,true);
                        break;
                    case 1:
                        mCameraList.clear();
                        mvpPresenter.getAllCamera(userID, privilege + "", page, mCameraList,true);
                        break;
                    case 2:
                        mvpPresenter.getNeedLossSmoke(userID, privilege + "", "", "", "",true);
                        break;
                    default:
                        break;
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (research) {
                    if(shopCameraAdapter!=null){
                        shopCameraAdapter.changeMoreStatus(ShopCameraAdapter.NO_DATA);
                    }
                    if(shopSmokeAdapter!=null){
                        shopSmokeAdapter.changeMoreStatus(ShopCameraAdapter.NO_DATA);
                    }
                    return;
                }
                switch (devType) {
                    case 0:
                        int count = shopSmokeAdapter.getItemCount();
                        int itemCount = lastVisibleItem+2;
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && itemCount == count) {
                            page = Integer.parseInt(page) + 1 + "";
                            mvpPresenter.getAllSmoke(userID, privilege + "", page, list, 1,true);
                        } else{
                            shopSmokeAdapter.changeMoreStatus(ShopSmokeAdapter.NO_DATA);
                        }
                        mProgressBar.setVisibility(View.GONE);
                        break;
                    case 1:
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 == shopCameraAdapter.getItemCount()) {
                            if (mCameraList != null && mCameraList.size() >= 20 && research == false) {
                                page = Integer.parseInt(page) + 1 + "";
                                mvpPresenter.getAllCamera(userID, privilege + "", page, mCameraList,true);
                            }
                        } else{
                            shopCameraAdapter.changeMoreStatus(ShopCameraAdapter.NO_DATA);
                        }
                        mProgressBar.setVisibility(View.GONE);
                        break;
                    case 2:
                        shopSmokeAdapter.changeMoreStatus(ShopSmokeAdapter.NO_MORE_DATA);
                        mProgressBar.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
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
                mvpPresenter.unsubscribe("allSmoke");
                break;
            case 1:
                mvpPresenter.unsubscribe("allCamera");
                break;
            case 2:
                mvpPresenter.unsubscribe("lostSmoke");
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
        shopSmokeAdapter = new ShopSmokeAdapter(mContext, list, mShopInfoFragmentPresenter);
        recyclerView.setAdapter(shopSmokeAdapter);
        swipereFreshLayout.setRefreshing(false);
        shopSmokeAdapter.changeMoreStatus(ShopSmokeAdapter.NO_DATA);
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext, msg);
        lostCount.setText("");
        if(shopCameraAdapter!=null){
            shopCameraAdapter.changeMoreStatus(ShopCameraAdapter.NO_DATA);
        }
       if(shopSmokeAdapter!=null){
           shopSmokeAdapter.changeMoreStatus(ShopSmokeAdapter.NO_DATA);
       }

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
        shopSmokeAdapter.changeMoreStatus(ShopSmokeAdapter.LOADING_MORE);
        shopSmokeAdapter.addMoreItem(list);
        shopSmokeAdapter.changeMoreStatus(ShopSmokeAdapter.PULLUP_LOAD_MORE);
    }

    @Override
    public void getOffLineData(List<Smoke> smokeList) {
        int count = smokeList.size();
        lostCount.setText(count + "");
        shopSmokeAdapter = new ShopSmokeAdapter(mContext, smokeList, mShopInfoFragmentPresenter);
        recyclerView.setAdapter(shopSmokeAdapter);
        swipereFreshLayout.setRefreshing(false);
        shopSmokeAdapter.changeMoreStatus(ShopSmokeAdapter.NO_DATA);
    }

    @Override
    public void getAllCamera(List<Camera> cameraList) {
        mCameraList.clear();
        mCameraList.addAll(cameraList);
        shopCameraAdapter = new ShopCameraAdapter(mContext, mCameraList, mShopInfoFragmentPresenter);
        recyclerView.setAdapter(shopCameraAdapter);
        swipereFreshLayout.setRefreshing(false);
        shopCameraAdapter.changeMoreStatus(ShopCameraAdapter.NO_DATA);
    }

    @Override
    public void getCameraOnLoadingMore(List<Camera> cameraList) {
        mCameraList.addAll(cameraList);
        shopCameraAdapter.changeMoreStatus(ShopCameraAdapter.LOADING_MORE);
        shopCameraAdapter.addMoreItem(mCameraList);
        shopCameraAdapter.changeMoreStatus(ShopCameraAdapter.PULLUP_LOAD_MORE);
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

    @Override
    public void unSubscribe(String type) {
        switch (type) {
            case "allSmoke":
                lostCount.setText("");
                research = false;
                page = "1";
                devType = 0;
                list.clear();
                mvpPresenter.getAllSmoke(userID, privilege + "", page, list, 1,false);
                break;
            case "allCamera":
                lostCount.setText("");
                research = false;
                page = "1";
                devType = 1;
                mCameraList.clear();
                mvpPresenter.getAllCamera(userID, privilege + "", page, mCameraList,false);
                break;
            case "lostSmoke":
                research = false;
                page = "1";
                devType = 2;
                mvpPresenter.getNeedLossSmoke(userID, privilege + "", "", "", "",false);
                break;
            default:
                break;
        }
    }
}
