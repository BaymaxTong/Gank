package io.gank.gank.subscribers;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Random;

import io.gank.gank.progress.ProgressCancelListener;
import io.gank.gank.progress.ProgressHandler;
import io.gank.gank.utils.SnackBarUtil;
import io.gank.gank.view.BatteryView;
import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created baymax on 16/7/5.
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextListener mSubscriberOnNextListener;
    private View view;
    private BatteryView battery;
    private int mProgress = 1;
    public static final int SHOW_PROGRESS = 0x01;
    public static final int DISMISS_PROGRESS = 0x02;
    private boolean running;
    private boolean refresh;

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_PROGRESS:
                    battery.setVisibility(View.VISIBLE);
                    if (mProgress <= 10) {
                        battery.setProgress(mProgress);
                        ++mProgress;
                    }else{
                        mProgress = 1;
                        battery.setProgress(mProgress);
                    }
                    break;
                case DISMISS_PROGRESS:
                    battery.setVisibility(View.GONE);
                    break;

                default:
                    break;
            }
        }
    };

    Runnable r = new Runnable() {
        @Override
        public void run() {
            running = true;
            while (running) {
                mHandler.obtainMessage(SHOW_PROGRESS).sendToTarget();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void stop() {
        running = false;
    }

    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, Context context, View view, BatteryView battery, boolean refresh) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.view = view;
        this.battery = battery;
        this.refresh = refresh;
    }

    private void showProgressDialog(){
        if(mHandler != null){
            mHandler.obtainMessage(SHOW_PROGRESS).sendToTarget();
        }
        Thread s = new Thread(r);
        s.start();
    }

    private void dismissProgressDialog(){
        stop();
        if (mHandler != null) {
            mHandler.obtainMessage(DISMISS_PROGRESS).sendToTarget();
            mHandler = null;
        }
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        if(refresh){
            showProgressDialog();
        }
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        if(refresh) {
            dismissProgressDialog();
        }
        SnackBarUtil.ShortSnackBar(view,"干货加载好了.. ( ＞ω＜)",SnackBarUtil.COLOR_PINK,SnackBarUtil.COLOR_GREEN).show();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            SnackBarUtil.ShortSnackBar(view,"服务器开小差了... ( ＞ω＜)",SnackBarUtil.COLOR_PINK,SnackBarUtil.COLOR_GREEN).show();
        } else if (e instanceof ConnectException) {
            SnackBarUtil.ShortSnackBar(view,"服务器开小差了... ( ＞ω＜)",SnackBarUtil.COLOR_PINK,SnackBarUtil.COLOR_GREEN).show();
        } else {
            SnackBarUtil.ShortSnackBar(view,"啊嘞..出错了呢( ＞ω＜)",SnackBarUtil.COLOR_PINK,SnackBarUtil.COLOR_GREEN).show();
        }
        dismissProgressDialog();

    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}