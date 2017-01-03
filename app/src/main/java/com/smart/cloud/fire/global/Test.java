package com.smart.cloud.fire.global;

import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */
public class Test {


    /**
     * Electric : [{"electricTime":"2016-12-23 16:40:01","electricType":6,"electricValue":[{"id":1,"value":"218.51"}]},{"electricTime":"2016-12-23 16:40:01","electricType":7,"electricValue":[{"ElectricThreshold":5,"id":1,"value":"0.000"}]}]
     * error : 获取单个电气设备成功
     * errorCode : 0
     */

    private String error;
    private int errorCode;
    /**
     * electricTime : 2016-12-23 16:40:01
     * electricType : 6
     * electricValue : [{"id":1,"value":"218.51"}]
     */

    private List<ElectricBean> Electric;

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

    public List<ElectricBean> getElectric() {
        return Electric;
    }

    public void setElectric(List<ElectricBean> Electric) {
        this.Electric = Electric;
    }

    public static class ElectricBean {
        private String electricTime;
        private int electricType;
        /**
         * id : 1
         * value : 218.51
         */

        private List<ElectricValueBean> electricValue;

        public String getElectricTime() {
            return electricTime;
        }

        public void setElectricTime(String electricTime) {
            this.electricTime = electricTime;
        }

        public int getElectricType() {
            return electricType;
        }

        public void setElectricType(int electricType) {
            this.electricType = electricType;
        }

        public List<ElectricValueBean> getElectricValue() {
            return electricValue;
        }

        public void setElectricValue(List<ElectricValueBean> electricValue) {
            this.electricValue = electricValue;
        }

        public static class ElectricValueBean {
            private int id;
            private String value;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
