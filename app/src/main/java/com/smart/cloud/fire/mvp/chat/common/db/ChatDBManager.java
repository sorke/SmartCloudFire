package com.smart.cloud.fire.mvp.chat.common.db;


import com.smart.cloud.fire.mvp.chat.common.db.base.BaseManager;
import com.smart.cloud.fire.mvp.chat.common.greenDAOBean.ChatBean;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by Administrator on 2016/10/21.
 */
public class ChatDBManager extends BaseManager<ChatBean,Long> {
    @Override
    public AbstractDao<ChatBean, Long> getAbstractDao() {
        return daoSession.getChatBeanDao();
    }
}
