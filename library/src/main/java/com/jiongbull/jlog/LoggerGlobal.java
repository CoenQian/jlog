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

package com.jiongbull.jlog;

import com.jiongbull.jlog.receiver.LogReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志的配置.
 */
public class LoggerGlobal {

    /** 日志容器. */
    private static final Map<String, Logger> mLoggers = new HashMap<>();
    /** 定时任务是否开启. */
    private static boolean sAlarmStarted;

    /**
     * 开启定时任务.
     */
    public static void startAlarm(@NonNull Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, LogReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.cancel(alarmIntent);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
        sAlarmStarted = true;
    }

    public static Map<String, Logger> getLoggers() {
        return mLoggers;
    }

    static void addLogger(@NonNull Logger logger) {
        mLoggers.put(logger.getName(), logger);
        if (!sAlarmStarted) {
            startAlarm(logger.getContext());
        }
    }
}