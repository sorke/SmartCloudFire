package com.smart.cloud.fire.mvp.chat.client;

import android.os.Bundle;

import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.AppComponent;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.utils.T;

import javax.inject.Inject;

import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/11/2.
 */
public class ChatClientActivity extends MvpActivity<ChatClientPresenter> implements ChatClientView{
    @Inject
    ChatClientPresenter chatClientPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        setupActivityComponent(MyApp.get(this).getAppComponent());
        chatClientPresenter.to();
    }

    protected  void setupActivityComponent(AppComponent appComponent){
        DaggerChatClientComponent.builder()
                .appComponent(appComponent)
                .chatClientModule(new ChatClientModule(this))
                .build()
                .injectChatClientActivity(this);
    }

    @Override
    protected ChatClientPresenter createPresenter() {
        return null;
    }

    @Override
    public void T(String msg) {
        T.showShort(this,msg);
    }
}
