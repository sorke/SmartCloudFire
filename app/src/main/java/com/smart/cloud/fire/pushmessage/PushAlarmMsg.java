package com.smart.cloud.fire.pushmessage;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/25.
 */
public class PushAlarmMsg implements Serializable{

    /**
     * address : 广州市天河区摩登广场
     * alarmType : 202
     * areaId : 越秀区
     * latitude : 23.138648
     * longitude : 113.351633
     * name : 烟感三
     * placeAddress : 广州市天河区摩登广场1楼
     * placeName : 沐足
     * principal1 : 张三一
     * principal1Phone : 12458558554
     * principal2 : 武大郎
     * principal2Phone : 12548755688
     */

    private String address;
    private int alarmType;
    private String areaId;
    private double latitude;
    private double longitude;
    private String name;
    private String placeAddress;
    private int ifDealAlarm;
    private String principal1;
    private String principal1Phone;
    private String principal2;
    private String principal2Phone;
    private String alarmTime;
    private String placeType;

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getPrincipal1() {
        return principal1;
    }

    public void setPrincipal1(String principal1) {
        this.principal1 = principal1;
    }

    public String getPrincipal1Phone() {
        return principal1Phone;
    }

    public void setPrincipal1Phone(String principal1Phone) {
        this.principal1Phone = principal1Phone;
    }

    public String getPrincipal2() {
        return principal2;
    }

    public void setPrincipal2(String principal2) {
        this.principal2 = principal2;
    }

    public String getPrincipal2Phone() {
        return principal2Phone;
    }

    public void setPrincipal2Phone(String principal2Phone) {
        this.principal2Phone = principal2Phone;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public int getIfDealAlarm() {
        return ifDealAlarm;
    }

    public void setIfDealAlarm(int ifDealAlarm) {
        this.ifDealAlarm = ifDealAlarm;
    }
}
