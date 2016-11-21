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
import com.jiongbull.jlog.util.PrinterUtils;

/**
 * 默认打印机.
 */
public class DefaultPrinter implements Printer {

    @Override
    public void printConsole(LogLevel level, String tag, String message, StackTraceElement element) {
        PrinterUtils.printConsole(level, tag, PrinterUtils.decorateMsgForConsole(message, element));
    }

    @Override
    public void printFile(LogLevel level, String tag, String message, StackTraceElement element) {
        synchronized (Printer.class) {
            if (level != LogLevel.CRASH) {
                PrinterUtils.printFile(PrinterUtils.decorateMsgForFile(level, message, element));
            } else {
                PrinterUtils.printCrashFile(PrinterUtils.decorateMsgForFile(level, message, element));
            }
        }
    }
}