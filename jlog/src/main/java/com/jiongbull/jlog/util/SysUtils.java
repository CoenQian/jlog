/*
 * Copyright JiongBull 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jiongbull.jlog.util;

import com.jiongbull.jlog.JLog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * 系统相关工具类.
 */
public class SysUtils {

    private SysUtils() {
    }

    /**
     * 获取设备制造商名称.
     *
     * @return 设备制造商名称
     */
    public static String getManufacturerName() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取设备名称.
     *
     * @return 设备名称
     */
    public static String getModelName() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取产品名称.
     *
     * @return 产品名称
     */
    public static String getProductName() {
        return android.os.Build.PRODUCT;
    }

    /**
     * 获取品牌名称.
     *
     * @return 品牌名称
     */
    public static String getBrandName() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取操作系统版本号.
     *
     * @return 操作系统版本号
     */
    public static int getOsVersionCode() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取操作系统版本名.
     *
     * @return 操作系统版本名
     */
    public static String getOsVersionName() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取操作系统版本显示名.
     *
     * @return 操作系统版本显示名
     */
    public static String getOsVersionDisplayName() {
        return android.os.Build.DISPLAY;
    }

    /**
     * 获取版本号.
     *
     * @param context {@link Context}
     * @return 版本号
     */
    public static int getAppVersionCode(Context context) {
        int appVersionCode = 0;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SysUtils", e.getMessage());
        }
        return appVersionCode;
    }

    /**
     * 获取版本名.
     *
     * @param context {@link Context}
     * @return 版本名
     */
    public static String getAppVersionName(final Context context) {
        String appVersionName = "未知版本";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SysUtils", e.getMessage());
        }
        return appVersionName;
    }

    /**
     * 获取系统换行符.
     *
     * @return 系统换行符
     */
    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    /**
     * 生成系统相关的信息.
     *
     * @return 系统相关的信息
     */
    public static String genInfo() {
        String lineSeparator = getLineSeparator();
        String info = "";
        info += "应用版本号：" + getAppVersionCode(JLog.getSettings().getContext()) + lineSeparator;
        info += "应用版本名：" + getAppVersionName(JLog.getSettings().getContext()) + lineSeparator;
        info += "OS版本号：" + getOsVersionCode() + lineSeparator;
        info += "OS版本名：" + getOsVersionName() + lineSeparator;
        info += "OS显示名：" + getOsVersionDisplayName() + lineSeparator;
        info += "品牌信息：" + getBrandName() + lineSeparator;
        info += "产品信息：" + getProductName() + lineSeparator;
        info += "设备信息：" + getModelName() + lineSeparator;
        info += "制造商信息：" + getManufacturerName() + lineSeparator + lineSeparator + lineSeparator;
        return info;
    }
}