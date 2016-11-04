package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smart.cloud.fire.global.Electric;
import com.smart.cloud.fire.mvp.electric.ElectricPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class ElectricActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView

    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Electric> electricList;
    private ElectricPresenter electricPresenter;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (Electric) v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Electric data);
    }

    public ElectricActivityAdapter(Context mContext, List<Electric> electricList, ElectricPresenter electricPresenter) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.electricList = electricList;
        this.mContext = mContext;
        this.electricPresenter = electricPresenter;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        View view = mInflater.inflate(R.layout.electric_activity_adapter, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Electric electric = electricList.get(position);
        ((ItemViewHolder) holder).electricName.setText(electric.getName());
        ((ItemViewHolder) holder).electricAlarmValue.setText(electric.getValues());
        ((ItemViewHolder) holder).electricCurrentValue.setText(electric.getCurrentValues());
        ((ItemViewHolder) holder).electricStates.setText("高压报警");
        holder.itemView.setTag(electric);
    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return electricList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.electric_name)
        TextView electricName;
        @Bind(R.id.electric_alarm_value)
        TextView electricAlarmValue;
        @Bind(R.id.electric_current_value)
        TextView electricCurrentValue;
        @Bind(R.id.electric_states)
        TextView electricStates;
        @Bind(R.id.electric_lin)
        LinearLayout electricLin;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    //添加数据
    public void addItem(List<Electric> electrics) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        electrics.addAll(electricList);
        electricList.removeAll(electricList);
        electricList.addAll(electrics);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<Electric> electrics) {
        electricList.addAll(electrics);
        notifyDataSetChanged();
    }


}
