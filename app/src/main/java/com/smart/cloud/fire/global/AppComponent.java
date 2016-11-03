package com.smart.cloud.fire.global;

import android.app.Application;

import com.smart.cloud.fire.mvp.chat.common.db.ChatDBManager;
import com.smart.cloud.fire.mvp.chat.common.db.RecentItemDBManager;
import com.smart.cloud.fire.service.LocationService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/2.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    Application getApplication();
    LocationService getLocationService();
    ChatDBManager getChatDBManager();
    RecentItemDBManager getRecentItemDBManager();
}
