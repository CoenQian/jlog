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
import com.jiongbull.jlog.constant.LogLevel;
import com.jiongbull.jlog.constant.LogSegment;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * 原生日志工具类.
 */
public final class LogUtils {

    /** logcat里日志的最大长度. */
    private static final int MAX_LOG_LENGTH = 4000;
    /** 日志的扩展名. */
    private static final String LOG_EXT = ".log";

    private LogUtils() {
    }

    /**
     * 使用LogCat输出日志，字符长度超过4000则自动换行.
     *
     * @param level   级别
     * @param tag     标签
     * @param message 信息
     */
    public static void log(@NonNull LogLevel level, @NonNull String tag, @NonNull String message) {
        int subNum = message.length() / MAX_LOG_LENGTH;
        if (subNum > 0) {
            int index = 0;
            for (int i = 0; i < subNum; i++) {
                int lastIndex = index + MAX_LOG_LENGTH;
                String sub = message.substring(index, lastIndex);
                logSub(level, tag, sub);
                index = lastIndex;
            }
            logSub(level, tag, message.substring(index, message.length()));
        } else {
            logSub(level, tag, message);
        }
    }

    /**
     * 通过全限定类名来获取类名。
     *
     * @param className 全限定类名
     * @return 类名
     */
    public static String getSimpleClassName(@NonNull String className) {
        int lastIndex = className.lastIndexOf(".");
        int index = lastIndex + 1;
        if (lastIndex > 0 && index < className.length()) {
            return className.substring(index);
        }
        return className;
    }

    /**
     * 生成日志目录路径.
     *
     * @return 日志目录路径
     */
    public static String genDirPath() {
        return JLog.getSettings().getLogDir();
    }

    /**
     * 生成日志文件名.
     *
     * @return 日志文件名
     */
    public static String genFileName() {
        String logPrefix = JLog.getSettings().getLogPrefix();
        logPrefix = TextUtils.isEmpty(logPrefix) ? "" : logPrefix + "_";
        String curDate = TimeUtils.getCurDate();
        String fileName;
        if (JLog.getSettings().getLogSegment() == LogSegment.TWENTY_FOUR_HOURS) {
            fileName = logPrefix + curDate + LOG_EXT;
        } else {
            fileName = logPrefix + curDate + "_" + getCurSegment() + LOG_EXT;
        }
        return fileName;
    }

    /**
     * 生成崩溃日志文件名.
     *
     * @return 崩溃日志文件名
     */
    public static String genCrashFileName() {
        String logPrefix = JLog.getSettings().getLogPrefix();
        logPrefix = TextUtils.isEmpty(logPrefix) ? "" : logPrefix + "_";
        String curDate = TimeUtils.getCurDate();
        String fileName;
        if (JLog.getSettings().getLogSegment() == LogSegment.TWENTY_FOUR_HOURS) {
            fileName = logPrefix + curDate + "_crash" + LOG_EXT;
        } else {
            fileName = logPrefix + curDate + "_" + getCurSegment() + "_crash" + LOG_EXT;
        }
        return fileName;
    }

    /**
     * 根据切片时间获取当前的时间段.
     *
     * @return 比如“0001”表示00:00-01:00
     */
    public static String getCurSegment() {
        int hour = TimeUtils.getCurHour();
        LogSegment logSegment = JLog.getSettings().getLogSegment();
        int segmentValue = logSegment.getValue();
        int start = hour - hour % segmentValue;
        int end = start + segmentValue;
        if (end == 24) {
            end = 0;
        }
        return getDoubleNum(start) + getDoubleNum(end);
    }

    /**
     * 对于1-9的数值进行前置补0.
     *
     * @param num 数值
     * @return num在[0, 9]时前置补0，否则返回原值
     */
    public static String getDoubleNum(int num) {
        return num < 10 ? "0" + num : String.valueOf(num);
    }

    /**
     * 使用LogCat输出日志.
     *
     * @param level 级别
     * @param tag   标签
     * @param sub   信息
     */
    private static void logSub(@NonNull LogLevel level, @NonNull String tag, @NonNull String sub) {
        switch (level) {
            case VERBOSE:
                Log.v(tag, sub);
                break;
            case DEBUG:
                Log.d(tag, sub);
                break;
            case INFO:
                Log.i(tag, sub);
                break;
            case WARN:
                Log.w(tag, sub);
                break;
            case ERROR:
            case CRASH:
                Log.e(tag, sub);
                break;
            case WTF:
                Log.wtf(tag, sub);
                break;
            case JSON:
                Log.d(tag, sub);
            default:
                break;
        }
    }
}