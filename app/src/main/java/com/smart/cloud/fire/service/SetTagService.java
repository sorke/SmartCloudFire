package com.smart.cloud.fire.service;

/**
 * Created by Administrator on 2016/8/11.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.igexin.sdk.PushManager;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import java.util.Timer;
import java.util.TimerTask;

public class SetTagService extends Service{

    private Timer mTimer;
    private Context mContext;
    private String userNum;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();
        userNum = intent.getExtras().getString("UserNum");
        if(userNum!=null){
            mTimer = new Timer();
            setTimerdoAction(doActionHandler,mTimer);
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private void setTimerdoAction(final Handler oj,Timer t) {
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message = oj.obtainMessage();
                message.what = 2;
                oj.sendMessage(message);
            }
        }, 1000, 5000/*表示1000毫秒之後，每隔1000毫秒執行一次 */);
    }

    private Handler doActionHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    boolean resultB = PushManager.getInstance().bindAlias(
                            mContext, userNum);
                    if(resultB){
                        SharedPreferencesManager
                                .getInstance()
                                .putData(mContext, ConstantValues.UserInfo.SETTAG,"true");
                        mTimer.cancel();
                        stopSelf();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        mTimer.cancel();
        stopSelf();
        super.onDestroy();
    }
}

