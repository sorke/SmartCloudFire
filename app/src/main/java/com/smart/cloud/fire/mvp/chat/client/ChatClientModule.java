package com.smart.cloud.fire.mvp.chat.client;

import com.smart.cloud.fire.global.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/11/2.
 */
@Module
public class ChatClientModule {
    private ChatClientActivity chatClientActivity;
    public ChatClientModule(ChatClientActivity chatClientActivity){
        this.chatClientActivity = chatClientActivity;
    }

    @Provides
    @ActivityScope
    ChatClientPresenter provideChatClientPresenter(){
        return new ChatClientPresenter(chatClientActivity);
    }
}
