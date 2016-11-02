package com.smart.cloud.fire.global;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/2.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    Application getApplication();
}
