package io.gank.gank.subscribers;

/**
 * Created baymax on 16/7/5.
 */
public interface SubscriberOnNextListener<T> {
    void onNext(T t);
}
