# jlog(基佬哥)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-JLog-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3166)
[![Download](https://api.bintray.com/packages/jiongbull/maven/jlog/images/download.svg) ](https://bintray.com/jiongbull/maven/jlog/_latestVersion)
[![Build Status](https://travis-ci.org/JiongBull/jlog.svg?branch=master)](https://travis-ci.org/JiongBull/jlog)

jlog是一款针对Android开发者的日志工具。

![jilao](http://7xize8.com1.z0.glb.clouddn.com/jlog_jilao.png)

[orhanobut](https://github.com/orhanobut)的[logger](https://github.com/orhanobut/logger)，[ZhaoKaiQiang](https://github.com/ZhaoKaiQiang)的[KLog](https://github.com/ZhaoKaiQiang/KLog)和[JakeWharton](https://github.com/JakeWharton)的[timber](https://github.com/JakeWharton/timber)给了我灵感和参考，感谢他们的开源精神。

再结合一些工作上的经验，就撸出了这个库。

希望你会喜欢它。( ^_^ )

# 特点

* 兼容android logcat，`VERBOSE`、`DEBUG`、`INFO`、`WARN`、`ERROR`和`WTF`全都有，一个都不能少
* 在`JSON`模式下，jlog会把json内容格式化，便于理解
* jlog提供了调用者的类、方法和行号信息，甚至可以从控制台直接跳转到源文件
* 简化了logcat，jlog使用调用者的类名作TAG（当然也支持自定义TAG）
* 突破了logcat的4000字长度限制
* jlog可以把日志输出到指定的目录和文件中
* 你可以决定哪些级别的日志写入文件中
* 在日志文件的顶部，jlog提供了很多有用的运行环境相关的信息，比如`操作系统信息`、`设备信息`和`应用信息`
* jlog针对写入文件的日志做了格式化，同时提供了足够的信息方便分析，例如`时间`、`日志等级`和`调用位置`
* 混淆后也能工作正常（获取调用位置）
* 支持配置日志文件编码，例如`UTF-8`，`GBK`
* 支持设置日志文件的时间格式
* 支持设置日志文件的时区（便于调试其他时区的设备）
* 日志按照时间切片写入到不同的文件中，默认是24小时，文件名诸如`2016-01-19.log`，你也可以指定`前缀`和`时间切片`，比如`用户标识_2016-01-19_2021.log`
* **如果你需要把日志同步上传到[七牛云存储](http://www.qiniu.com/)，可以考虑使用这个项目[jlog-qiniu](https://github.com/JiongBull/jlog-qiniu)**

![jlog sample](http://7xize8.com1.z0.glb.clouddn.com/jlog_sample.gif)

# 依赖

```
dependencies {
    compile 'com.jiongbull:jlog:1.0.4'
}
```

# 配置

## 初始化

建议在你的application的`onCreate()`方法里初始化jlog的全局配置，设置一次终身受用。

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

所有的配置都保存在`JLog`类里。你可以通过`getSettings()`来获取配置，它会返回一个`Settings`对象。修改`Settings`对象后再用`setSettings()`设置，下次使用时就会生效了。

记得把下面的代码添加到你的混淆文件中（例如：`proguard-rules.pro`）。

```
-keepattributes SourceFile, LineNumberTable
-keep class com.jiongbull.jlog.** { *; }
```
如果你的应用的'targetSdkVersion'是23+，不要忘记在闪屏页或首页申请'android.permission.WRITE_EXTERNAL_STORAG'权限。
你可以参考这篇文章，[在Android 6.0 设备上动态获取权限](http://gudong.name/%E6%8A%80%E6%9C%AF/2015/11/10/android_m_permission.html).

## init(context)

建议使用application context。

```
JLog.init(this);
```
## setDebug(boolean)

默认是true，日志会输出到控制台中。在发布版本的时候请把这个变量设置为false。

```
JLog.init(this)
    .setDebug(false);
```

或

```
JLog.init(this)
    .setDebug(BuildConfig.DEBUG);
```

## writeToFile(boolean)

日志开关，如果是true，日志会输出到文件中，默认是false。

```
JLog.init(this)
    .writeToFile(true);
```

## setLogLevelsForFile(List<LogLevel>)

这个方法决定了哪些级别的日志可以输出到文件中。默认的日志级别是`LogLevel.ERROR`和`LogLevel.WTF`。

```
List<LogLevel> logLevels = new ArrayList<>();
logLevels.add(LogLevel.ERROR);
logLevels.add(LogLevel.JSON);
JLog.init(this)
    .writeToFile(true)
    .setLogLevelsForFile(logLevels);
```

## setLogDir(String)

配置日志保存的目录名称，日志目录是位于sd卡中，默认名称是`jlog`.

![default directory](http://7xize8.com1.z0.glb.clouddn.com/jlog_def_directory.jpg)

可以使用应用的名称作为日志目录名。

```
JLog.init(this)
    .writeToFile(true)
    .setLogDir(getString(R.string.app_name));
```

![app directory](http://7xize8.com1.z0.glb.clouddn.com/jlog_app_directory.jpg)

子目录当然也支持啦，可以使用一些唯一标识作为子目录的名称，比如用户标识。

```
JLog.init(this)
    .writeToFile(true)
    .setLogDir(getString(R.string.app_name) + File.separator + "JiongBull");
```

![app sub directory](http://7xize8.com1.z0.glb.clouddn.com/jlog_app_sub_directory.jpg)

## setLogPrefix(String)

如果不想使用子目录，你或许可以试一试日志文件的`前缀`功能。

```
JLog.init(this)
    .writeToFile(true)
    .setLogDir(getString(R.string.app_name))
    .setLogPrefix("JiongBull");
```

![prefix file](http://7xize8.com1.z0.glb.clouddn.com/jlog_prefix_file.jpg)

## setLogSegment(LogSegment)

日志按照时间切片写入到不同的文件中，默认是24小时，文件名诸如`2016-01-19.log`，如果设置为`LogSegment.ONE_HOUR`，文件名就会变成诸如`2016-01-19_0203.log`那样了，表示文件里记录的是2:00到3:00的日志。

```
JLog.init(this)
    .writeToFile(true)
    .setLogDir(getString(R.string.app_name))
    .setLogSegment(LogSegment.ONE_HOUR);
```

![time segment](http://7xize8.com1.z0.glb.clouddn.com/jlog_time_segment.jpg)

## setCharset(String)

配置日志文件的编码格式，默认是`UTF-8`。

```
JLog.init(this)
    .writeToFile(true)
    .setLogDir(getString(R.string.app_name))
    .setCharset("GBK");
```

## setTimeFormat(String)

默认的时间格式是`yyyy-MM-dd HH:mm:ss`，你可以使用这个方法让日志更容易理解。

```
JLog.init(this)
    .writeToFile(true)
    .setLogDir(getString(R.string.app_name))
    .setTimeFormat("yyyy年MM月dd日 HH时mm分ss秒");
```

![time format](http://7xize8.com1.z0.glb.clouddn.com/jlog_time_format.jpg)

## setZoneOffset(ZoneOffset)

我们可以指定文件里日志时间的时区，而不受用户位置的影响，这样会更容易定位问题，默认是`ZoneOffset.P0800`（+0800），表示“北京时间”。

```
JLog.init(this)
    .writeToFile(true)
    .setLogDir(getString(R.string.app_name))
    .setZoneOffset(ZoneOffset.P0800);
```

# 用法

## JLog.v(String)

jlog默认会使用调用者的类名作TAG。

![default tag](http://7xize8.com1.z0.glb.clouddn.com/default_tag.jpg)

## JLog.v(TAG, String)

你也可以自己指定TAG。

![custom tag](http://7xize8.com1.z0.glb.clouddn.com/custom_tag.jpg)

## JLog.json(json)

jlog会把json内容格式化，便于理解。

![json](http://7xize8.com1.z0.glb.clouddn.com/json.jpg)

## 混淆

jlog在混淆模式下依旧工作正常。

![logs in console](http://7xize8.com1.z0.glb.clouddn.com/proguard_console.jpg)

![logs in file](http://7xize8.com1.z0.glb.clouddn.com/proguard_file.jpg)

## 运行环境信息

在日志文件的顶部，jlog提供了很多有用的运行环境相关的信息，比如`操作系统信息`、`设备信息`和`应用信息`

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

# 关于

* [GITHUB](https://github.com/JiongBull)
* [博客](http://jiongbull.com)
* [微博](http://weibo.com/jiongbull)
