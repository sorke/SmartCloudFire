package com.smart.cloud.fire.mvp.chat.client;

import com.smart.cloud.fire.base.presenter.BasePresenter;

/**
 * Created by Administrator on 2016/11/2.
 */
public class ChatClientPresenter extends BasePresenter<ChatClientView>{

    public ChatClientPresenter(ChatClientView view){
        attachView(view);
    }

    public void to(){
        mvpView.T("jksdfkajkhdfjka");
    }
}
