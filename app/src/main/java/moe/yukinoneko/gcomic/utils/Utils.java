/*
 * Copyright (C) 2016  SamuelGjk <samuel.alva@outlook.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package moe.yukinoneko.gcomic.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * Created by SamuelGjk on 2016/4/18.
 */
public class Utils {

    public static float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static String getFirstCharacter(String str) {
        return str.substring(0, 1);
    }

    public static int getNavigationBarHeight(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics dm1 = new DisplayMetrics();
        display.getMetrics(dm1);

        DisplayMetrics dm2 = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealMetrics(dm2);
        } else {
            try {
                Class c = Class.forName("android.view.Display");
                Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, dm2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return dm2.heightPixels - dm1.heightPixels;
    }

    /**
     * 判断网络是否连接
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是 wifi 连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "unknown";
    }
}
