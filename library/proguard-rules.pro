##---------------Common android apps---------------
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-useuniqueclassmembernames

-allowaccessmodification
-keepattributes SourceFile,LineNumberTable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

##---------------Okio---------------
-dontwarn okio.**

##---------------jlog---------------
-keep class com.jiongbull.jlog.**{*;}
