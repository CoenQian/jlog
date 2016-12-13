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

package com.jiongbull.jlog;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.jiongbull.jlog.constant.LogLevel;
import com.jiongbull.jlog.constant.LogSegment;
import com.jiongbull.jlog.printer.DefaultPrinter;
import com.jiongbull.jlog.printer.JsonPrinter;
import com.jiongbull.jlog.printer.Printer;
import com.jiongbull.jlog.util.LogUtils;
import com.jiongbull.jlog.util.TimeUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志工具.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Logger {
    /** 日志类名. */
    private static final String LOG_CLASS_NAME = Logger.class.getName();
    /** 日志的打印方法名. */
    private static final String LOG_PRINT_METHOD_NAME = "printLog";

    private final Context mContext;
    private final DefaultPrinter mDefaultPrinter;
    private final JsonPrinter mJsonPrinter;

    /** 标识. */
    private String mName;
    /** DEBUG模式. */
    private boolean mDebug;
    /** 日志是否记录到文件中. */
    private boolean mWriteToFile;
    /** 日志保存的目录. */
    private String mLogDir;
    /** 日志文件的前缀. */
    private String mLogPrefix;
    /** 切片间隔，单位小时. */
    @LogSegment
    private int mLogSegment;
    /** 写入文件的日志级别. */
    private List<String> mLogLevelsForFile;
    /** 时区偏移时间. */
    @TimeUtils.ZoneOffset
    private long mZoneOffset;
    /** 时间格式. */
    private String mTimeFormat;
    /** 封装的层级，V、D、I、W、E、WTF、JSON共用，请确保他们封装在同一层级中. */
    @IntRange(from = 0, to = 10)
    private int mPackagedLevel;
    /** 云存储接口. */
    private IStorage mStorage;

    private Logger(@NonNull Builder builder) {
        mContext = builder.mContext;
        mName = builder.mName;
        mDebug = builder.mDebug;
        mWriteToFile = builder.mWriteToFile;
        mLogDir = builder.mLogDir;
        mLogPrefix = builder.mLogPrefix;
        mLogSegment = builder.mLogSegment;
        mLogLevelsForFile = builder.mLogLevelsForFile;
        mZoneOffset = builder.mZoneOffset;
        mTimeFormat = builder.mTimeFormat;
        mPackagedLevel = builder.mPackagedLevel;
        mStorage = builder.mStorage;

        mDefaultPrinter = new DefaultPrinter();
        mJsonPrinter = new JsonPrinter();
    }

    /**
     * 记录“verbose”类型的日志.
     *
     * @param tag     标签
     * @param message 信息
     */
    public void v(@NonNull String tag, @NonNull String message) {
        printLog(LogLevel.VERBOSE, tag, null, message);
    }

    /**
     * 记录“verbose”类型的日志（自动生成标签）.
     *
     * @param message 信息
     */
    public void v(@NonNull String message) {
        printLog(LogLevel.VERBOSE, null, null, message);
    }

    /**
     * 记录“debug”类型的日志.
     *
     * @param tag     标签
     * @param message 信息
     */
    public void d(@NonNull String tag, @NonNull String message) {
        printLog(LogLevel.DEBUG, tag, null, message);
    }

    /**
     * 记录“debug”类型的日志（自动生成标签）.
     *
     * @param message 信息
     */
    public void d(@NonNull String message) {
        printLog(LogLevel.DEBUG, null, null, message);
    }

    /**
     * 记录“info”类型的日志.
     *
     * @param tag     标签
     * @param message 信息
     */
    public void i(@NonNull String tag, @NonNull String message) {
        printLog(LogLevel.INFO, tag, null, message);
    }

    /**
     * 记录“info”类型的日志（自动生成标签）.
     *
     * @param message 信息
     */
    public void i(@NonNull String message) {
        printLog(LogLevel.INFO, null, null, message);
    }

    /**
     * 记录“json”类型的日志.
     *
     * @param tag  标签
     * @param json json
     */
    public void json(@NonNull String tag, @NonNull String json) {
        printLog(LogLevel.JSON, tag, null, json);
    }

    /**
     * 记录“json”类型的日志（自动生成标签）.
     *
     * @param json 信息
     */
    public void json(@NonNull String json) {
        printLog(LogLevel.JSON, null, null, json);
    }

    /**
     * 记录“warn”类型的日志.
     *
     * @param tag     标签
     * @param message 信息
     */
    public void w(@NonNull String tag, @NonNull String message) {
        printLog(LogLevel.WARN, tag, null, message);
    }

    /**
     * 记录“warn”类型的日志（自动生成标签）.
     *
     * @param message 信息
     */
    public void w(@NonNull String message) {
        printLog(LogLevel.WARN, null, null, message);
    }

    /**
     * 记录“error”类型的日志.
     *
     * @param tag     标签
     * @param t       {@link Throwable}
     * @param message 信息
     */
    public void e(@NonNull String tag, @NonNull Throwable t, @NonNull String message) {
        printLog(LogLevel.ERROR, tag, t, message);
    }

    /**
     * 记录“error”类型的日志（自动生成标签）.
     *
     * @param t       {@link Throwable}
     * @param message 信息
     */
    public void e(@NonNull Throwable t, @NonNull String message) {
        printLog(LogLevel.ERROR, null, t, message);
    }

    /**
     * 记录“error”类型的日志.
     *
     * @param tag     标签
     * @param message 信息
     */
    public void e(@NonNull String tag, @NonNull String message) {
        printLog(LogLevel.ERROR, tag, null, message);
    }

    /**
     * 记录“error”类型的日志（自动生成标签）.
     *
     * @param message 信息
     */
    public void e(@NonNull String message) {
        printLog(LogLevel.ERROR, null, null, message);
    }

    /**
     * 记录“error”类型的日志.
     *
     * @param tag 标签
     * @param t   {@link Throwable}
     */
    public void e(@NonNull String tag, @NonNull Throwable t) {
        printLog(LogLevel.ERROR, tag, t, null);
    }

    /**
     * 记录“error”类型的日志（自动生成标签）.
     *
     * @param t {@link Throwable}
     */
    public void e(@NonNull Throwable t) {
        printLog(LogLevel.ERROR, null, t, null);
    }

    /**
     * 记录“wtf”类型的日志.
     *
     * @param tag     标签
     * @param t       {@link Throwable}
     * @param message 信息
     */
    public void wtf(@NonNull String tag, @NonNull Throwable t, @NonNull String message) {
        printLog(LogLevel.WTF, tag, t, message);
    }

    /**
     * 记录“wtf”类型的日志（自动生成标签）.
     *
     * @param t       {@link Throwable}
     * @param message 信息
     */
    public void wtf(@NonNull Throwable t, @NonNull String message) {
        printLog(LogLevel.WTF, null, t, message);
    }

    /**
     * 记录“wtf”类型的日志.
     *
     * @param tag     标签
     * @param message 信息
     */
    public void wtf(@NonNull String tag, @NonNull String message) {
        printLog(LogLevel.WTF, tag, null, message);
    }

    /**
     * 记录“wtf”类型的日志（自动生成标签）.
     *
     * @param message 信息
     */
    public void wtf(@NonNull String message) {
        printLog(LogLevel.WTF, null, null, message);
    }

    /**
     * 记录“wtf”类型的日志.
     *
     * @param tag 标签
     * @param t   {@link Throwable}
     */
    public void wtf(@NonNull String tag, @NonNull Throwable t) {
        printLog(LogLevel.WTF, tag, t, null);
    }

    /**
     * 记录“wtf”类型的日志（自动生成标签）.
     *
     * @param t {@link Throwable}
     */
    public void wtf(@NonNull Throwable t) {
        printLog(LogLevel.WTF, null, t, null);
    }

    public Context getContext() {
        return mContext;
    }

    public String getName() {
        return mName;
    }

    public boolean isDebug() {
        return mDebug;
    }

    public void setDebug(boolean debug) {
        mDebug = debug;
    }

    public boolean isWriteToFile() {
        return mWriteToFile;
    }

    public void setWriteToFile(boolean writeToFile) {
        mWriteToFile = writeToFile;
    }

    public String getLogDir() {
        return mLogDir;
    }

    public void setLogDir(@NonNull String logDir) {
        mLogDir = logDir;
    }

    public String getLogPrefix() {
        return mLogPrefix;
    }

    public void setLogPrefix(String logPrefix) {
        mLogPrefix = logPrefix;
    }

    @LogSegment
    public int getLogSegment() {
        return mLogSegment;
    }

    public void setLogSegment(@LogSegment int logSegment) {
        mLogSegment = logSegment;
    }

    public List<String> getLogLevelsForFile() {
        return mLogLevelsForFile;
    }

    public void setLogLevelsForFile(@NonNull List<String> logLevelsForFile) {
        mLogLevelsForFile = logLevelsForFile;
    }

    @TimeUtils.ZoneOffset
    public long getZoneOffset() {
        return mZoneOffset;
    }

    public void setZoneOffset(@TimeUtils.ZoneOffset long zoneOffset) {
        mZoneOffset = zoneOffset;
    }

    public String getTimeFormat() {
        return mTimeFormat;
    }

    public void setTimeFormat(@NonNull String timeFormat) {
        mTimeFormat = timeFormat;
    }

    public int getPackagedLevel() {
        return mPackagedLevel;
    }

    public void setPackagedLevel(@IntRange(from = 0, to = 10) int packagedLevel) {
        mPackagedLevel = packagedLevel;
    }

    public IStorage getStorage() {
        return mStorage;
    }

    public void setStorage(IStorage storage) {
        mStorage = storage;
    }

    /**
     * 打印日志.
     *
     * @param level   {@link LogLevel}，日志级别
     * @param tag     标签
     * @param t       {@link Throwable}
     * @param message 信息
     */
    private void printLog(@LogLevel String level, String tag, Throwable t, String message) {
        if (TextUtils.isEmpty(message)) {
            message = null;
        }
        if (message == null) {
            if (t == null) {
                return; // 不记录没有信息和异常的日志
            }
            message = Log.getStackTraceString(t);
        } else {
            if (t != null) {
                message += Printer.LINE_SEPARATOR + getStackTraceString(t);
            }
        }
        StackTraceElement[] elements = new Throwable().getStackTrace();
        int index = getStackIndex(elements);
        if (index == -1) {
            throw new IllegalStateException(
                    "pls reduce packageLevel");
        }
        StackTraceElement element = elements[index];
        if (TextUtils.isEmpty(tag)) {
            tag = getTag(element);
        }
        boolean isOutputToConsole = mDebug;
        boolean isOutputToFile = mWriteToFile && mLogLevelsForFile.contains(level);
        switch (level) {
            case LogLevel.VERBOSE:
            case LogLevel.DEBUG:
            case LogLevel.INFO:
            case LogLevel.WARN:
            case LogLevel.ERROR:
            case LogLevel.WTF:
                if (isOutputToConsole) {
                    mDefaultPrinter.printConsole(level, tag, message, element);
                }
                if (isOutputToFile) {
                    mDefaultPrinter.printFile(mContext, level, message, element, mZoneOffset,
                            mTimeFormat, mLogDir, mLogPrefix, mLogSegment);
                }
                break;
            case LogLevel.JSON:
                if (isOutputToConsole) {
                    mJsonPrinter.printConsole(level, tag, message, element);
                }
                if (isOutputToFile) {
                    mJsonPrinter.printFile(mContext, level, message, element, mZoneOffset,
                            mTimeFormat, mLogDir, mLogPrefix, mLogSegment);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取TAG。
     *
     * @param element 堆栈元素
     * @return TAG
     */
    private String getTag(@NonNull StackTraceElement element) {
        return LogUtils.getSimpleClassName(element.getClassName());
    }

    /**
     * 获取调用日志类输出方法的堆栈元素索引.
     *
     * @param elements 堆栈元素
     * @return 索引位置，-1 - 不可用
     */
    private int getStackIndex(@NonNull StackTraceElement[] elements) {
        boolean isChecked = false;
        StackTraceElement element;
        for (int i = 0; i < elements.length; i++) {
            element = elements[i];
            if (LOG_CLASS_NAME.equals(element.getClassName())
                    && LOG_PRINT_METHOD_NAME.equals(element.getMethodName())) {
                isChecked = true;
            }
            if (isChecked) {
                int index = i + 2 + mPackagedLevel;
                if (index < elements.length) {
                    return index;
                }
            }
        }
        return -1;
    }

    /**
     * 获取异常栈信息，不同于Log.getStackTraceString()，该方法不会过滤掉UnknownHostException.
     *
     * @param t {@link Throwable}
     * @return 异常栈里的信息
     */
    private String getStackTraceString(@NonNull Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public static class Builder {
        private final Context mContext;
        private String mName;
        private boolean mDebug;
        private String mTimeFormat;
        @TimeUtils.ZoneOffset
        private long mZoneOffset;
        private String mLogDir;
        private String mLogPrefix;
        @LogSegment
        private int mLogSegment;
        private boolean mWriteToFile;
        private List<String> mLogLevelsForFile;
        @IntRange(from = 0, to = 10)
        private int mPackagedLevel;
        private IStorage mStorage;

        private Builder(Context context, String name) {
            mContext = context;
            mName = name;
            mDebug = true;
            mWriteToFile = false;
            mLogDir = "jlog";
            mLogPrefix = "";
            mLogSegment = LogSegment.TWENTY_FOUR_HOURS;
            mLogLevelsForFile = new ArrayList<>();
            mLogLevelsForFile.add(LogLevel.ERROR);
            mLogLevelsForFile.add(LogLevel.WTF);
            mZoneOffset = TimeUtils.ZoneOffset.P0800;
            mTimeFormat = "yyyy-MM-dd HH:mm:ss";
            mPackagedLevel = 0;
        }

        public static Builder newBuilder(@NonNull Context context, @NonNull String name) {
            return new Builder(context.getApplicationContext(), name);
        }

        public Builder setDebug(boolean debug) {
            mDebug = debug;
            return this;
        }

        public Builder setTimeFormat(@NonNull String timeFormat) {
            mTimeFormat = timeFormat;
            return this;
        }

        public Builder setZoneOffset(@TimeUtils.ZoneOffset long zoneOffset) {
            mZoneOffset = zoneOffset;
            return this;
        }

        public Builder setLogDir(@NonNull String logDir) {
            mLogDir = logDir;
            return this;
        }

        public Builder setLogPrefix(String logPrefix) {
            mLogPrefix = logPrefix;
            return this;
        }

        public Builder setLogSegment(@LogSegment int logSegment) {
            mLogSegment = logSegment;
            return this;
        }

        public Builder setWriteToFile(boolean writeToFile) {
            mWriteToFile = writeToFile;
            return this;
        }

        public Builder setLogLevelsForFile(@NonNull List<String> logLevelsForFile) {
            mLogLevelsForFile = logLevelsForFile;
            return this;
        }

        public Builder setPackagedLevel(@IntRange(from = 0, to = 10) int packagedLevel) {
            mPackagedLevel = packagedLevel;
            return this;
        }

        public Builder setStorage(IStorage storage) {
            mStorage = storage;
            return this;
        }

        public Logger build() {
            Logger logger = new Logger(this);
            LoggerGlobal.addLogger(logger);
            return logger;
        }
    }
}