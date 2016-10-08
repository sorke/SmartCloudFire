package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;
import com.smart.cloud.fire.view.NormalDialog;

import java.util.List;

import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/7/29.
 */
public class ShopInfoFragmentAdapter extends BaseAdapter {
    private Context mContext;
    private List<Smoke> listNormalSmoke;

    public ShopInfoFragmentAdapter(Context mContext, List<Smoke> listNormalSmoke){
        this.listNormalSmoke = listNormalSmoke;
        this.mContext = mContext;
    }

    class ViewHolder{
        public TextView group_tv;
        public TextView group_tv_address;
        public TextView repeater_name_tv;
        public TextView repeater_mac_tv;
        public TextView group_principal1;
        public TextView group_phone1;
        public TextView group_principal2;
        public TextView group_phone2;
        private LinearLayout category_group_lin;
        private ImageView group_image;
    }

    @Override
    public int getCount() {
        return listNormalSmoke.size();
    }

    @Override
    public Object getItem(int position) {
        return listNormalSmoke.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(null==convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.shop_info_adapter, null);
            holder = new ViewHolder();
            holder.group_tv=(TextView)convertView.findViewById(R.id.group_tv);
            holder.group_tv_address=(TextView)convertView.findViewById(R.id.group_tv_address);
            holder.repeater_name_tv=(TextView)convertView.findViewById(R.id.repeater_name_tv);
            holder.repeater_mac_tv=(TextView)convertView.findViewById(R.id.repeater_mac_tv);
            holder.group_principal1=(TextView)convertView.findViewById(R.id.group_principal1);
            holder.group_phone1=(TextView)convertView.findViewById(R.id.group_phone1);
            holder.group_principal2=(TextView)convertView.findViewById(R.id.group_principal2);
            holder.group_phone2=(TextView)convertView.findViewById(R.id.group_phone2);
            holder.category_group_lin = (LinearLayout) convertView.findViewById(R.id.category_group_lin);
            holder.group_image = (ImageView) convertView.findViewById(R.id.group_image);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        final Smoke normalSmoke = listNormalSmoke.get(position);
        int netStates = normalSmoke.getNetState();
        if(netStates==0){
            holder.category_group_lin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
            holder.group_image.setImageResource(R.drawable.yg_zj_lx);
        }else{
            holder.category_group_lin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
            holder.group_image.setImageResource(R.drawable.yg_zj_zx);
        }
        holder.group_tv_address.setText(normalSmoke.getAddress());
        holder.group_tv.setText(normalSmoke.getName());
        holder.repeater_name_tv.setText(normalSmoke.getPlaceType());
        holder.repeater_mac_tv.setText(normalSmoke.getAreaName());
        holder.group_principal1.setText(normalSmoke.getPrincipal1());
        holder.group_phone1.setText(normalSmoke.getPrincipal1Phone());
        holder.group_principal2.setText(normalSmoke.getPrincipal2());
        holder.group_phone2.setText(normalSmoke.getPrincipal2Phone());
        holder.group_phone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneOne = normalSmoke.getPrincipal1Phone();
                if (Utils.isPhoneNumber(phoneOne)) {
                    NormalDialog mNormalDialog = new NormalDialog(mContext, "是否需要拨打电话：", phoneOne,
                            "是", "否");
                    mNormalDialog.showNormalDialog();
                } else {
                    T.showShort(mContext, "电话号码不合法");
                }

            }
        });
        holder.group_phone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneTwo = normalSmoke.getPrincipal2Phone();
                if (Utils.isPhoneNumber(phoneTwo)) {
                    NormalDialog mNormalDialog = new NormalDialog(mContext, "是否需要拨打电话：", phoneTwo,
                            "是", "否");
                    mNormalDialog.showNormalDialog();
                } else {
                    T.showShort(mContext, "电话号码不合法");
                }

            }
        });
        return convertView;
    }

}
