package com.smart.cloud.fire.mvp.chat.client;

import com.smart.cloud.fire.global.ActivityScope;
import com.smart.cloud.fire.mvp.chat.common.db.ChatDBManager;
import com.smart.cloud.fire.mvp.chat.common.db.RecentItemDBManager;
import com.smart.cloud.fire.service.LocationService;

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
    ChatClientPresenter provideChatClientPresenter(LocationService locationService, ChatDBManager chatDBManager, RecentItemDBManager recentItemDBManager){
        return new ChatClientPresenter(chatClientActivity,locationService,chatDBManager,recentItemDBManager);
    }
}
