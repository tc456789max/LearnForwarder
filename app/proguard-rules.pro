# ProGuard 混淆规则

# 保留应用入口点
-keep public class com.example.learnforwarder.App
-keep public class com.example.learnforwarder.ui.MainActivity
-keep public class com.example.learnforwarder.ui.SettingsActivity
-keep public class com.example.learnforwarder.ui.LogActivity
-keep public class com.example.learnforwarder.ui.FileReceiveActivity

# 保留服务
-keep public class com.example.learnforwarder.service.NotificationListenerService
-keep public class com.example.learnforwarder.service.MainForegroundService
-keep public class com.example.learnforwarder.receiver.BootReceiver

# 保留数据库相关类
-keep class com.example.learnforwarder.data.** { *; }
-keep class androidx.room.** { *; }
-keepclassmembers class * {
    @androidx.room.* <fields>;
}

# 保留ViewModel
-keep class androidx.lifecycle.ViewModel { *; }
-keep class * extends androidx.lifecycle.ViewModel { *; }

# 保留LiveData
-keep class androidx.lifecycle.LiveData { *; }
-keep class androidx.lifecycle.MutableLiveData { *; }

# 保留Retrofit
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# 保留GSON
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# 保留OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# 保留Timber日志库
-keep class timber.log.Timber { *; }
-keep class timber.log.Timber$* { *; }

# 保留AndroidX
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# 保留Kotlin相关
-keep class kotlin.** { *; }
-keep interface kotlin.** { *; }
-keep class kotlinx.** { *; }
-keep interface kotlinx.** { *; }

# 保留枚举
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留本地方法
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留自定义View的构造方法
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# 删除日志
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# 优化选项
-optimizationpasses 5
-dontusemixedcaseclassnames
-verbose

# 移除未使用的代码
-dontshrink
-dontoptimize
