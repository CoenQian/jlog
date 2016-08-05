package com.jiongbull.jlog;

import org.joor.Reflect;

import android.app.Application;
import android.content.Context;

/**
 * 这里提供全局通用的信息.
 */
public class JLogGlobal {

    private static Context sContext;

    /**
     * 获取Application Context.
     *
     * @return Application Context
     */
    public static Context getContext() {
        if (sContext == null) {
            synchronized (JLogGlobal.class) {
                sContext = getSystemApp().getApplicationContext();
            }
        }
        return sContext;
    }

    private static Application getSystemApp() {
        return Reflect.on("android.app.ActivityThread")
                .call("currentActivityThread")
                .field("mInitialApplication").get();
    }
}