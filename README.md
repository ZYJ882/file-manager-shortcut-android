# 文件管理快捷入口

**文件管理快捷入口**是一个极简 Android 应用。点击桌面图标后，它会立即请求 Android 的系统文档浏览界面，让用户使用设备自带的文件浏览器查看本地文件、下载内容、SD 卡和已接入的文档提供方。

该项目不是自定义文件管理器，不会扫描、读取、复制、上传或保存任何用户文件。它不声明网络权限或存储权限；文件浏览、操作范围与访问授权均由 Android 系统界面和用户决定。[1]

| 项目 | 值 |
|---|---|
| 应用 ID | `app.auralis.filelauncher` |
| 版本 | `1.0.0` |
| 最低 Android 版本 | Android 8.0（API 26） |
| 目标 Android 版本 | Android 15（API 35） |
| 许可证 | MIT |

## 工作方式

应用首先发起 `ACTION_OPEN_DOCUMENT`，以打开 Android 系统控制的文档选择界面，并允许浏览任意文件类型。如果设备没有响应该动作的组件，应用会回退到 `ACTION_GET_CONTENT`。Android 的存储访问框架提供统一的系统界面，用于浏览本地与已注册文档提供方中的文件。[1]

> Android 没有为第三方应用提供一个可跨所有厂商设备直接进入“完整文件管理器主页”的统一公开接口。因此，本项目使用 Android 官方的系统文件浏览入口；具体界面会因设备系统和厂商文件管理器而异。

## 下载与安装

已构建的安装包位于 [`releases/v1.0.0/FileManagerShortcut-v1.0.0-debug.apk`](releases/v1.0.0/FileManagerShortcut-v1.0.0-debug.apk)。这是一个以 Android 调试证书签名的 APK，适合个人安装与测试；在设备提示时，允许对应来源安装未知应用即可。

| 文件 | SHA-256 |
|---|---|
| `FileManagerShortcut-v1.0.0-debug.apk` | `5d9c21a262053f9bcdbbedc0d8f8493f26e342782b810d1fcf632e33f241fffd` |

正式分发或上架应用商店前，请使用自己的发布签名密钥构建并签名，不要使用仓库内的调试 APK。

## 本地构建

项目使用 Gradle 8.10.2、Android Gradle Plugin 8.7.3、Kotlin 2.0.21 和 JDK 17。安装 Android SDK Platform 35 与相应构建工具后，在项目根目录执行：

```bash
./gradlew assembleDebug
```

生成的 APK 位于 `app/build/outputs/apk/debug/app-debug.apk`。如果 Gradle 无法定位 SDK，请创建未被提交的 `local.properties` 文件：

```properties
sdk.dir=/absolute/path/to/Android/Sdk
```

## 仓库结构

| 路径 | 内容 |
|---|---|
| `app/src/main/java/` | 唯一的快捷入口 Activity |
| `app/src/main/res/` | 启动器图标与主题资源 |
| `releases/v1.0.0/` | 已验证的调试 APK 与 SHA-256 校验文件 |
| `.github/workflows/` | GitHub Actions 调试构建工作流 |

## 许可证

本项目以 [MIT License](LICENSE) 发布。

## 参考资料

[1]: https://developer.android.com/training/data-storage/shared/documents-files "Android Developers：通过存储访问框架访问文档和其他文件"
