/*
 * Copyright 2016 JiongBull
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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * 系统相关工具.
 */
@SuppressWarnings({"unused", "WeakerAccess", "SameReturnValue"})
public class SysUtils {

    private SysUtils() {
    }

    /**
     * 获取设备制造商信息.
     *
     * @return 设备制造商信息
     */
    @NonNull
    public static String getManufacturerInfo() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取设备信息.
     *
     * @return 设备信息
     */
    @NonNull
    public static String getModelInfo() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取产品信息.
     *
     * @return 产品信息
     */
    @NonNull
    public static String getProductInfo() {
        return android.os.Build.PRODUCT;
    }

    /**
     * 获取品牌信息.
     *
     * @return 品牌信息
     */
    @NonNull
    public static String getBrandInfo() {
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
    @NonNull
    public static String getOsVersionName() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取操作系统版本显示名.
     *
     * @return 操作系统版本显示名
     */
    @NonNull
    public static String getOsVersionDisplayName() {
        return android.os.Build.DISPLAY;
    }

    /**
     * 获取版本号.
     *
     * @param context {@link Context}
     * @return 版本号
     */
    public static int getAppVersionCode(@NonNull Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取版本名.
     *
     * @param context {@link Context}
     * @return 版本名
     */
    @NonNull
    public static String getAppVersionName(@NonNull Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取系统换行符.
     *
     * @return 系统换行符
     */
    @NonNull
    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    /**
     * 获取系统语言信息.
     *
     * @return 语言信息，zh-CN，zh-TW，en-US等
     */
    @NonNull
    public static String getLanguage() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage() + "-" + locale.getCountry();
    }

    /**
     * 判断build type是否是debug.
     *
     * @param buildType debug、release等
     * @return true - debug，false - 非debug
     */
    public static boolean isDebug(@NonNull String buildType) {
        return "debug".equals(buildType);
    }
}