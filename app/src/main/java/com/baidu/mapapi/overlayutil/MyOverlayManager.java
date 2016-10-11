package com.baidu.mapapi.overlayutil;

/**
 * Created by Administrator on 2016/7/28.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Camera;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;

import java.util.ArrayList;
import java.util.List;

import fire.cloud.smart.com.smartcloudfire.R;

public class MyOverlayManager extends OverlayManager {
    private static List<Smoke> mapNormalSmoke;
    private static Context context;
    private static BaiduMap baiduMap;

    public  MyOverlayManager(BaiduMap baiduMap, List<Smoke> mapNormalSmoke, Context context){
        super(baiduMap);
        this.mapNormalSmoke = mapNormalSmoke;
        this.context = context;
        this.baiduMap = baiduMap;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Bundle bundle = marker.getExtraInfo();
        Intent intent = new Intent();
        intent.putExtra("mNormalSmoke",bundle);
        intent.setAction("MAP_FRAGMENT_CLICK_MAP_POINT");
        context.sendBroadcast(intent);
        return true;
    }

    @Override
    public boolean onPolylineClick(Polyline arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<OverlayOptions> getOverlayOptions() {
        // TODO Auto-generated method stub
        List<OverlayOptions> overlayOptionses = new ArrayList<>();
        if(mapNormalSmoke!=null&&mapNormalSmoke.size()>0){
            View viewA = LayoutInflater.from(context).inflate(
                    R.layout.image_mark, null);
            View viewB = LayoutInflater.from(context).inflate(
                    R.layout.image_mark_alarm, null);
            BitmapDescriptor bdA = BitmapDescriptorFactory
                    .fromView(viewA);
            BitmapDescriptor bdC = BitmapDescriptorFactory
                    .fromView(viewB);
            ArrayList<BitmapDescriptor> giflist = new ArrayList<>();
            giflist.add(bdA);
            giflist.add(bdC);
            View view = LayoutInflater.from(context).inflate(
                    R.layout.image_test, null);
            View view2 = LayoutInflater.from(context).inflate(
                    R.layout.image_test2, null);
            BitmapDescriptor cameraImage = BitmapDescriptorFactory
                    .fromView(view);
            BitmapDescriptor cameraImage2 = BitmapDescriptorFactory
                    .fromView(view2);
            ArrayList<BitmapDescriptor> giflist2 = new ArrayList<>();
            giflist2.add(cameraImage);
            giflist2.add(cameraImage2);
            for (Smoke smoke : mapNormalSmoke) {
                Camera mCamera = smoke.getCamera();
                int alarmState = smoke.getIfDealAlarm();
                if(mCamera!=null){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mNormalSmoke",mCamera);
                    double latitude = Double.parseDouble(mCamera.getLatitude());
                    double longitude = Double.parseDouble(mCamera.getLongitude());
                    LatLng latLng = new LatLng(latitude, longitude);
                    if(alarmState==0){
                        overlayOptionses.add(new MarkerOptions().position(latLng).icons(giflist2).extraInfo(bundle)
                                .zIndex(0).period(10)
                                .animateType(MarkerOptions.MarkerAnimateType.drop));
                    }else{
                        overlayOptionses.add(new MarkerOptions().position(latLng).icon(cameraImage).extraInfo(bundle)
                                .zIndex(0).draggable(true).perspective(true)
                                .animateType(MarkerOptions.MarkerAnimateType.drop));
                    }

                }else{
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mNormalSmoke",smoke);
                    double latitude = Double.parseDouble(smoke.getLatitude());
                    double longitude = Double.parseDouble(smoke.getLongitude());
                    LatLng l = new LatLng(latitude, longitude);
                    if(alarmState==0){
                        overlayOptionses.add(new MarkerOptions().position(l).icons(giflist).extraInfo(bundle)
                                .zIndex(0).period(10)
                                .animateType(MarkerOptions.MarkerAnimateType.drop));
                    }else{
                        overlayOptionses.add(new MarkerOptions().position(l).icon(bdA).extraInfo(bundle)
                                .zIndex(0).draggable(true).perspective(true)
                                .animateType(MarkerOptions.MarkerAnimateType.drop));
                    }
                }
            }
        }
        return overlayOptionses;
    }


}

