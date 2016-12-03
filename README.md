# jlog

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-jlog-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3166)
[![License](https://img.shields.io/badge/License-Apache%202.0-brightgreen.svg)](https://github.com/JiongBull/jlog/blob/master/LICENSE.md)
[![Download](https://jitpack.io/v/JiongBull/jlog.svg)](https://jitpack.io/#JiongBull/jlog)
[![Build Status](https://travis-ci.org/JiongBull/jlog.svg?branch=master)](https://travis-ci.org/JiongBull/jlog)

jlog is a useful log tool for android developers which was inspired by these projects:

* [orhanobut](https://github.com/orhanobut)'s [logger](https://github.com/orhanobut/logger)
* [ZhaoKaiQiang](https://github.com/ZhaoKaiQiang)'s [KLog](https://github.com/ZhaoKaiQiang/KLog)
* [JakeWharton](https://github.com/JakeWharton)'s [timber](https://github.com/JakeWharton/timber)

Combining with work experience, i made this libriary.

Hope you enjoy it. ( ^_^ )

[中文文档](https://github.com/JiongBull/jlog/blob/master/README_ZH.md)

## Features

* Compatible with android logcat, you can use `VERBOSE`, `DEBUG`, `INFO`, `WARN`, `ERROR` and `WTF` as well
* In `JSON` mode,  formats json content in a pretty way
* Jlog provides caller's class, method and line information, you can even jump to source from the console
* Simplify logcat,  uses caller's class name as TAG(custom TAG supported as well).
* Overcome logcat's 4000 words limit
* Jlog can output logs to a specified file in a specified directory
* You can decide logs in which level can be outputted to file
* At the top of log file,  provides a lot of useful information about running environment, such `os information`, `device information`, `apk information`
* Jlog formats logs in files in a pretty way, and provides enough information, such as `time`, `level`, `thread` and `caller's positon`
* Jlog works well with proguard
* Support setting log file's time format
* Support setting log file's timezone
* Logs are divied into separate files by time segment, default is 24H, file's name is like `2016-01-19.log`, you can set `prefix` and `segment` of time, such as `${userid}_2016-01-19_2021.log`
* Support setting disk's capacity for logs
* Support uploading logs to [qiniu](http://www.qiniu.com)
* Support extending
* `TimingLogger` can log the time of method proceeding

![jlog sample](http://7xize8.com1.z0.glb.clouddn.com/jlog_sample_img.gif
)

## Dependency

Add the repository to the root's build.gradle.

```groovy
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
```

And add dependencies to the module's build.gradle.

```groovy
dependencies {
     compile 'com.github.JiongBull:jlog:0.1.0'
}
```

## Configuration

### Initialization

It is recommend that initializing jlog's global configuration in your application's `onCreate()` method, then we can use it everywhere.

```java
public class RootApp extends Application {

    private static Logger sLogger;

    @Override
    public void onCreate() {
        super.onCreate();
        List<String> logLevels = new ArrayList<>();
        logLevels.add(LogLevel.ERROR);
        logLevels.add(LogLevel.WTF);

        sLogger = Logger.Builder.newBuilder(getApplicationContext(), "jlog")
                /* properties below are default value, you can modify them or not. */
                .setDebug(true)
                .setWriteToFile(false)
                .setLogDir("jlog")
                .setLogPrefix("")
                .setLogSegment(LogSegment.TWELVE_HOURS)
                .setLogLevelsForFile(logLevels)
                .setZoneOffset(TimeUtils.ZoneOffset.P0800)
                .setTimeFormat("yyyy-MM-dd HH:mm:ss")
                .setPackagedLevel(0)
                .setStorage(null)
                .build();
    }

    public static Logger getLogger() {
        return sLogger;
    }
}
```

All properties are saved in `Logger`'s object after build . Modify it's properties, it will work next time.

For example:
```java
logger.setWriteToFile(true);
```

If your app's `targetSdkVersion` is 23+, don't forget to apply for `android.permission.WRITE_EXTERNAL_STORAGE` permission in your splash or main activity.

### setDebug(boolean)

Default is true, logs will be outputed to the console. Pls set this variable as false when release your app.

```java
logger.setDebug(false);
```

or

```java
logger.setDebug(BuildConfig.DEBUG);
```

### writeToFile(boolean)

If true, logs will output to file, default is false.

```java
  logger.writeToFile(true);
```

### setLogDir(String)

Configure the directory which saving logs, the directory is located in external sdcard and default  name is `jlog`.

You can use your app's name as directory's name.

```java
logger.setLogDir(getString(R.string.app_name));
```

Sub directory is supported as well, you can use some unique words as sub directory's name, such as user id.

```java
logger.setLogDir(getString(R.string.app_name) + File.separator + ${userid});
```

### setLogPrefix(String)

If you don't want use sub directory for logs, you may try `prefix` for log file.

```java
logger.setLogPrefix("JiongBull");
```

### setLogSegment(LogSegment)

Logs are divied into separate files by time segment, default is 24H, file's name is like `2016-01-19.log`, if it is setted to `LogSegment.ONE_HOUR`, file's name is like `2016-01-19_0203`, which means logs were recorded from 2:00 to 3:00.

```java
logger.setLogSegment(LogSegment.ONE_HOUR);
```

### setLogLevelsForFile(List<String>)

This method decides logs in which level can be outputted to file. Default  are `LogLevel.ERROR` and `LogLevel.WTF`.

```java
List<LogLevel> logLevels = new ArrayList<>();
logLevels.add(LogLevel.INFO);
logLevels.add(LogLevel.ERROR);
logger.setLogLevelsForFile(logLevels);
```

### setZoneOffset(ZoneOffset)

We can specify log's time zone no matter where user from, this method make it easy to find bugs, default is `ZoneOffset.P0800`(+0800), which means "Beijing time".

```java
logger.setZoneOffset(ZoneOffset.P0800);
```

## setTimeFormat(String)

Default time format is `yyyy-MM-dd HH:mm:ss`, you can use this method to make it easy to understand.

```java
logger.setTimeFormat("yyyy年MM月dd日 HH时mm分ss秒");
```

### setPackagedLevel(int)

If you want to extend jlog, please set package's level(hierarchy), otherwise, jlog can't get invoker's info.

```java
logger.setPackagedLevel(1);
```

### setStorage(IStorage)

You can implement `IStorage` interface, upload logs to remote server in `upload` method, it will be invoked every fifteen minutes.

```java
logger.setStorage(new IStorage() {
    @Override
    public void upload(@NonNull Logger logger) {
        // upload logs to remote server
    }
})
```

There are two predefined `IStorage` implement, you can use them directly.

*  [jlog-storage-qiniu](https://github.com/JiongBull/jlog-storage-qiniu), upload logs to [qiniu](http://www.qiniu.com) automatically
* [jlog-storage-disk](https://github.com/JiongBull/jlog-storage-disk), set disk's capacity for logs, when over capacity,
it will delete files according to the last modified time of files in log directory


## Usage

### logger.v(String)

Jlog will use caller's class name as default tag.

### logger.v(TAG, String)

You can specify TAG as well.

### logger.json(json)

Jlog formats json content in a pretty way

## About me

[![GitHub](https://img.shields.io/badge/GitHub-JiongBull-blue.svg)](https://github.com/JiongBull)
[![WeiBo](https://img.shields.io/badge/weibo-JiongBull-blue.svg)](https://weibo.com/jiongbull)
[![Blog](https://img.shields.io/badge/Blog-JiongBull-blue.svg)](http://jiongbull.com)
