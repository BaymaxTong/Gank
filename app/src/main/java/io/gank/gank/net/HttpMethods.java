package io.gank.gank.net;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.gank.gank.entity.Gank;
import io.gank.gank.entity.Results;
import io.gank.gank.entity.SResults;
import io.gank.gank.entity.Search;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created baymax on 16/7/5.
 */
public class HttpMethods {

    public static final String BASE_URL = "http://gank.io/api/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private GankService gankService;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        gankService = retrofit.create(GankService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 用于获取Gank的通用数据
     * @param subscriber  由调用者传过来的观察者对象
     * @param type 起始类型
     * @param page 获取页数
     */
    public void getGankData(Subscriber<List<Results>> subscriber, String type, int page){
        Observable observable = gankService.getGankData(type, page)
                .map(new HttpResultFunc<List<Results>>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 用于获取Gank的Search数据
     * @param subscriber  由调用者传过来的观察者对象
     * @param type 起始类型
     * @param page 获取页数
     */
    public void getSearchData(Subscriber<List<SResults>> subscriber, String content, String type, int page){
        Observable observable = gankService.getSearchData(content, type, page)
                .map(new HttpResultSearch<List<SResults>>());

        toSubscribeSearch(observable, subscriber);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
         o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
//延时200ms 搜索
    private <T> void toSubscribeSearch(Observable<T> o, Subscriber<T> s){
        o.debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<Gank<T>, T>{

        @Override
        public T call(Gank<T> httpResult) {
            if (httpResult.getError().equals("true")) {
                throw new ApiException(100);
            }
            return httpResult.getResults();
        }
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultSearch<T> implements Func1<Search<T>, T>{

        @Override
        public T call(Search<T> httpResult) {
            if (httpResult.getError().equals("true")) {
                throw new ApiException(100);
            }
            if(httpResult.getCount() == 0){
                throw new ApiException(103);
            }
            return httpResult.getResults();
        }
    }

}
