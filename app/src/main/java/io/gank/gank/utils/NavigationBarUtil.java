package io.gank.gank.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

import java.lang.reflect.Method;

/**
 * Created by baymax on 2016/8/1.
 */
public class NavigationBarUtil {
    // 判断设备是否有返回键、菜单键来确定是否有 NavigationBar
    public static boolean hasNavigationBar(Context context) {
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey) {// && !hasBackKey
            return true;
        }
        return false;
    }

    // 获取 NavigationBar 的高度
    public static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
