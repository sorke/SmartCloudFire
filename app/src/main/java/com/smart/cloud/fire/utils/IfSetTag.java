package com.smart.cloud.fire.utils;

import android.app.Activity;
import android.content.Intent;

import com.igexin.sdk.PushManager;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.service.SetTagService;


/**
 * Created by Administrator on 2016/8/11.
 */
public class IfSetTag {

    public void ifSetTag(Activity activity,String userid){
        String userID = SharedPreferencesManager.getInstance().getData(activity.getApplicationContext(),
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        String setTag = SharedPreferencesManager.getInstance().getData(activity.getApplicationContext(), ConstantValues.UserInfo.SETTAG);
        if (!userID.equals(userid)||!setTag.equals("true")) {
            PushManager.getInstance().initialize(activity.getApplicationContext().getApplicationContext());
            boolean resultB = PushManager.getInstance().bindAlias(
                    activity.getApplicationContext(), userid);
//            T.showLong(activity.getApplicationContext(),resultB+"");
            if(resultB==false){
                Intent setTagIntent = new Intent(activity.getApplicationContext(),SetTagService.class);
                setTagIntent.putExtra("UserNum",userid);
                activity.getApplicationContext().startService(setTagIntent);
            }else{
                SharedPreferencesManager
                        .getInstance()
                        .putData(activity.getApplicationContext(),ConstantValues.UserInfo.SETTAG,"true");
            }
        }
    }

}
