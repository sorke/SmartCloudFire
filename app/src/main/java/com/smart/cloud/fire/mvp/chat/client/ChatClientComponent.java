package com.smart.cloud.fire.mvp.chat.client;

import com.smart.cloud.fire.global.ActivityScope;
import com.smart.cloud.fire.global.AppComponent;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/2.
 */
@ActivityScope
@Component(modules = ChatClientModule.class,dependencies = AppComponent.class)
public interface ChatClientComponent {
        ChatClientActivity injectChatClientActivity(ChatClientActivity chatClientActivity);
}
