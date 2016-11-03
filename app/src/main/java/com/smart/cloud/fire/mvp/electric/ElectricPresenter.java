package com.smart.cloud.fire.mvp.electric;

import com.smart.cloud.fire.base.presenter.BasePresenter;

/**
 * Created by Administrator on 2016/11/2.
 */
public class ElectricPresenter extends BasePresenter<ElectricView>{

    public ElectricPresenter(ElectricView electricView){
        attachView(electricView);
    }


}
