package com.smart.cloud.fire.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.global.InitBaiduNavi;
import com.smart.cloud.fire.mvp.fragment.CollectFragment.AlarmMessageModel;
import com.smart.cloud.fire.mvp.fragment.CollectFragment.CollectFragmentPresenter;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/9/23.
 */
public class CollectFragmentAdapter extends BaseAdapter {
    private Activity mContext;
    private List<AlarmMessageModel> mNormalAlarmMessageList;
    private CollectFragmentPresenter collectFragmentPresenter;
    private String userId;
    private String privilege;

    public CollectFragmentAdapter(Activity mContext, List<AlarmMessageModel> mNormalAlarmMessageList
            , CollectFragmentPresenter collectFragmentPresenter,String userId,String privilege) {
        this.mNormalAlarmMessageList = mNormalAlarmMessageList;
        this.mContext = mContext;
        this.collectFragmentPresenter = collectFragmentPresenter;
        this.userId = userId;
        this.privilege = privilege;
    }

    @Override
    public int getCount() {
        return mNormalAlarmMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNormalAlarmMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.collect_fragment_adapter, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        final AlarmMessageModel  mNormalAlarmMessage = mNormalAlarmMessageList.get(position);
        int alarmType = mNormalAlarmMessage.getAlarmType();
        int ifDeal = mNormalAlarmMessage.getIfDealAlarm();
        holder.alarmTimeTv.setText(mNormalAlarmMessage.getAlarmTime());
        holder.smokeMacTv.setText(mNormalAlarmMessage.getName());
        holder.repeaterAddressTv.setText(mNormalAlarmMessage.getAddress());
        holder.repeaterNameTv.setText(mNormalAlarmMessage.getPlaceType());
        holder.repeaterMacTv.setText(mNormalAlarmMessage.getAreaName());
        holder.userSmokeMarkPrincipal.setText(mNormalAlarmMessage.getPrincipal1());
        holder.userSmokeMarkPhoneTv.setText(mNormalAlarmMessage.getPrincipal1Phone());
        holder.userSmokeMarkPrincipalTwo.setText(mNormalAlarmMessage.getPrincipal2());
        holder.userSmokeMarkPhoneTvTwo.setText(mNormalAlarmMessage.getPrincipal2Phone());
        if(ifDeal==0){
            holder.dealAlarmActionTv.setText("取消报警");
            holder.dealAlarmActionTv.setVisibility(View.VISIBLE);
        }else{
            holder.dealAlarmActionTv.setVisibility(View.GONE);
        }
        if(alarmType==202){
            holder.alarmMarkImage.setImageResource(R.drawable.xx_huojing);
            holder.smokeMac.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
            holder.smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
        }else{
            holder.alarmMarkImage.setImageResource(R.drawable.xx_ddy);
            holder.smokeMac.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
            holder.smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
        }
        RxView.clicks(holder.actionNowTv).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Smoke smoke = new Smoke();
                smoke.setLatitude(mNormalAlarmMessage.getLatitude());
                smoke.setLongitude(mNormalAlarmMessage.getLongitude());
                Reference<Activity> reference = new WeakReference(mContext);
                new InitBaiduNavi(reference.get(),smoke);
            }
        });
        //取消报警
        RxView.clicks(holder.dealAlarmActionTv).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                collectFragmentPresenter.dealAlarm(userId,mNormalAlarmMessage.getMac(),privilege);
            }
        });
        RxView.clicks(holder.userSmokeMarkPhoneTv).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                collectFragmentPresenter.telPhoneAction(mContext,mNormalAlarmMessage.getPrincipal1Phone());
            }
        });
        RxView.clicks(holder.userSmokeMarkPhoneTvTwo).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                collectFragmentPresenter.telPhoneAction(mContext,mNormalAlarmMessage.getPrincipal2Phone());
            }
        });
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.alarm_time_tv)
        TextView alarmTimeTv;
        @Bind(R.id.smoke_mac)
        TextView smokeMac;
        @Bind(R.id.smoke_mac_tv)
        TextView smokeMacTv;
        @Bind(R.id.alarm_mark_image)
        ImageView alarmMarkImage;
        @Bind(R.id.repeater_address_tv)
        TextView repeaterAddressTv;
        @Bind(R.id.repeater_name_tv)
        TextView repeaterNameTv;
        @Bind(R.id.repeater_mac_tv)
        TextView repeaterMacTv;
        @Bind(R.id.user_smoke_mark_principal)
        TextView userSmokeMarkPrincipal;
        @Bind(R.id.user_smoke_mark_phone_tv)
        TextView userSmokeMarkPhoneTv;
        @Bind(R.id.user_smoke_mark_principal_two)
        TextView userSmokeMarkPrincipalTwo;
        @Bind(R.id.user_smoke_mark_phone_tv_two)
        TextView userSmokeMarkPhoneTvTwo;
        @Bind(R.id.deal_alarm_action_tv)
        TextView dealAlarmActionTv;
        @Bind(R.id.action_now_tv)
        TextView actionNowTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
