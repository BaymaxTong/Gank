package io.gank.gank.base;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by baymax on 2016/7/15.
 */
public class GankApplication extends Application{
    //数据库名字
    private String realmName = "gank.realm";
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        // Configure default configuration for Realm
        RealmConfiguration realmConfig = new RealmConfiguration
                                                .Builder(this)
                                                .name(realmName)
                                                .deleteRealmIfMigrationNeeded()
                                                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    public static Context getContext() {
        return context;
    }
}
