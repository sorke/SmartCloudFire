package com.smart.cloud.fire.mvp.fragment.ConfireFireFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.jakewharton.rxbinding.view.RxView;
import com.obsessive.zbar.CaptureActivity;
import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.view.XCDropDownListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/9/21.
 */
public class ConfireFireFragment extends MvpFragment<ConfireFireFragmentPresenter> implements ConfireFireFragmentView {
    @Bind(R.id.add_repeater_mac)
    EditText addRepeaterMac;
    @Bind(R.id.add_fire_mac)
    EditText addFireMac;
    @Bind(R.id.add_fire_name)
    EditText addFireName;
    @Bind(R.id.add_fire_lat)
    EditText addFireLat;
    @Bind(R.id.add_fire_lon)
    EditText addFireLon;
    @Bind(R.id.add_fire_address)
    EditText addFireAddress;
    @Bind(R.id.add_fire_man)
    EditText addFireMan;
    @Bind(R.id.add_fire_man_phone)
    EditText addFireManPhone;
    @Bind(R.id.add_fire_man_two)
    EditText addFireManTwo;
    @Bind(R.id.add_fire_man_phone_two)
    EditText addFireManPhoneTwo;
    @Bind(R.id.scan_repeater_ma)
    ImageView scanRepeaterMa;
    @Bind(R.id.scan_er_wei_ma)
    ImageView scanErWeiMa;
    @Bind(R.id.location_image)
    ImageView locationImage;
    @Bind(R.id.add_fire_zjq)
    XCDropDownListView addFireZjq;
    @Bind(R.id.add_fire_type)
    XCDropDownListView addFireType;
    @Bind(R.id.add_fire_dev_btn)
    RelativeLayout addFireDevBtn;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    private Context mContext;
    private int scanType = 0;//0表示扫描中继器，1表示扫描烟感
    private int privilege;
    private String userID;
    private ShopType mShopType;
    private Area mArea;
    private String areaId = "";
    private String shopTypeId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_fire, null);
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
        regFilter();
        init();
    }

    private void init() {
        addFireZjq.setEditTextHint("区域");
        addFireType.setEditTextHint("类型");
        RxView.clicks(addFireDevBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                addFire();
            }
        });
    }

    private void addFire(){
        if(mShopType!=null){
            shopTypeId = mShopType.getPlaceTypeId();
        }
        if(mArea!=null){
            areaId = mArea.getAreaId();
        }
        String longitude = addFireLon.getText().toString().trim();
        String latitude = addFireLat.getText().toString().trim();
        String smokeName = addFireName.getText().toString().trim();
        String smokeMac = addFireMac.getText().toString().trim();
        String address = addFireAddress.getText().toString().trim();
        String placeAddress="";
        String principal1 = addFireMan.getText().toString().trim();
        String principal2 = addFireManTwo.getText().toString().trim();
        String principal1Phone = addFireManPhone.getText().toString().trim();
        String principal2Phone = addFireManPhoneTwo.getText().toString().trim();
        String repeater = addRepeaterMac.getText().toString().trim();
        mvpPresenter.addSmoke(userID,privilege+"",smokeName,smokeMac,address,longitude,
                latitude,placeAddress,shopTypeId,principal1,principal1Phone,principal2,
                principal2Phone,areaId,repeater);
    }

    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("GET_AREA_ACTION");
        filter.addAction("GET_SHOP_TYPE_ACTION");
        mContext.registerReceiver(mReceiver, filter);
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取商店类型
            if(intent.getAction().equals("GET_SHOP_TYPE_ACTION")){
                mShopType = (ShopType) intent.getExtras().getSerializable("mShopType");
            }
            //获取区域
            if(intent.getAction().equals("GET_AREA_ACTION")){
                mArea = (Area) intent.getExtras().getSerializable("mArea");
            }
        }
    };

    @Override
    protected ConfireFireFragmentPresenter createPresenter() {
        ConfireFireFragmentPresenter mConfireFireFragmentPresenter = new ConfireFireFragmentPresenter(ConfireFireFragment.this);
        return mConfireFireFragmentPresenter;
    }

    @Override
    public String getFragmentName() {
        return "ConfireFireFragment";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (addFireZjq.ifShow()) {
            addFireZjq.closePopWindow();
        }
        if (addFireType.ifShow()) {
            addFireType.closePopWindow();
        }
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        mvpPresenter.stopLocation();
        super.onDestroy();
        mContext.unregisterReceiver(mReceiver);
    }

    @Override
    public void onStart() {
        mvpPresenter.initLocation();
        super.onStart();
    }

    @OnClick({R.id.scan_repeater_ma,R.id.scan_er_wei_ma,R.id.location_image,R.id.add_fire_zjq,R.id.add_fire_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_repeater_ma:
                scanType=0;
                Intent scanRepeater = new Intent(mContext,CaptureActivity.class);
                startActivityForResult(scanRepeater, 0);
                break;
            case R.id.scan_er_wei_ma:
                scanType=1;
                Intent openCameraIntent = new Intent(mContext,CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            case R.id.location_image:
                mvpPresenter.startLocation();
                break;
            case R.id.add_fire_zjq:
                if (addFireZjq.ifShow()) {
                    addFireZjq.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 2);
                    addFireZjq.setClickable(false);
                    addFireZjq.showLoading();
                }
                break;
            case R.id.add_fire_type:
                if (addFireType.ifShow()) {
                    addFireType.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 1);
                    addFireType.setClickable(false);
                    addFireType.showLoading();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void getLocationData(BDLocation location) {
        addFireLon.setText(location.getLongitude() + "");
        addFireAddress.setText(location.getAddrStr());
        addFireLat.setText(location.getLatitude() + "");
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
    public void getDataFail(String msg) {
        T.showShort(mContext,msg);
    }

    @Override
    public void getDataSuccess(Smoke smoke) {
        addFireLon.setText(smoke.getLongitude() +"");
        addFireLat.setText(smoke.getLatitude()+"");
        addFireAddress.setText(smoke.getAddress());
        addFireName.setText(smoke.getName());
        addFireMan.setText(smoke.getPrincipal1());
        addFireManPhone.setText(smoke.getPrincipal1Phone());
        addFireManTwo.setText(smoke.getPrincipal2());
        addFireManPhoneTwo.setText(smoke.getPrincipal2Phone());
        List<String> repeaters = smoke.getRepeaters();
        if(repeaters!=null&&repeaters.size()>0){
            addRepeaterMac.setText(repeaters.get(0));
        }
    }

    @Override
    public void getShopType(ArrayList<Object> shopTypes) {
        addFireType.setItemsData(shopTypes);
        addFireType.showPopWindow();
        addFireType.setClickable(true);
        addFireType.closeLoading();
    }

    @Override
    public void getShopTypeFail(String msg) {
        T.showShort(mContext, msg);
        addFireType.setClickable(true);
        addFireType.closeLoading();
    }

    @Override
    public void getAreaType(ArrayList<Object> shopTypes) {
        addFireZjq.setItemsData(shopTypes);
        addFireZjq.showPopWindow();
        addFireZjq.setClickable(true);
        addFireZjq.closeLoading();
    }

    @Override
    public void getAreaTypeFail(String msg) {
        T.showShort(mContext, msg);
        addFireZjq.setClickable(true);
        addFireZjq.closeLoading();
    }

    @Override
    public void addSmokeResult(String msg, int errorCode) {
        T.showShort(mContext,msg);
        if(errorCode==0){
            mShopType=null;
            mArea=null;
            clearText();
            areaId = "";
            shopTypeId = "";
            addFireMac.setText("");
            addFireZjq.addFinish();
            addFireType.addFinish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            if(scanType==0){
                addRepeaterMac.setText(scanResult);
            }else{
                addFireMac.setText(scanResult);
                clearText();
                mvpPresenter.getOneSmoke(userID,privilege+"",scanResult);
            }
        }
    }

    private void clearText(){
        addFireLon.setText("");
        addFireLat.setText("");
        addFireAddress.setText("");
        addFireName.setText("");
        addFireMan.setText("");
        addFireManPhone.setText("");
        addFireManTwo.setText("");
        addFireManPhoneTwo.setText("");
        addFireZjq.setEditTextData("");
        addFireType.setEditTextData("");
    }
}
