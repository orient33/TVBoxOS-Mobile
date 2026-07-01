# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Clean build
./gradlew clean

# Install debug to connected device
./gradlew installDebug
```

## Project Overview

TVBoxOS-Mobile (MBox) — Android TV/移动端视频聚合播放器，基于 TVBox 开源项目。支持通过订阅源配置多个视频站点，使用 Spider 插件架构动态加载视频源。

## Architecture

### Module Structure (8 modules)

- **app**: 主应用模块
- **player**: 播放器封装（IJK、ExoPlayer）
- **quickjs**: QuickJS JavaScript 引擎，用于 JS Spider 执行
- **catvod**: 基础模块，包含 Spider 基类、Proxy 工具类
- **chaquo**: Python 解析模块，使用 Chaquopy 运行 Python Spider
- **TabLayout**: 自定义 Tab 组件
- **ViewPager1Delegate**: ViewPager 代理
- **crash**: 崩溃处理模块（CustomActivityOnCrash）

### Core Package: `com.github.tvbox.osc`

| Package | 职责 |
|---------|------|
| `base` | App、BaseActivity、BaseFragment |
| `ui/activity` | 12 个 Activity（Splash、Main、Detail、Live、Setting 等） |
| `ui/fragment` | HomeFragment、GridFragment 等 |
| `api` | ApiConfig 单例，管理订阅源配置与数据获取 |
| `bean` | 29 个数据模型类 |
| `player` | 播放器实现与控制器（VodController、LiveController） |
| `viewmodel` | SourceViewModel、SubtitleViewModel |
| `data` | Room 数据库（tvbox.v3.db） |
| `server` | 内嵌 NanoHTTPD Web 服务器，用于远程控制 |
| `cache` | 数据缓存层 |

### Supporting Packages

- `com.github.catvod.crawler` — Spider 抽象类与插件加载系统
- `com.github.catvod.net` — 网络工具
- `okhttp3.dnsoverhttps` — DNS-over-HTTPS 实现

### Key Patterns

- **Spider 插件架构**: 抽象 `Spider` 类支持三种加载方式，运行时无需重编译
  - JAR: `JarLoader` + DexClassLoader（.jar 文件）
  - JS: `JsLoader` + QuickJS（.js 文件）
  - Python: `PyLoader` + Chaquopy（.py 文件，Python 3.12）
- **MVVM**: ViewModel + LiveData，通过 EventBus 跨组件通信
- **单例**: ApiConfig 管理全局视频源配置，AppDataManager 管理数据访问
- **Room 数据库**: 实体包括 Cache、VodRecord（观看历史）、VodCollect（收藏）

### Key Dependencies

- **播放器**: ExoPlayer 2.18.7、IJK Player、DKPlayer UI
- **网络**: OkHttp 3.12.11、OkGo 3.0.4、NanoHTTPD 2.3.1
- **数据**: Gson 2.8.7、Room 2.3.0、Hawk 2.0.1、Jsoup 1.14.1
- **UI**: Material 1.4.0、XPopup 2.10.0、Lottie 5.2.0、TV RecyclerView 3.0.0
- **Python**: Chaquopy 17.0.0、Python 3.12、lxml、pyquery、requests、pycryptodome、beautifulsoup4
- **特殊**: DLNA-Cast（投屏）、ZXing（二维码）、Conscrypt（TLS）、Thunder.jar（迅雷协议）

## Build Configuration

- **AGP**: 7.2.2, **Kotlin**: 1.5.31
- **SDK**: minSdk=24, targetSdk=30, compileSdk=33
- **NDK ABI**: 仅 arm64-v8a
- **签名**: debug/release 共用 TVBoxOSC.jks
- **ViewBinding**: 已启用
- **MultiDex**: 已启用
- **仓库**: Aliyun 镜像（国内优化）

## Testing

当前项目无测试代码（无 src/test、src/androidTest 目录，无测试依赖）。

## CI/CD

GitHub Actions workflow (`.github/workflows/test.yml`): 手动触发构建 release APK 并上传 artifact。
