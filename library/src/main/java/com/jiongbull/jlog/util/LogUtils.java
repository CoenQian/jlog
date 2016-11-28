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

import com.jiongbull.jlog.R;
import com.jiongbull.jlog.constant.LogLevel;
import com.jiongbull.jlog.constant.LogSegment;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

import static com.jiongbull.jlog.constant.LogLevel.DEBUG;
import static com.jiongbull.jlog.constant.LogLevel.ERROR;
import static com.jiongbull.jlog.constant.LogLevel.INFO;
import static com.jiongbull.jlog.constant.LogLevel.JSON;
import static com.jiongbull.jlog.constant.LogLevel.VERBOSE;
import static com.jiongbull.jlog.constant.LogLevel.WARN;
import static com.jiongbull.jlog.constant.LogLevel.WTF;
import static com.jiongbull.jlog.util.FileUtils.ZIP_EXT;

/**
 * 原生日志工具类.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class LogUtils {
    private static final String TAG = "log";
    /** logcat里日志的最大长度. */
    private static final int MAX_LOG_LENGTH = 4000;
    /** 日志的扩展名. */
    private static final String LOG_EXT = ".log";
    /** 读写文件的线程池，单线程模型. */
    private static final ExecutorService sExecutorService;

    static {
        sExecutorService = Executors.newSingleThreadExecutor();
    }

    private LogUtils() {
    }

    /**
     * 使用LogCat输出日志，字符长度超过4000则自动换行.
     *
     * @param level   级别
     * @param tag     标签
     * @param message 信息
     */
    public static void log(@LogLevel String level, @NonNull String tag, @NonNull String message) {
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
     * @param logDir 日志保存的目录
     * @return 日志目录路径
     */
    public static String genDirPath(@NonNull String logDir) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + logDir;
    }

    /**
     * 生成日志文件名.
     *
     * @param logPrefix  日志前缀
     * @param logSegment 日志切片
     * @param zoneOffset 时区偏移
     * @return 日志文件名
     */
    public static String genFileName(String logPrefix, @LogSegment int logSegment, @TimeUtils.ZoneOffset long zoneOffset) {
        logPrefix = TextUtils.isEmpty(logPrefix) ? "" : logPrefix + "_";
        String curDate = TimeUtils.getCurDate(zoneOffset);
        String fileName;
        if (logSegment == LogSegment.TWENTY_FOUR_HOURS) {
            fileName = logPrefix + curDate + LOG_EXT;
        } else {
            fileName = logPrefix + curDate + "_" + getCurSegment(logSegment, zoneOffset) + LOG_EXT;
        }
        return fileName;
    }

    /**
     * 根据切片时间获取当前的时间段.
     *
     * @param logSegment 日志切片
     * @param zoneOffset 时区偏移
     * @return 比如“0001”表示00:00-01:00
     */
    public static String getCurSegment(@LogSegment int logSegment, @TimeUtils.ZoneOffset long zoneOffset) {
        int hour = TimeUtils.getCurHour(zoneOffset);
        int start = hour - hour % logSegment;
        int end = start + logSegment;
        if (end == 24) {
            end = 0;
        }
        return getDoubleNum(start) + getDoubleNum(end);
    }

    /**
     * 获取日录下的所有日志文件.
     *
     * @param logDir 日志目录
     * @return 日志文件数组
     */
    public static File[] getLogFiles(@NonNull File logDir) {
        FilenameFilter logFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(LOG_EXT);
            }
        };
        return logDir.listFiles(logFilter);
    }

    /**
     * 压缩日志文件.
     *
     * @param file 日志文件，以".log"结尾
     */
    public static void zipLogs(@NonNull File file) throws IOException {
        String filePath = file.getAbsolutePath();
        int index = filePath.lastIndexOf('.');
        if (index == -1) {
            return;
        }
        String destPath = filePath.substring(0, index) + ZIP_EXT;
        FileUtils.zip(filePath, destPath, true);
    }

    /**
     * 根据当前日志的时间切片来过滤日志文件.
     *
     * @param logFiles   所有的日志文件
     * @param zoneOffset 时区
     * @param logPrefix  前缀
     * @param logSegment 日志切片
     * @return 当前时间切片之前的日志
     */
    public static File[] filterLogFiles(@NonNull File[] logFiles, @TimeUtils.ZoneOffset long zoneOffset,
                                        String logPrefix, @LogSegment int logSegment) {
        logPrefix = TextUtils.isEmpty(logPrefix) ? "" : logPrefix + "_";
        String curDate = TimeUtils.getCurDate(zoneOffset);
        String referFileName;
        if (logSegment == LogSegment.TWENTY_FOUR_HOURS) {
            referFileName = logPrefix + curDate + LOG_EXT;
        } else {
            referFileName = logPrefix + curDate + "_" + LogUtils.getCurSegment(logSegment, zoneOffset) + LOG_EXT;
        }

        List<File> files = new ArrayList<>();
        for (File logFile : logFiles) {
            if (logFile.getName().compareTo(referFileName) < 0) {
                files.add(logFile);
            }
        }
        return files.toArray(new File[files.size()]);
    }

    /**
     * 对于1-9的数值进行前置补0.
     *
     * @param num 数值
     * @return num在[0, 9]时前置补0，否则返回原值
     */
    private static String getDoubleNum(int num) {
        return num < 10 ? "0" + num : String.valueOf(num);
    }

    /**
     * 使用LogCat输出日志.
     *
     * @param level 级别
     * @param tag   标签
     * @param sub   信息
     */
    private static void logSub(@LogLevel String level, @NonNull String tag, @NonNull String sub) {
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
            case JSON:
                Log.i(tag, sub);
                break;
            case WARN:
                Log.w(tag, sub);
                break;
            case ERROR:
                Log.e(tag, sub);
                break;
            case WTF:
                Log.wtf(tag, sub);
                break;
            default:
                break;
        }
    }

    /**
     * 把文本写入文件中.
     *
     * @param context  Context
     * @param dirPath  目录路径
     * @param fileName 文件名
     * @param content  待写内容
     */
    public static void write(@NonNull final Context context, @NonNull final String dirPath, @NonNull final String fileName,
                              @NonNull final String content) {
        sExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                String filePath = dirPath + File.separator + fileName;
                BufferedSink bufferedSink = null;
                try {
                    if (FileUtils.createDir(dirPath)) {
                        String outContent = content;
                        if (!FileUtils.isExist(dirPath + File.separator + fileName)) {
                            outContent = genInfo(context) + outContent;
                        }
                        File file = new File(filePath);
                        Sink sink = Okio.appendingSink(file);
                        bufferedSink = Okio.buffer(sink);
                        bufferedSink.writeUtf8(outContent);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "写日志异常", e);
                } finally {
                    IOUtils.closeQuietly(bufferedSink);
                }
            }
        });
    }

    /**
     * 生成系统相关的信息.
     *
     * @param context Context
     * @return 系统相关的信息
     */
    private static String genInfo(@NonNull Context context) {
        String lineSeparator = SysUtils.getLineSeparator();
        String info = "";
        info += context.getString(R.string.app_version_name) + ": " + SysUtils.getAppVersionName(context) + lineSeparator;
        info += context.getString(R.string.app_version_code) + ": " + SysUtils.getAppVersionCode(context) + lineSeparator;
        info += context.getString(R.string.os_version_name) + ": " + SysUtils.getOsVersionName() + lineSeparator;
        info += context.getString(R.string.os_version_code) + ": " + SysUtils.getOsVersionCode() + lineSeparator;
        info += context.getString(R.string.os_display_name) + ": " + SysUtils.getOsVersionDisplayName() + lineSeparator;
        info += context.getString(R.string.brand_info) + ": " + SysUtils.getBrandInfo() + lineSeparator;
        info += context.getString(R.string.product_info) + ": " + SysUtils.getProductInfo() + lineSeparator;
        info += context.getString(R.string.model_info) + ": " + SysUtils.getModelInfo() + lineSeparator;
        info += context.getString(R.string.manufacturer_info) + ": " + SysUtils.getManufacturerInfo() + lineSeparator + lineSeparator + lineSeparator;
        return info;
    }
}