package com.smart.cloud.fire.global;

/**
 * Created by Administrator on 2016/11/3.
 */
public class Electric {

    private String name;

    public Electric(String name, String values, String currentValues, String states) {
        this.name = name;
        this.values = values;
        this.currentValues = currentValues;
        this.states = states;
    }

    public Electric() {
    }

    private String values;
    private String currentValues;
    private String states;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getCurrentValues() {
        return currentValues;
    }

    public void setCurrentValues(String currentValues) {
        this.currentValues = currentValues;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }
}
