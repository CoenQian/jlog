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

package com.jiongbull.jlog.printer;

import com.jiongbull.jlog.constant.LogLevel;
import com.jiongbull.jlog.constant.LogSegment;
import com.jiongbull.jlog.util.PrinterUtils;
import com.jiongbull.jlog.util.TimeUtils;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * 默认打印机.
 */
public class DefaultPrinter implements Printer {

    @Override
    public void printConsole(@LogLevel String level, @NonNull String tag, @NonNull String message, @NonNull StackTraceElement element) {
        PrinterUtils.printConsole(level, tag, PrinterUtils.decorateMsgForConsole(message, element));
    }

    @Override
    public void printFile(@NonNull Context context, @LogLevel String level, String message, @NonNull StackTraceElement element,
                          @TimeUtils.ZoneOffset long zoneOffset, @NonNull String timeFmt, @NonNull String logDir, String logPrefix,
                          @LogSegment int logSegment) {
        synchronized (Printer.class) {
            PrinterUtils.printFile(context, logDir, logPrefix, logSegment, zoneOffset,
                    PrinterUtils.decorateMsgForFile(level, message, element, zoneOffset, timeFmt));
        }
    }
}