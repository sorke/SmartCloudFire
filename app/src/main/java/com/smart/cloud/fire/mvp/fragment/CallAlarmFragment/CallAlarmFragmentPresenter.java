package com.smart.cloud.fire.mvp.fragment.CallAlarmFragment;

import com.smart.cloud.fire.base.presenter.BasePresenter;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/10/13.
 */
public class CallAlarmFragmentPresenter extends BasePresenter<CallAlarmFragmentView> {
    private  Subscription mSubscription;

    public CallAlarmFragmentPresenter(CallAlarmFragmentView view){
        attachView(view);
    }

    public void countdown(int time){
        if (time < 0) time = 0;
        final int countTime = time;
        Observable<Integer> mObservable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1);
        mSubscription = mObservable.doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mvpView.getCurrentTime(countTime+"");
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        mvpView.sendAlarmMessage("已发送报警消息");
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(Integer integer) {
                        mvpView.getCurrentTime(integer.toString());
                    }
                });
    }

    public void stopCountDown(){
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
            mSubscription=null;
            mvpView.stopCountDown("已取消报警");
        }
    }
}
