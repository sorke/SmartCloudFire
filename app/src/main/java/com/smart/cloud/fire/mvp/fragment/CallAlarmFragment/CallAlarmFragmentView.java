package com.smart.cloud.fire.mvp.fragment.CallAlarmFragment;

/**
 * Created by Administrator on 2016/10/13.
 */
public interface CallAlarmFragmentView {
    void getCurrentTime(String time);
    void stopCountDown(String msg);
    void sendAlarmMessage(String result);
}
