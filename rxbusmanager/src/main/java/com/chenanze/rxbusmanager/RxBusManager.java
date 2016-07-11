package com.chenanze.rxbusmanager;

import android.support.v4.util.Pair;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * 管理 RxBus 和 Rxjava 提供了标签管理和方便的使用接口
 * Created by chenanze on 16/6/28.
 */
public class RxBusManager {

    private static RxBusManager mRxManage;

    public static final String DEFAULT_TAG = "default";

    private static String mTag;

    private static boolean isUseTag = false;

    public RxBus mRxBus = RxBus.$();
    private Map<String, List<Map<String, Pair<Subscription, Observable<?>>>>> mObservablesMap = new HashMap<>();
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public static RxBusManager getInstance() {
        if (mRxManage == null) {
            mRxManage = new RxBusManager();
        }
        isUseTag = false;
        return mRxManage;
    }

    /**
     * 设置事件标签
     *
     * @param tagName
     * @return
     */
    public RxBusManager t(String tagName) {
        mTag = tagName;
        isUseTag = true;
        return this;
    }

    public RxBusManager on(String eventName, Action1<Object> action1) {
        if (isUseTag)
            on(eventName, mTag, action1);
        else
            on(eventName, DEFAULT_TAG, action1);
        return this;
    }

    public RxBusManager on(String eventName, String tagName, Action1<Object> action1) {
        Observable<?> observable = mRxBus.register(eventName);
        Subscription subscription = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, (e) -> e.printStackTrace());
        mCompositeSubscription.add(subscription);
        List<Map<String, Pair<Subscription, Observable<?>>>> mapList = mObservablesMap.get(eventName);
        if (mapList == null) {
            mapList = new ArrayList<>();
            mObservablesMap.put(eventName, mapList);
        }
        mapList.add(registerWithTag(tagName, new Pair<>(subscription, observable)));
        return this;
    }

    public Map<String, Pair<Subscription, Observable<?>>> registerWithTag(String tagName, Pair<Subscription, Observable<?>> pair) {
        Map<String, Pair<Subscription, Observable<?>>> mTagObservablesMap = new HashMap<>();
        mTagObservablesMap.put(tagName, pair);
        return mTagObservablesMap;
    }

    public void add(Subscription m) {
        mCompositeSubscription.add(m);
    }

    public void clear() {
        mCompositeSubscription.unsubscribe();// 取消订阅
//        mCompositeSubscription.remove();
        for (Map.Entry<String, List<Map<String, Pair<Subscription, Observable<?>>>>> entry : mObservablesMap.entrySet()) {
            for (Map<String, Pair<Subscription, Observable<?>>> listItem : entry.getValue()) {
                for (Map.Entry<String, Pair<Subscription, Observable<?>>> subEntry : listItem.entrySet()) {
                    mRxBus.unregister(entry.getKey(), subEntry.getValue().second);// 移除观察
                }
            }
        }
    }

    /**
     * 取消 指定事件名,指定标签名的事件订阅
     *
     * @param eventName 事件名
     * @param tagName   标签名
     */
    public void unregister(String eventName, String tagName) {
        long startTime = System.nanoTime();
        if (mObservablesMap.get(eventName) == null) {
            Log.d("unregister", "event entry is null");
            return;
        }

        for (Map<String, Pair<Subscription, Observable<?>>> listItem : mObservablesMap.get(eventName)) {
            for (Map.Entry<String, Pair<Subscription, Observable<?>>> entry : listItem.entrySet()) {
                if (entry.getKey().equals(tagName)) {
                    mCompositeSubscription.remove(entry.getValue().first);// 取消订阅
                    mRxBus.unregister(eventName, entry.getValue().second);// 移除观察
                }
            }
        }
        long endTime = System.nanoTime();
        Log.d("time", String.valueOf(endTime - startTime) + " nm");
    }

    /**
     * 取消 指定标签名的事件订阅
     *
     * @param tagName 标签名
     */
    public void unregister(String tagName) {
        for (Map.Entry<String, List<Map<String, Pair<Subscription, Observable<?>>>>> entry :
                mObservablesMap.entrySet())
            unregister(entry.getKey(), tagName);
    }

    public <T> void postDatasWithInterval(String tag, List<T> datas, long interval) {
        new Thread(() -> {
            int i = 0;
            while (i < datas.size()) {
                try {
                    Thread.sleep(interval);
                    post(tag, datas.get(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }).start();
    }

    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }

    public void post(Object tag) {
        mRxBus.post(tag, null);
    }
}
