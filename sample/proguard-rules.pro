##---------------Begin: proguard configuration common for all Android apps ----------
# 压缩级别
-optimizationpasses 5
# 不使用大小写混合的类名
-dontusemixedcaseclassnames
# 不忽略非公共库的类
-dontskipnonpubliclibraryclasses
# 不忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers
# 不要预校验
-dontpreverify
# 混淆时是否记录日志
-verbose
# 混淆算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

##---------------Begin: 日志相关----------##
# 所有class的结构信息
-dump class_files.txt
# 未混淆的类和成员
-printseeds seeds.txt
# 被删除的代码
-printusage unused.txt
# 混淆前后的映射
-printmapping mapping.txt
##---------------End: 日志相关----------##

# 优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification
# 保留注解属性
-keepattributes *Annotation*
# 重定义源文件中的类名
#-renamesourcefileattribute SourceFile
# 保留属性（源文件名、行号）
-keepattributes SourceFile,LineNumberTable
# 重新定义包名
-repackageclasses ''

# 不混淆指定的类和类成员
# keep {modifier} class_specification

# 不混淆指定类的成员
# keepclassmemebers {modifier} class_specification

# 不混淆指定的类和类的成员
# keepclasswithmembers {modifier} class_specification

# 不混淆指定的类和类的成员名称
# keepnames class_specification

# 不混淆类的成员名
# keepclassmembernames class_specification

# 不混淆类名和类的成员名
# keepclasseswithmembernames class_specification

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#-keep public class * {
#    public protected *;
#}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#---------------End: proguard configuration common for all Android apps ----------

##---------------Begin: proguard configuration for Apache Http----------
-dontwarn org.apache.http.**
-keep class org.apache.http.**{*;}
-keep interface org.apache.http.**{*;}
##---------------End: proguard configuration for Apache Http----------

##---------------Begin: proguard configuration for support-v4----------
-dontwarn android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
##---------------End: proguard configuration for support-v4----------

##---------------Begin: proguard configuration for support-v4----------
-dontwarn android.net.http.**
-keep interface android.net.http.** { *; }
-keep class android.net.http.** { *; }
-keep public class * extends android.net.http.**
##---------------End: proguard configuration for support-v4----------

##---------------Begin: proguard configuration for JLog----------
-keep class com.jiongbull.jlog.** { *; }
##---------------End: proguard configuration for JLog----------