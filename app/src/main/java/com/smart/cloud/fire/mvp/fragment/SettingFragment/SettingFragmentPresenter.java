package com.smart.cloud.fire.mvp.fragment.SettingFragment;

import android.content.Context;
import android.os.AsyncTask;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.MainThread;

/**
 * Created by Administrator on 2016/9/21.
 */
public class SettingFragmentPresenter extends BasePresenter<SettingFragmentView>{
    public SettingFragmentPresenter(SettingFragmentView view) {
        attachView(view);
    }

    public void checkUpdate(final Context mContext){
        mvpView.showLoading();
        new MyTast().execute(mContext);
    }

    class MyTast extends AsyncTask<Context, Integer, Integer> {

        @Override
        protected Integer doInBackground(Context... params) {
            // TODO Auto-generated method stub\
            Context context = params[0];
            long ll = -1;
            int result = new MainThread(context).checkUpdate(ll);
            return result;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            mvpView.hideLoading();
        }
    }
}
