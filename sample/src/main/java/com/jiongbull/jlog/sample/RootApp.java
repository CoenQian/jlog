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

package com.jiongbull.jlog.sample;

import com.jiongbull.jlog.JLog;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

/**
 * Root application.
 */
public class RootApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JLog.init(this)
                .writeToFile(true);
    }

    @Override
    public void onTrimMemory(int level) {
        JLog.flushCache();
        super.onTrimMemory(level);
    }
}