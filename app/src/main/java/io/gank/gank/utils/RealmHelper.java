package io.gank.gank.utils;

import android.content.Context;
import io.realm.Realm;
import io.realm.RealmResults;
import io.gank.gank.entity.Results;

/**
 * Realm数据库帮助类
 * Created by baymax on 2016/7/19.
 */
public class RealmHelper {
    //懒汉单例模式
    private static RealmHelper instance;
    public  Context mContext;
    public Realm realm;
    //数据库名字
    private String realmName = "gank.realm";

    public  RealmHelper(Context context){
        mContext = context;
    }
    //双检索单例
    public static RealmHelper getInstance(Context context){
        if(instance == null){
            synchronized (RealmHelper.class){//同步锁 保证线程安全
                if(instance == null){
                    instance = new RealmHelper(context);
                }
            }
        }
        return instance;
    }

    /**
     * @同步插入数据
     * @param results
     * @throws Exception
     */
    public void insert(Results results){
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Results res = realm.copyToRealm(results);
        realm.commitTransaction();
        realm.close();
    }

    /**
     * 获取所有的Results数据
     *
     * @return
     * @throws Exception
     */
    public RealmResults<Results> getAllResult(){
        realm = Realm.getDefaultInstance();
        RealmResults<Results> list = realm.where(Results.class)
                .findAll();
        realm.close();
        return list;
    }

    /**
     * 获取所有的Results数据
     *
     * @return
     * @throws Exception
     */
    public RealmResults<Results> getTagResult(String tag){
        realm = Realm.getDefaultInstance();
        RealmResults<Results> list = realm.where(Results.class).equalTo("type",tag)
                .findAll();
        realm.close();
        return list;
    }

    /**
     * 删除所有的Results数据
     *
     * @return
     * @throws Exception
     */
    public void deleteAllResults() throws Exception {
        realm = Realm.getDefaultInstance();
        realm.where(Results.class).findAll().deleteAllFromRealm();
        realm.close();
    }

}
