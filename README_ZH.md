# jlog(基佬哥)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-jlog-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3166)
[![Download](https://jitpack.io/v/JiongBull/jlog.svg)](https://jitpack.io/#JiongBull/jlog)
[![Build Status](https://travis-ci.org/JiongBull/jlog.svg?branch=master)](https://travis-ci.org/JiongBull/jlog)

jlog是一款针对Android开发者的日志工具。

![jilao](http://7xize8.com1.z0.glb.clouddn.com/jlog_jilao.png)

[orhanobut](https://github.com/orhanobut)的[logger](https://github.com/orhanobut/logger)，[ZhaoKaiQiang](https://github.com/ZhaoKaiQiang)的[KLog](https://github.com/ZhaoKaiQiang/KLog)和[JakeWharton](https://github.com/JakeWharton)的[timber](https://github.com/JakeWharton/timber)给了我灵感和参考，感谢他们的开源精神。

再结合一些工作上的经验，就撸出了这个库。

希望你会喜欢它。( ^_^ )

## 特点

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
* 支持设置日志文件的时间格式
* 支持设置日志文件的时区（便于调试其他时区的设备）
* 日志按照时间切片写入到不同的文件中，默认是24小时，文件名诸如`2016-01-19.log`，你也可以指定`前缀`和`时间切片`，比如`${userid}_2016-01-19_2021.log`
* 支持设置日志文件的磁盘容量，超出后会按文件的最后修改时间清除
* 支持上传日志到[七牛](http://www.qiniu.com)
* 支持扩展
* `TimingLogger`可以记录方法运行的时间

![jlog sample](http://7xize8.com1.z0.glb.clouddn.com/jlog_sample_img.gif
)

## 依赖

在根目录的build.gradle里添加仓库。

```groovy
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
```

在模块的build.gradle中添加依赖。

```groovy
dependencies {
     compile 'com.github.JiongBull:jlog:0.0.1'
}
```

## 配置

### 初始化

建议在你的application的`onCreate()`方法里初始化jlog的全局配置，设置一次终身受用。

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
                /* 下面的属性都是默认值，你可以根据需求决定是否修改他们. */
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

build后所有的属性都保存在`Logger`对象中。修改它的属性，下次就会生效。

如果你的应用的'targetSdkVersion'是23+，不要忘记在闪屏页或首页申请'android.permission.WRITE_EXTERNAL_STORAG'权限。

### setDebug(boolean)

默认是true，日志会输出到控制台中。在发布版本的时候请把这个变量设置为false。

```java
logger.setDebug(false);
```

或

```java
logger.setDebug(BuildConfig.DEBUG);
```

### writeToFile(boolean)

日志开关，如果是true，日志会输出到文件中，默认是false。

```java
  logger.writeToFile(true);
```

### setLogDir(String)

配置日志保存的目录名称，日志目录是位于内部sd卡中，默认名称是`jlog`.

可以使用应用的名称作为日志目录名。

```java
logger.setLogDir(getString(R.string.app_name));
```

子目录当然也支持啦，可以使用一些唯一标识作为子目录的名称，比如用户标识。

```java
logger.setLogDir(getString(R.string.app_name) + File.separator + ${userid});

### setLogPrefix(String)

如果不想使用子目录，你或许可以试一试日志文件的`前缀`功能。

```java
logger.setLogPrefix("JiongBull");
```

### setLogSegment(LogSegment)

日志按照时间切片写入到不同的文件中，默认是24小时，文件名诸如`2016-01-19.log`，如果设置为`LogSegment.ONE_HOUR`，文件名就会变成诸如`2016-01-19_0203.log`那样了，表示文件里记录的是2:00到3:00的日志。

```java
logger.setLogSegment(LogSegment.ONE_HOUR);
```

### setLogLevelsForFile(List<String>)

这个方法决定了哪些级别的日志可以输出到文件中。默认的日志级别是`LogLevel.ERROR`和`LogLevel.WTF`。

```java
List<LogLevel> logLevels = new ArrayList<>();
logLevels.add(LogLevel.INFO);
logLevels.add(LogLevel.ERROR);
logger.setLogLevelsForFile(logLevels);
```

### setZoneOffset(ZoneOffset)

我们可以指定文件里日志时间的时区，而不受用户位置的影响，这样会更容易定位问题，默认是`ZoneOffset.P0800`（+0800），表示“北京时间”。

```java
logger.setZoneOffset(ZoneOffset.P0800);
```

### setTimeFormat(String)

默认的时间格式是`yyyy-MM-dd HH:mm:ss`，你可以使用这个方法让日志更容易理解。

```java
logger.setTimeFormat("yyyy年MM月dd日 HH时mm分ss秒");
```

### setPackagedLevel(int)

如果需要扩展jlog，请设置封装的层级，否则jlog不能获取调用者的信息.

```java
logger.setPackagedLevel(1);
```

### setStorage(IStorage)

可以继承`IStorage`接口，通过`upload`接口把日志上传到远程服务器上，它每隔15分钟会被调用一次。

```java
logger.setStorage(new IStorage() {
    @Override
    public void upload(@NonNull Logger logger) {
        // 把日志上传到远程服务器上.
    }
})
```

有两种已经预定义的`IStorage`实现，可以直接使用。

*  [jlog-storage-qiniu](https://github.com/JiongBull/jlog-storage-qiniu), 可以把日志上传到[七牛](http://www.qiniu.com)
* [jlog-storage-disk](https://github.com/JiongBull/jlog-storage-disk), 设置日志的磁盘容量，当超过容量后，会根据日志目录中文件的最后修改时间删除文件。

## 用法

### logger.v(String)

jlog默认会使用调用者的类名作TAG。

### logger.v(TAG, String)

你也可以自己指定TAG。

### logger.json(json)

jlog会把json内容格式化，便于理解。

### 混淆

jlog在混淆模式下依旧工作正常。

### 运行环境信息

在日志文件的顶部，jlog提供了很多有用的运行环境相关的信息，比如`操作系统信息`、`设备信息`和`应用信息`

## License

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

## 关于

* [GITHUB](https://github.com/JiongBull)
* [博客](http://jiongbull.com)
* [微博](http://weibo.com/jiongbull)
