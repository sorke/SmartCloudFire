package com.smart.cloud.fire.mvp.electric;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;

import com.smart.cloud.fire.adapter.ElectricActivityAdapter;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.Electric;
import com.smart.cloud.fire.mvp.LineChart.LineChartActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/11/2.
 */
public class ElectricActivity extends MvpActivity<ElectricPresenter> implements ElectricView {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_fresh_layout)
    SwipeRefreshLayout swipeFreshLayout;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    private ElectricPresenter electricPresenter;
    private ElectricActivityAdapter electricActivityAdapter;
    private Context mContext;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric);
        ButterKnife.bind(this);
        mContext = this;
        refreshListView();
    }

    private void refreshListView() {
        //设置刷新时动画的颜色，可以设置4个
        swipeFreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeFreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeFreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<Electric> electricList = testData();
        electricActivityAdapter = new ElectricActivityAdapter(mContext, electricList, electricPresenter);
        recyclerView.setAdapter(electricActivityAdapter);
        swipeFreshLayout.setRefreshing(false);
        swipeFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeFreshLayout.setRefreshing(false);
            }
        });
        electricActivityAdapter.setOnItemClickListener(new ElectricActivityAdapter.OnRecyclerViewItemClickListener(){
            @Override
            public void onItemClick(View view, Electric data){
                Intent intent = new Intent(mContext, LineChartActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected ElectricPresenter createPresenter() {
        electricPresenter = new ElectricPresenter(this);
        return electricPresenter;
    }

    private List<Electric> testData(){
        List<Electric> electricList = new ArrayList<>();
        for(int i=0;i<10;i++){
            Electric electric = new Electric("电压"+i, "220V", "230"+i+"V", "");
            electricList.add(electric);
        }
        return electricList;
    }
}
