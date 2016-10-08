package com.smart.cloud.fire.refreshlistview;

/**
 * Created by Administrator on 2016/7/29.
 */
public interface OnRefreshListener {
    /**
     * 下拉刷新
     */
    void onDownPullRefresh();

    /**
     * 上拉加载更多
     */
    void onLoadingMore();
}
