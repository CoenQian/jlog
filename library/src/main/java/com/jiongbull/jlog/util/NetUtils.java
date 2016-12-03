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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

/**
 * 网络相关.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class NetUtils {

    /**
     * 检测当前网络是否连接.
     *
     * @param context Context
     * @return true - 当前网络已连接, false - 当前网络未连接
     */
    public static boolean isNetConnected(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * 检测wifi是否连接.
     *
     * @param context Context
     * @return true - 当前wifi可用, false - 当前wifi不可用
     */
    public static boolean isWifiConnected(@NonNull Context context) {
        return isNetConnected(context, ConnectivityManager.TYPE_WIFI);
    }

    /**
     * 检测ethernet是否连接.
     *
     * @param context Context
     * @return true - 当前wifi可用, false - 当前wifi不可用
     */
    public static boolean isEthernetConnected(@NonNull Context context) {
        return isNetConnected(context, ConnectivityManager.TYPE_ETHERNET);
    }

    /**
     * 检测移动网络是否连接.
     *
     * @param context Context
     * @return true - 当前wifi可用, false - 当前wifi不可用
     */
    public static boolean isMobileConnected(@NonNull Context context) {
        return isNetConnected(context, ConnectivityManager.TYPE_MOBILE);
    }

    @SuppressWarnings("deprecation")
    private static boolean isNetConnected(@NonNull Context context, int networkType) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(networkType);
        return networkInfo != null && networkInfo.isConnected();
    }
}