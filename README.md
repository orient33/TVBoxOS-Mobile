# TVBoxMobile

基于
* [q215613905](https://github.com/q215613905)/[TVBoxOS](https://github.com/q215613905/TVBoxOS)

本仓库以 `origin/main` 为上游基线，当前 fork 在此基础上增加了构建升级和 Python 解析支持相关修改。

## Build

[Github Actions](https://github.com/XiaoRanLiu3119/MBox-Build/actions)

本地构建：

```bash
./gradlew assembleDebug
```

构建环境与版本：

- JDK 21：`gradle/gradle-daemon-jvm.properties` 已指定 JetBrains 21。
- Gradle Wrapper 8.13。
- Android Gradle Plugin 8.13.2。
- Kotlin 1.9.24。
- `compileSdk 33`，`minSdk 24`，`targetSdk 30`。
- 当前仅打包 `arm64-v8a` ABI。

注意：

- 首次构建会下载 Chaquopy / Python 相关依赖，耗时会更长。
- Release 构建引用 `TVBoxOSC.jks`，仓库未包含该 keystore；没有本地签名文件时请先使用 Debug 构建。

## 主要依赖

工程模块：

- `app`：主应用模块。
- `player`：ExoPlayer / IJK / DKPlayer 播放封装。
- `quickjs`：JS Spider 执行引擎。
- `catvod`：Spider 基类、空实现和代理基础能力。
- `chaquo`：Python Spider 运行时封装。
- `TabLayout`、`ViewPager1Delegate`、`crash`：UI 组件和崩溃页模块。

核心三方库：

- AndroidX、Material、AppCompat、ConstraintLayout、RecyclerView、Room、MultiDex。
- OkHttp、OkGo、Okio、Gson、Picasso、Glide、Jsoup、NanoHTTPD、EventBus、Hawk。
- AndroidAutoSize、XXPermissions、TitleBar、XPopup、ImmersionBar、UtilCodeX、ShadowLayout 等 UI / 工具库。
- 本地 JAR：`app/libs/thunder.jar`、`app/libs/commons-lang3-3.12.0.jar`、`app/libs/dec-0.1.2.jar`。

Python 解析依赖：

- Chaquopy Gradle Plugin 17.0.0。
- Python 3.12。
- `lxml`、`pyquery`、`requests`、`cachetools`、`pycryptodome`、`beautifulsoup4`。

## 本 fork 的主要修改

相对 `origin/main` 增加了 2 个提交：

1. `update jdk. AGP. version. fix build`
   - 升级 Gradle Wrapper、AGP、Kotlin 和 JDK 配置。
   - 为 Android 模块补充 `namespace` 等新版 AGP 所需配置。
   - 修复 Debug 签名、Java 11 编译参数、desugar、lint 等构建配置。
   - 因 JitPack 解析问题临时禁用 DLNA-Cast 依赖，并调整相关投屏列表代码。

2. `feat: 添加 Python 解析支持 (Chaquopy)`
   - 新增 `catvod` 模块，抽出 `Spider` 基类并补充 `SpiderNull`、`Proxy`。
   - 新增 `chaquo` 模块，通过 Chaquopy 运行 Python Spider。
   - 新增 `PyLoader`，支持加载、初始化、缓存 `.py` Spider。
   - 更新 `ApiConfig#getCSP()`，按扩展名支持 `.py` 源。
   - 新增 `UriUtil`，用于资源路径和 URI 处理。

精力有限，未必会及时维护，仅用于学习。

## 推荐使用

[takagen99](https://github.com/takagen99/Box)
[FongMi](https://github.com/FongMi/TV)
