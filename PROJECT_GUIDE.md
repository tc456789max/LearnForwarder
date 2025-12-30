# Learn Forwarder - Android 学习项目完整指南

## 项目概述

**Learn Forwarder** 是一个基于"转阅助手"应用原理的学习项目，旨在通过实现类似的功能来学习现代Android开发技术。该项目采用**MVVM + Repository**架构模式，集成了通知监听、文件处理、后台服务等核心功能。

## 项目特性

- ✅ **通知监听服务** - 实时监听系统通知
- ✅ **文件分享处理** - 接收通过Intent分享的文件
- ✅ **前台服务** - 确保应用后台稳定运行
- ✅ **本地数据存储** - 使用Room数据库存储记录
- ✅ **现代架构** - MVVM + Repository + Coroutines
- ✅ **权限管理** - 完善的权限申请和处理机制

## 项目结构

```
LearnForwarder/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/learnforwarder/
│   │   │   │   ├── ui/
│   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   ├── SettingsActivity.kt
│   │   │   │   │   ├── LogActivity.kt
│   │   │   │   │   └── FileReceiveActivity.kt
│   │   │   │   ├── service/
│   │   │   │   │   ├── NotificationListenerService.kt
│   │   │   │   │   └── MainForegroundService.kt
│   │   │   │   ├── receiver/
│   │   │   │   │   └── BootReceiver.kt
│   │   │   │   ├── data/
│   │   │   │   │   └── DatabaseModels.kt
│   │   │   │   └── App.kt
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   ├── activity_settings.xml
│   │   │   │   │   ├── activity_log.xml
│   │   │   │   │   └── activity_file_receive.xml
│   │   │   │   ├── values/
│   │   │   │   │   └── strings.xml
│   │   │   │   └── xml/
│   │   │   │       └── file_paths.xml
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   └── build.gradle
└── build.gradle
```

## 核心组件详解

### 1. 通知监听服务 (NotificationListenerService)

**文件**: `service/NotificationListenerService.kt`

该服务监听系统通知栏的所有消息，并将其保存到本地数据库。

**关键功能**:
- 监听通知发布事件
- 提取通知的标题和文本内容
- 记录通知的包名和时间戳
- 异步保存到Room数据库

**权限要求**:
```xml
<uses-permission android:name="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE" />
```

**使用方式**:
用户需要在系统设置中手动启用"通知访问权限"。应用会在启动时检查并提示用户。

### 2. 前台服务 (MainForegroundService)

**文件**: `service/MainForegroundService.kt`

前台服务确保应用在后台运行时不被系统杀死。

**关键功能**:
- 创建持续通知
- 在后台稳定运行
- 支持Android 8.0+的前台服务要求
- 自动管理通知渠道

**启动方式**:
```kotlin
MainForegroundService.startService(context)
```

**停止方式**:
```kotlin
MainForegroundService.stopService(context)
```

### 3. 主Activity (MainActivity)

**文件**: `ui/MainActivity.kt`

应用的主入口，提供用户界面和功能控制。

**功能**:
- 显示服务运行状态
- 启动/停止服务按钮
- 权限申请和检查
- 导航到设置和日志页面

**权限处理**:
- 动态申请存储权限
- 检查通知访问权限
- 引导用户开启必要权限

### 4. 数据库模型 (DatabaseModels.kt)

**文件**: `data/DatabaseModels.kt`

定义了应用的数据模型和数据库操作接口。

**数据模型**:

| 模型 | 表名 | 主要字段 |
|------|------|---------|
| NotificationRecord | notifications | id, packageName, title, text, timestamp, key |
| FileRecord | files | id, fileName, filePath, fileSize, mimeType, timestamp |

**DAO接口**:
- `NotificationDao` - 通知数据操作
- `FileDao` - 文件数据操作

### 5. 文件接收Activity (FileReceiveActivity)

**文件**: `ui/FileReceiveActivity.kt`

处理通过ACTION_SEND分享的文件。

**功能**:
- 接收单个文件
- 接收多个文件
- 提取文件信息（名称、大小、类型）
- 保存文件记录到数据库

**支持的Intent Action**:
- `android.intent.action.SEND` - 单个文件
- `android.intent.action.SEND_MULTIPLE` - 多个文件

### 6. 启动广播接收器 (BootReceiver)

**文件**: `receiver/BootReceiver.kt`

在系统启动完成时自动启动前台服务。

**监听事件**:
- `android.intent.action.BOOT_COMPLETED`
- `com.htc.intent.action.QUICKBOOT_POWERON`

## 开发环境要求

| 项目 | 版本 |
|------|------|
| Android SDK | 34 |
| Minimum SDK | 24 (Android 7.0) |
| Target SDK | 34 (Android 14) |
| Gradle | 8.0.0 |
| Kotlin | 1.8.0 |
| Java | 11 |

## 依赖库

### AndroidX 核心库
- `androidx.appcompat:appcompat:1.6.1` - 向后兼容
- `androidx.core:core-ktx:1.12.0` - Kotlin扩展
- `androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0` - ViewModel
- `androidx.lifecycle:lifecycle-livedata-ktx:2.7.0` - LiveData

### 数据库
- `androidx.room:room-runtime:2.6.1` - 本地数据库
- `androidx.room:room-ktx:2.6.1` - Kotlin协程支持

### 网络和异步
- `org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3` - 协程
- `com.squareup.retrofit2:retrofit:2.9.0` - HTTP客户端
- `com.squareup.okhttp3:okhttp:4.11.0` - HTTP库

### 其他
- `com.google.code.gson:gson:2.10.1` - JSON解析
- `com.jakewharton.timber:timber:5.0.1` - 日志库
- `com.google.android.material:material:1.11.0` - Material Design

## 关键权限说明

| 权限 | 用途 | 类型 |
|------|------|------|
| INTERNET | 网络通信 | 普通 |
| READ_EXTERNAL_STORAGE | 读取文件 | 危险 |
| WRITE_EXTERNAL_STORAGE | 写入文件 | 危险 |
| BIND_NOTIFICATION_LISTENER_SERVICE | 通知监听 | 系统 |
| FOREGROUND_SERVICE | 前台服务 | 普通 |
| RECEIVE_BOOT_COMPLETED | 开机启动 | 普通 |

## 使用指南

### 安装和运行

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd LearnForwarder
   ```

2. **打开项目**
   在Android Studio中打开项目

3. **构建APK**
   ```bash
   ./gradlew assembleDebug
   ```

4. **安装到设备**
   ```bash
   ./gradlew installDebug
   ```

### 首次使用

1. **启动应用** - 点击"Start Service"按钮
2. **授予权限** - 根据提示授予必要权限
3. **启用通知访问** - 在系统设置中启用"通知访问权限"
4. **查看日志** - 点击"View Logs"查看捕获的通知

### 功能操作

| 功能 | 操作 |
|------|------|
| 启动服务 | 点击"Start Service"按钮 |
| 停止服务 | 点击"Stop Service"按钮 |
| 查看设置 | 点击"Settings"按钮 |
| 查看日志 | 点击"View Logs"按钮 |
| 清除日志 | 在日志页面点击"Clear Logs" |

## 开发建议

### 代码质量
- 使用Kotlin而不是Java
- 遵循MVVM架构模式
- 使用Coroutines处理异步操作
- 添加适当的日志记录

### 性能优化
- 使用LiveData观察数据变化
- 避免在主线程执行耗时操作
- 合理使用缓存
- 监控内存使用

### 安全性
- 使用FileProvider安全分享文件
- 加密敏感数据
- 避免硬编码密钥
- 定期更新依赖库

## 测试

### 单元测试
```bash
./gradlew test
```

### 集成测试
```bash
./gradlew connectedAndroidTest
```

## 打包和发布

### 生成Release APK

1. **生成密钥**
   ```bash
   keytool -genkey -v -keystore release.keystore \
     -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **配置签名**
   在 `build.gradle` 中添加签名配置

3. **构建Release APK**
   ```bash
   ./gradlew assembleRelease
   ```

### 代码混淆和加固

1. **启用ProGuard/R8混淆**
   ```gradle
   buildTypes {
       release {
           minifyEnabled true
           shrinkResources true
           proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
       }
   }
   ```

2. **使用360加固**
   - 下载360加固工具
   - 上传Release APK进行加固
   - 下载加固后的APK

## 常见问题

### Q: 通知访问权限如何启用？
A: 打开系统设置 → 应用 → 特殊应用权限 → 通知访问权限 → 启用Learn Forwarder

### Q: 为什么收不到通知？
A: 确保已启用通知访问权限，并且服务处于运行状态

### Q: 如何查看捕获的通知？
A: 点击主界面的"View Logs"按钮查看所有捕获的通知

### Q: 应用在后台被杀死怎么办？
A: 启用前台服务，并在系统设置中将应用加入电池优化白名单

## 参考资源

- [Android 官方文档](https://developer.android.com/)
- [Kotlin 官方文档](https://kotlinlang.org/docs/)
- [Android Architecture Components](https://developer.android.com/topic/architecture)
- [Room 数据库](https://developer.android.com/training/data-storage/room)
- [通知监听服务](https://developer.android.com/reference/android/service/notification/NotificationListenerService)

## 许可证

本项目仅供学习使用。

## 作者

Manus AI - Learn Forwarder Project

---

**最后更新**: 2025年12月30日
