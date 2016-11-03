package com.smart.cloud.fire.mvp.chat.common.db;


import com.smart.cloud.fire.mvp.chat.common.db.base.BaseManager;
import com.smart.cloud.fire.mvp.chat.common.greenDAOBean.RecentItem;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by Administrator on 2016/10/21.
 */
public class RecentItemDBManager extends BaseManager<RecentItem,Long> {
    @Override
    public AbstractDao<RecentItem, Long> getAbstractDao() {
        return daoSession.getRecentItemDao();
    }
}
