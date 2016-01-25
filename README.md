# jlog

jlog is an useful log tool for android developers.

[orhanobut](https://github.com/orhanobut)'s [logger](https://github.com/orhanobut/logger), [ZhaoKaiQiang](https://github.com/ZhaoKaiQiang)'s [KLog](https://github.com/ZhaoKaiQiang/KLog) and  [JakeWharton](https://github.com/JakeWharton)'s [timber](https://github.com/JakeWharton/timber) give me inspiration and reference, thank them for their open source spirit.

Combining with work experience, i made this libriary.

Hope you enjoy it. ( ^_^ )

[中文文档](https://github.com/JiongBull/jlog/blob/master/README_ZH.md)

# Features

* Compatible with android logcat, you can use `VERBOSE`, `DEBUG`, `INFO`, `WARN`, `ERROR` and `WTF` as well
* In `JSON` mode,  formats json content in a pretty way
* Jlog provides caller's class, method and line information, you can even jump to source from the console
* Simplify logcat,  uses caller's class name as TAG(custom TAG supported as well).
* Overcome logcat's 4000 words limit
* Jlog can output logs to a specified file in a specified directory
* You can decide logs in which level can be outputted to file
* At the top of log file,  provides a lot of useful information about running environment, such `os information`, `device information`, `apk information`
* Jlog formats logs in files in a pretty way, and provides enough information, such as `time`, `level` and `caller's positon`
* Jlog works well with proguard
* Support setting log file's encoding, such as `UTF-8`, `GBK`
* Support setting log file's time format
* Support setting log file's timezone
* Logs are divied into separate files by time segment, default is 24H, file's name is like `2016-01-19.log`, you can set `prefix` and `segment` of time, such as `userid_2016-01-19_2021.log`

![jlog sample](http://7xize8.com1.z0.glb.clouddn.com/jlog_sample.gif)

# Dependency

```
dependencies {
    compile 'com.jiongbull:jlog:1.0.3'
}
```

# Configuration

## Initialization

It is recommend that initializing jlog's global configuration in your application's `onCreate()` method, then we can use it everywhere.

```
public class RootApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JLog.init(this)
            .setDebug(BuildConfig.DEBUG);
    }
}
```

All configuration is saved in `JLog` class. You can obtain it by `getSettings()` method, it will return a `Settings` object. Modify the `settings` object and set it by `setSettings()` method, it will work next time.

Add follow codes to your proguard file (eg: `proguard-rules.pro`).

```
-keepattributes SourceFile, LineNumberTable
-keep class com.jiongbull.jlog.** { *; }
```

## init(context)

Set application context.

```
JLog.init(this);
```
## setDebug(boolean)

Default is true, logs will be outputed to the console. Pls set this variable as false when release your app.

```
JLog.init(this)
    .setDebug(false);
```

or

```
JLog.init(this)
    .setDebug(BuildConfig.DEBUG);
```

## writeToFile(boolean)

If true, logs will output to file, default is false.

```
JLog.init(this)
    .writeToFile(true);
```

## setLogLevelsForFile(List<LogLevel>)

This method decides logs in which level can be outputted to file. Default logLevels are `LogLevel.ERROR` and `LogLevel.WTF`.

```
List<LogLevel> logLevels = new ArrayList<>();
logLevels.add(LogLevel.ERROR);
logLevels.add(LogLevel.JSON);
JLog.init(this)
    .writeToFile(true)
    .setLogLevelsForFile(logLevels);
```

## setLogDir(String)

Configure the directory that saving logs, the directory is located in sdcard and default  name is `jlog`.

![default directory](http://7xize8.com1.z0.glb.clouddn.com/jlog_def_directory.jpg)

You can use your app's name as directory's name.

```
JLog.init(this)
    .writeToFile(true)
    .setLogDir(getString(R.string.app_name));
```

![app directory](http://7xize8.com1.z0.glb.clouddn.com/jlog_app_directory.jpg)

Sub directory is supported as well, you can use some unique words as sub directory's name, such as user id.

```
JLog.init(this)
    .writeToFile(true)
    .setLogDir(getString(R.string.app_name) + File.separator + "JiongBull");
```

![app sub directory](http://7xize8.com1.z0.glb.clouddn.com/jlog_app_sub_directory.jpg)

## setLogPrefix(String)

If you don't want use sub directory for logs, you may try `prefix` for log file.

```
JLog.init(this)
    .writeToFile(true)
    .setLogDir(getString(R.string.app_name))
    .setLogPrefix("JiongBull");
```

![prefix file](http://7xize8.com1.z0.glb.clouddn.com/jlog_prefix_file.jpg)

## setLogSegment(LogSegment)

Logs are divied into separate files by time segment, default is 24H, file's name is like `2016-01-19.log`, if is setted to `LogSegment.ONE_HOUR`, file's name is like `2016-01-19_0203`, which means logs were recorded from 2:00 to 3:30.

```
JLog.init(this)
    .writeToFile(true)
    .setLogDir(getString(R.string.app_name))
    .setLogSegment(LogSegment.ONE_HOUR);
```

![time segment](http://7xize8.com1.z0.glb.clouddn.com/jlog_time_segment.jpg)

## setCharset(String)

Configure log file's encoding, default is `UTF-8`.

```
JLog.init(this)
    .writeToFile(true)
    .setLogDir(getString(R.string.app_name))
    .setCharset("GBK");
```

## setTimeFormat(String)

Default time format is `yyyy-MM-dd HH:mm:ss`, you can use this method to make it easy to understand.

```
JLog.init(this)
    .writeToFile(true)
    .setLogDir(getString(R.string.app_name))
    .setTimeFormat("yyyy年MM月dd日 HH时mm分ss秒");
```

![time format](http://7xize8.com1.z0.glb.clouddn.com/jlog_time_format.jpg)

## setZoneOffset(ZoneOffset)

We can specify log's time zone no matter where user from, this method make it easy to find bugs, default is `ZoneOffset.P0800`(+0800), which means "Beijing time".

```
JLog.init(this)
    .writeToFile(true)
    .setLogDir(getString(R.string.app_name))
    .setZoneOffset(ZoneOffset.P0800);
```

# Usage

## JLog.v(String)

Jlog will use caller's class name as default tag.

![default tag](http://7xize8.com1.z0.glb.clouddn.com/default_tag.jpg)

## JLog.v(TAG, String)

You can specify TAG as well.

![custom tag](http://7xize8.com1.z0.glb.clouddn.com/custom_tag.jpg)

## JLog.json(json)

Jlog formats json content in a pretty way

![json](http://7xize8.com1.z0.glb.clouddn.com/json.jpg)

## Proguard

Jlog works well with proguard.

![logs in console](http://7xize8.com1.z0.glb.clouddn.com/proguard_console.jpg)

![logs in file](http://7xize8.com1.z0.glb.clouddn.com/proguard_file.jpg)

## Running environment information

At the top of log file, jlog provides a lot of useful information about running environment, such `os information`, `device information`, `apk information`

![environment info en](http://7xize8.com1.z0.glb.clouddn.com/environment_en.jpg)

![environment info zh](http://7xize8.com1.z0.glb.clouddn.com/environment_zh.jpg)

# License

```
Copyright JiongBull 2016

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

# About me

* [GITHUB](https://github.com/JiongBull)
* [BLOG](http://jiongbull.com)
* [WEIBO](http://weibo.com/jiongbull)
