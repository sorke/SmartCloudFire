package com.smart.cloud.fire.global;

import java.util.List;

/**
 * Created by Administrator on 2016/11/15.
 */
public class ElectricInfo {

    /**
     * error : 获取烟感成功）
     * errorCode : 0
     */
    private String error;
    private int errorCode;
    private List<Electric> smoke;
    private List<ElectricValue> Electric;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<Electric> getSmoke() {
        return smoke;
    }

    public void setSmoke(List<Electric> smoke) {
        this.smoke = smoke;
    }

    public List<ElectricValue> getElectric() {
        return Electric;
    }

    public void setElectric(List<ElectricValue> electric) {
        Electric = electric;
    }
}
