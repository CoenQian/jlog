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

package com.jiongbull.jlog.service;

import com.jiongbull.jlog.IStorage;
import com.jiongbull.jlog.Logger;
import com.jiongbull.jlog.LoggerGlobal;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Map;
import java.util.Set;

/**
 * 执行日志任务的服务.
 */
public class LogService extends IntentService {

    public LogService() {
        super("LogService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Map<String, Logger> loggersMap = LoggerGlobal.getLoggers();
        Set<Map.Entry<String, Logger>> loggersSet = loggersMap.entrySet();
        for (Map.Entry<String, Logger> entry : loggersSet) {
            Logger logger = entry.getValue();
            IStorage storage = logger.getStorage();
            if (storage != null) {
                storage.upload(logger);
            }
        }
        WakefulBroadcastReceiver.completeWakefulIntent(intent); // 释放锁
    }
}