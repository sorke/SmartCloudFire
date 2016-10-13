package com.smart.cloud.fire.mvp.fragment.CallAlarmFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.utils.T;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/9/21.
 */
public class CallAlarmFragment extends MvpFragment<CallAlarmFragmentPresenter> implements CallAlarmFragmentView {
    @Bind(R.id.alarm_time)
    TextView alarmTime;
    @Bind(R.id.cancel_alarm)
    RelativeLayout cancelAlarm;
    @Bind(R.id.call_alarm_image)
    ImageView callAlarmImage;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_alarm, container,
                false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnLongClick({R.id.call_alarm_image})
    public boolean OnLongClick(View view) {
        switch (view.getId()) {
            case R.id.call_alarm_image:
                mvpPresenter.countdown(5);
                callAlarmImage.setLongClickable(false);
                break;
            default:
                break;
        }
        return true;
    }

    @OnClick({R.id.cancel_alarm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_alarm:
                mvpPresenter.stopCountDown();
                break;
            default:
                break;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
    }

    @Override
    protected CallAlarmFragmentPresenter createPresenter() {
        CallAlarmFragmentPresenter mCallAlarmFragmentPresenter = new CallAlarmFragmentPresenter(CallAlarmFragment.this);
        return mCallAlarmFragmentPresenter;
    }

    @Override
    public String getFragmentName() {
        return "CallAlarmFragment";
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void getCurrentTime(String time) {
        cancelAlarm.setVisibility(View.VISIBLE);
        alarmTime.setText(time + "(s)");
    }

    @Override
    public void stopCountDown(String msg) {
        cancelAlarm.setVisibility(View.GONE);
        T.showShort(mContext, msg);
        callAlarmImage.setLongClickable(true);
    }

    @Override
    public void sendAlarmMessage(String result) {
        cancelAlarm.setVisibility(View.GONE);
        T.showShort(mContext, result);
        callAlarmImage.setLongClickable(true);
    }

}
