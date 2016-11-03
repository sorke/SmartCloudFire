package com.smart.cloud.fire.mvp.chat.client;

import com.baidu.location.BDLocation;
import com.smart.cloud.fire.mvp.chat.common.greenDAOBean.ChatBean;

/**
 * Created by Administrator on 2016/11/2.
 */
public interface ChatClientView {
    void sendMsgResult(ChatBean mChatBean);
    void getLocationData(BDLocation location);
}
