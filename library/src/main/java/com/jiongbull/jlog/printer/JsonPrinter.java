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

package com.jiongbull.jlog.printer;

import com.jiongbull.jlog.constant.LogLevel;
import com.jiongbull.jlog.constant.LogSegment;
import com.jiongbull.jlog.util.PrinterUtils;
import com.jiongbull.jlog.util.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * JSON打印机.
 */
public class JsonPrinter implements Printer {

    /** JSON的缩进量. */
    private static final int JSON_INDENT = 4;

    @Override
    public void printConsole(@LogLevel String level, @NonNull String tag, @NonNull String message,
                             @NonNull StackTraceElement element) {
        String json;
        try {
            if (message.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(message);
                json = jsonObject.toString(JSON_INDENT);
            } else if (message.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(message);
                json = jsonArray.toString(JSON_INDENT);
            } else {
                json = message;
            }
        } catch (JSONException e) {
            json = message;
        }
        PrinterUtils.printConsole(level, tag, PrinterUtils.decorateMsgForConsole(json, element));
    }

    @Override
    public void printFile(@NonNull Context context, @LogLevel String level, @NonNull String message,
                          @NonNull StackTraceElement element, @TimeUtils.ZoneOffset long zoneOffset,
                          @NonNull String timeFmt, @NonNull String logDir, String logPrefix,
                          @LogSegment int logSegment) {
        synchronized (Printer.class) {
            PrinterUtils.printFile(context, logDir, logPrefix, logSegment, zoneOffset,
                    PrinterUtils.decorateMsgForFile(level, message, element, zoneOffset, timeFmt));
        }
    }
}