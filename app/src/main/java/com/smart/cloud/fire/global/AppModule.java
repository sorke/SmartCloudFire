package com.smart.cloud.fire.global;

import android.app.Application;

import com.smart.cloud.fire.mvp.chat.common.db.ChatDBManager;
import com.smart.cloud.fire.mvp.chat.common.db.RecentItemDBManager;
import com.smart.cloud.fire.service.LocationService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/11/2.
 */
@Module
public class AppModule {
    private Application application;

    public AppModule(Application application){
        this.application=application;
    }

    @Provides
    @Singleton
    public Application provideApplication(){
        return application;
    }

    @Provides
    @Singleton
    LocationService provideLocationService(){
        return new LocationService(application);
    }

    @Provides
    @Singleton
    ChatDBManager provideChatDBManager(){
        return new ChatDBManager();
    }

    @Provides
    @Singleton
    RecentItemDBManager provideRecentDB(){
        return new RecentItemDBManager();
    }

}
