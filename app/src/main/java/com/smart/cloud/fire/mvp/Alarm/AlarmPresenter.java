package com.smart.cloud.fire.mvp.Alarm;

import android.content.Context;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.rxjava.RxTimeCount;
import com.smart.cloud.fire.utils.MusicManger;
import com.smart.cloud.fire.utils.Utils;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by Administrator on 2016/9/27.
 */
public class AlarmPresenter extends BasePresenter<AlarmView>{
    private boolean isAlarm;
    public AlarmPresenter(AlarmView view){
        attachView(view);
    }

    public void finishActivity(int count, final Context context){
        RxTimeCount.countdown(count)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        startMusic();
                        loadMusicAndVibrate(context);
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        mvpView.finishActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMusic();
                    }

                    @Override
                    public void onNext(Integer integer) {
                    }
                });
    }
    private void loadMusicAndVibrate(Context context) {
        MusicManger.getInstance().playAlarmMusic(context);
        new Thread() {
            public void run() {
                while (isAlarm) {
                    MusicManger.getInstance().Vibrate();
                    Utils.sleepThread(100);
                }
                MusicManger.getInstance().stopVibrate();
            }
        }.start();
    }

    public void startMusic(){
        this.isAlarm=true;
    }

    public void stopMusic(){
        this.isAlarm=false;
    }

    public void telPhone(Context mContext,String phoneNum){
       telPhoneAction(mContext,phoneNum);
    }

}
