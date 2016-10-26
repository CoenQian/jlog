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

import com.jiongbull.jlog.constant.LogLevel;
import com.jiongbull.jlog.constant.LogSegment;
import com.jiongbull.jlog.constant.ZoneOffset;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置.
 */
public class Settings {

    private Context mContext;
    /** DEBUG模式. */
    private boolean mIsDebug;
    /** 字符集. */
    private String mCharset;
    /** 时间格式. */
    private String mTimeFormat;
    /** 时区偏移时间. */
    private ZoneOffset mZoneOffset;
    /** 日志保存的目录. */
    private String mLogDir;
    /** 日志压缩包保存的目录. */
    private String mZipDir;
    /** 日志文件的前缀. */
    private String mLogPrefix;
    /** 切片间隔，单位小时. */
    private LogSegment mLogSegment;
    /** 日志是否记录到文件中. */
    private boolean mWriteToFile;
    /** 日志文件夹缓存大小. */
    private long mCacheSize;
    /** 写入文件的日志级别. */
    private List<LogLevel> mLogLevelsForFile;
    /** 封装的层级，V、D、I、W、E、WTF、JSON共用，请确保他们封装在同一层级中. */
    private int mPackagedLevel;

    public Settings() {
        mIsDebug = true;
        mCharset = "UTF-8";
        mTimeFormat = "yyyy-MM-dd HH:mm:ss";
        mZoneOffset = ZoneOffset.P0800;
        mLogDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "jlog";
        mZipDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "jlog";
        mLogPrefix = "";
        mLogSegment = LogSegment.TWENTY_FOUR_HOURS;
        mWriteToFile = false;
        mCacheSize = 10 * 1024 * 1024;
        mLogLevelsForFile = new ArrayList<>();
        mLogLevelsForFile.add(LogLevel.ERROR);
        mLogLevelsForFile.add(LogLevel.WTF);
        mLogLevelsForFile.add(LogLevel.CRASH);
        mPackagedLevel = 0;
    }

    public Context getContext() {
        return mContext;
    }

    public Settings setContext(@NonNull Context context) {
        mContext = context;
        return this;
    }

    public String getCharset() {
        return mCharset;
    }

    public Settings setCharset(@NonNull String charset) {
        mCharset = charset;
        return this;
    }

    public String getTimeFormat() {
        return mTimeFormat;
    }

    public Settings setTimeFormat(@NonNull String timeFormat) {
        mTimeFormat = timeFormat;
        return this;
    }

    public ZoneOffset getZoneOffset() {
        return mZoneOffset;
    }

    public Settings setZoneOffset(@NonNull ZoneOffset zoneOffset) {
        mZoneOffset = zoneOffset;
        return this;
    }

    public String getLogDir() {
        return mLogDir;
    }

    public Settings setLogDir(@NonNull String logDir) {
        mLogDir = logDir;
        return this;
    }

    public String getZipDir() {
        return mZipDir;
    }

    public Settings setZipDir(String zipDir) {
        mZipDir = zipDir;
        return this;
    }

    public String getLogPrefix() {
        return mLogPrefix;
    }

    public Settings setLogPrefix(@NonNull String logPrefix) {
        mLogPrefix = logPrefix;
        return this;
    }

    public LogSegment getLogSegment() {
        return mLogSegment;
    }

    public Settings setLogSegment(@NonNull LogSegment logSegment) {
        mLogSegment = logSegment;
        return this;
    }

    public boolean isWriteToFile() {
        return mWriteToFile;
    }

    public Settings writeToFile(boolean isWriteToFile) {
        mWriteToFile = isWriteToFile;
        return this;
    }

    public long getCacheSize() {
        return mCacheSize;
    }

    /**
     * 设置日志文件夹的缓存大小，超过该体积则清空文件夹，默认10m
     * @param cacheSize 允许日志缓存文件夹大小，单位M
     */
    public Settings setCacheSize(long cacheSize) {
        mCacheSize = cacheSize * 1024 * 1024;
        return this;
    }

    public List<LogLevel> getLogLevelsForFile() {
        return mLogLevelsForFile;
    }

    public Settings setLogLevelsForFile(@NonNull List<LogLevel> logLevelsForFile) {
        mLogLevelsForFile = logLevelsForFile;
        return this;
    }

    public boolean isDebug() {
        return mIsDebug;
    }

    public Settings setDebug(boolean isDebug) {
        mIsDebug = isDebug;
        return this;
    }

    public int getPackagedLevel() {
        return mPackagedLevel;
    }

    public Settings setPackagedLevel(int packagedLevel) {
        mPackagedLevel = packagedLevel;
        return this;
    }
}