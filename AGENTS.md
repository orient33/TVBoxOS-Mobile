# AGENTS.md

Quick-reference for AI agents working in this repo. Read this before touching code.

## Build & Run

```bash
./gradlew assembleDebug          # Debug APK → app/build/outputs/apk/debug/
./gradlew assembleRelease        # Release APK → app/build/outputs/apk/release/
./gradlew installDebug           # Install debug to device
./gradlew clean                  # Nuke build/
```

- **JDK 21 required** (gradle-daemon-jvm.properties enforces JetBrains 21).
- **Gradle 8.13**, AGP 8.13.2, Kotlin 1.9.24 — do NOT trust older version numbers you may see elsewhere.
- First build downloads Chaquopy Python SDK (~200 MB); subsequent builds are faster.
- Release builds have `minifyEnabled false` — ProGuard rules exist but are not applied.

## Module Map (8 modules)

```
app              → Main application (com.github.tvbox.osc)
├── player       → ExoPlayer 2.18.7 + IJK + DKPlayer UI wrappers
├── quickjs      → QuickJS engine for JS Spider execution
├── catvod       → Spider base class, JarLoader, JsLoader, PyLoader, OkHttp utils
├── chaquo       → Python Spider via Chaquopy (Python 3.12, pip: lxml/pyquery/requests/etc.)
├── TabLayout    → Custom tab component (com.angcyo.tablayout)
├── ViewPager1Delegate → ViewPager1 adapter for TabLayout
└── crash        → CustomActivityOnCrash (cat.ereza.customactivityoncrash)
```

Dependency direction: `app → player, quickjs, catvod, chaquo, TabLayout, ViewPager1Delegate, crash`. `chaquo → catvod`. `ViewPager1Delegate → TabLayout`.

## Critical Architecture Facts

- **Spider plugin system** is the core abstraction. Three loader types:
  - `JarLoader` (DexClassLoader, .jar) — in `app/src/main/java/com/github/catvod/crawler/`
  - `JsLoader` (QuickJS bridge, .js) — same package
  - `PyLoader` (Chaquopy, .py) — same package
  All implement `com.github.catvod.crawler.Spider`. New source types MUST extend this class.

- **ApiConfig** singleton (`com.github.tvbox.osc.api.ApiConfig`) manages all subscription sources. Most data flows start here.

- **EventBus** (greenrobot) is the primary cross-component communication mechanism — NOT LiveData observers between activities. Search for `@Subscribe` annotations to trace event flow.

- **Room database** (`tvbox.v3.db`): entities are Cache, VodRecord (watch history), VodCollect (favorites). Access via `AppDataManager` → `RoomDataManger`.

- **Embedded web server** (`com.github.tvbox.osc.server.RemoteServer`, NanoHTTPD) provides remote control API — do not break its request handlers.

- **AndroidAutoSize** sets design dimensions 360×820dp — UI layout is phone-optimized, not TV-first despite the project name.

## Gotchas

- **No tests.** No `src/test/`, no `src/androidTest/`, no test dependencies. Verify changes by building, not by running tests.
- **NDK ABI is arm64-v8a only.** Adding other ABIs requires changes in both `app/build.gradle` and `chaquo/build.gradle` ndk.abiFilters.
- **Sign keystore** (`TVBoxOSC.jks`) is referenced but `.gitignore` excludes `*.jks`. Release builds will fail without it. Debug builds use the default debug keystore.
- **DLNA-Cast is commented out** in `app/build.gradle` — the `RemoteTVBox` and `DLNACastService` classes exist but the dependency is disabled. Do not add DLNA code without re-enabling the dependency.
- **`android.nonTransitiveRClass=false`** in gradle.properties — R class references are transitive. Changing this will break the crash module.
- **Local JARs** in `app/libs/`: `thunder.jar` (Xunlei/Thunder download), `commons-lang3-3.12.0.jar`, `dec-0.1.2.jar`. These are not in any Maven repo — do not attempt to replace with Gradle dependencies without verifying availability.
- **Chaquopy Python source** lives at `chaquo/src/main/python/` (not the default `src/main/python/` — it's overridden in build.gradle sourceSets). Requirements are in both `chaquo/build.gradle` pip block AND `chaquo/requirements.txt` — keep them in sync.

## Code Style

- Java 11 source/target (not Kotlin despite the Kotlin plugin). Most code is Java.
- ViewBinding enabled — activities use generated binding classes, not `findViewById`.
- Chinese comments are common and should be preserved.
- ProGuard keep rules in `app/proguard-rules.pro` are extensive and must be updated when adding new serializable/spider classes.

## CI

GitHub Actions (`.github/workflows/test.yml`): manual `workflow_dispatch` only. Runs `./gradlew assemblerelease --build-cache --parallel --daemon --warning-mode all`.
