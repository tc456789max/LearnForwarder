# Learn Forwarder - Android 学习项目

一个基于"转阅助手"应用原理的Android学习项目，展示了现代Android开发的最佳实践。

## 📱 功能特性

- **通知监听** - 实时捕获系统通知栏消息
- **文件分享** - 接收并处理通过Intent分享的文件
- **后台服务** - 前台服务确保应用稳定运行
- **本地存储** - 使用Room数据库存储所有记录
- **现代架构** - MVVM + Repository + Coroutines
- **权限管理** - 完善的权限申请和处理机制

## 🏗️ 项目架构

```
┌─────────────────────────────────────┐
│         UI Layer (Activities)       │
├─────────────────────────────────────┤
│    ViewModel + LiveData/StateFlow   │
├─────────────────────────────────────┤
│      Repository (数据管理)          │
├─────────────────────────────────────┤
│   Local DB (Room) + Remote API      │
├─────────────────────────────────────┤
│    Services (后台任务)              │
└─────────────────────────────────────┘
```

## 📋 项目结构

```
LearnForwarder/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/learnforwarder/
│   │   │   ├── ui/                    # UI层
│   │   │   ├── service/               # 服务层
│   │   │   ├── receiver/              # 广播接收器
│   │   │   ├── data/                  # 数据层
│   │   │   └── App.kt                 # Application类
│   │   ├── res/
│   │   │   ├── layout/                # 布局文件
│   │   │   ├── values/                # 资源文件
│   │   │   └── xml/                   # XML配置
│   │   └── AndroidManifest.xml
│   └── build.gradle
└── build.gradle
```

## 🚀 快速开始

### 环境要求

- Android Studio Giraffe 或更高版本
- Android SDK 34
- Minimum SDK 24 (Android 7.0)
- Java 11 或更高版本

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd LearnForwarder
   ```

2. **打开项目**
   在Android Studio中打开项目

3. **构建项目**
   ```bash
   ./gradlew build
   ```

4. **运行应用**
   ```bash
   ./gradlew installDebug
   ```

## 📖 使用指南

### 首次启动

1. 启动应用后，点击"Start Service"按钮
2. 根据提示授予必要的权限
3. 在系统设置中启用"通知访问权限"
4. 服务将在后台运行并监听通知

### 主要功能

| 功能 | 说明 |
|------|------|
| Start Service | 启动后台服务 |
| Stop Service | 停止后台服务 |
| Settings | 打开设置页面 |
| View Logs | 查看捕获的通知日志 |

## 🔧 技术栈

### 核心库
- **Kotlin** - 编程语言
- **AndroidX** - Android支持库
- **Lifecycle** - 生命周期管理
- **Room** - 本地数据库
- **Coroutines** - 异步编程

### 网络和工具
- **Retrofit** - HTTP客户端
- **OkHttp** - HTTP库
- **GSON** - JSON解析
- **Timber** - 日志库

## 📚 学习要点

通过本项目，您可以学习到：

1. **系统服务开发**
   - NotificationListenerService 实现
   - 前台服务 (ForegroundService)
   - 广播接收器 (BroadcastReceiver)

2. **数据管理**
   - Room数据库设计和使用
   - DAO模式实现
   - 数据库迁移

3. **异步编程**
   - Kotlin Coroutines
   - LiveData观察
   - 后台任务处理

4. **权限管理**
   - 动态权限申请
   - 权限检查和处理
   - 系统权限配置

5. **应用架构**
   - MVVM模式
   - Repository模式
   - 依赖注入

## 🔐 权限说明

| 权限 | 用途 |
|------|------|
| INTERNET | 网络通信 |
| READ_EXTERNAL_STORAGE | 读取文件 |
| WRITE_EXTERNAL_STORAGE | 写入文件 |
| BIND_NOTIFICATION_LISTENER_SERVICE | 通知监听 |
| FOREGROUND_SERVICE | 前台服务 |
| RECEIVE_BOOT_COMPLETED | 开机启动 |

## 🛠️ 开发建议

### 代码质量
- 遵循Kotlin编码规范
- 使用MVVM架构模式
- 编写单元测试
- 添加适当的日志记录

### 性能优化
- 使用协程处理异步操作
- 避免在主线程执行耗时操作
- 合理使用缓存
- 监控内存使用

### 安全性
- 使用FileProvider安全分享文件
- 加密敏感数据
- 定期更新依赖库
- 使用ProGuard进行代码混淆

## 📦 打包发布

### 生成Release APK

```bash
# 生成签名密钥
keytool -genkey -v -keystore release.keystore \
  -keyalg RSA -keysize 2048 -validity 10000

# 构建Release APK
./gradlew assembleRelease
```

### 代码加固

推荐使用360加固进行代码保护：
1. 访问 https://jiagu.360.cn
2. 上传Release APK
3. 下载加固后的APK

## 🐛 常见问题

### Q: 如何启用通知访问权限？
A: 打开系统设置 → 应用 → 特殊应用权限 → 通知访问权限 → 启用Learn Forwarder

### Q: 为什么收不到通知？
A: 确保已启用通知访问权限，并且服务处于运行状态

### Q: 应用在后台被杀死怎么办？
A: 将应用加入电池优化白名单，或在系统设置中禁用电池优化

## 📝 文件说明

- `PROJECT_GUIDE.md` - 详细的项目开发指南
- `dev_plan.md` - 应用架构和功能规划
- `android_tech_research.md` - Android技术研究总结
- `proguard-rules.pro` - ProGuard混淆规则

## 📖 参考资源

- [Android 官方文档](https://developer.android.com/)
- [Kotlin 官方文档](https://kotlinlang.org/docs/)
- [Android Architecture Components](https://developer.android.com/topic/architecture)
- [Room 数据库](https://developer.android.com/training/data-storage/room)
- [NotificationListenerService](https://developer.android.com/reference/android/service/notification/NotificationListenerService)

## 📄 许可证

本项目仅供学习使用。

## 👨‍💻 作者

**Manus AI** - Learn Forwarder Project

---

**最后更新**: 2025年12月30日

**项目状态**: ✅ 完成

如有问题或建议，欢迎提出Issue或Pull Request。
