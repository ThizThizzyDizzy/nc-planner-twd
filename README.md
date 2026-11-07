# Nuclearcraft Planner TWD Edition — native Android draft

This repository hosts the Java Android app. Plannerator remains the sole source of planner logic, menus, configurations, textures, and file formats. DizzyEngine supplies the renderer and UI; Android supplies a GL surface and platform adapters. No Codename One, ads, analytics, WebView, or custom native binaries are included.

## Build

Requirements: JDK 21, Android SDK platform 36 and build-tools 35.0.0, and the three authoritative source checkouts. This first draft requires Android 15 / API 35 and OpenGL ES 3.0.

By default the checkouts are `$HOME/NetBeansProjects/NuclearCraft Plannerator`, `$HOME/NetBeansProjects/Dizzy-Engine`, and `$HOME/NetBeansProjects/NCPF`. Set `-PplanneratorDir=...`, `-PdizzyEngineDir=...`, and `-PncpfDir=...` for different paths.

```bash
export JAVA_HOME=/path/to/jdk-21
export ANDROID_HOME=/path/to/android-sdk
./build-android.sh :app:assembleDebug
```

The script downloads Gradle 8.13 into `$HOME/.cache/plannerator-android` if needed. Build outputs and project caches are outside this repository. The default APK is `$HOME/.cache/plannerator-android/build/app/outputs/apk/debug/app-debug.apk`; use `-PandroidBuildDir=...` to change the output root.

To test on your phone, enable Developer options → USB debugging, connect a data-capable USB cable, and accept the phone's debugging authorization prompt. Then run:

```bash
./debug-android.sh
```

This builds a fresh debug APK, installs it, and launches the app. It uses explicit tool paths without changing `PATH`; existing project tools in `$HOME/.cache/plannerator-android/tools` are discovered automatically. Set `ANDROID_HOME` and `JAVA_HOME` to use other SDK/JDK installations, or `ADB` to select an explicit ADB executable. With multiple connected devices, use `./debug-android.sh --serial DEVICE_SERIAL`. Additional Gradle options, such as source checkout paths, can follow the serial option. Run `./debug-android.sh --help` for details.

Updating the main source checkouts and rerunning the same command builds the updated app. There is no planner source copy or desktop build prerequisite. The existing NetBeans/Ant build files are unchanged.

The shared Plannerator checkout must include commit `7672d7476a1e2cbad832107dca23555802bb971e` (or its equivalent): ordinary Save/Export actions use `Core.storageFile` to write into Android's private files directory, recovery saves capture current designs, and the export confirmation hides the desktop-only Open Folder action on Android. Use Save Dialog to export to a user-selected document provider. The Android activity saves recovery data and preferences on the GL thread before pausing its surface.

Run `./build-android.sh :engine:contextRecoveryTest --offline` for the mock-GL regression test covering repeated context replacement, texture caches, glyph buffers, shader uniforms, and preservation of layers and cleanup hooks.

Run `./build-android.sh :planner:activityRecreationTest --offline` for the separate mock-GL regression test covering replacement hosts sharing one process session. It uses distinct render threads and checks that planner initialization runs once, the existing layer/project survives, and Core dispatches queued UI actions on the replacement thread; it does not run Android framework lifecycle callbacks. Activity instances reuse this process session and restore graphics when their new surface is created.

## Draft scope

The app loads the standard NuclearCraft configuration and the existing menus/editor, with a single touch mapped to the left mouse button. Optional modpack configurations are skipped during Android startup. UI redesign, touch gestures, software keyboard integration, VR, Discord bot mode, desktop updates, and DSSL native interop are outside this draft. The Android document picker adapts imported/exported files through private cache files. When the EGL context is recreated, the host rebuilds renderer buffers, cached textures, and font glyph buffers while retaining the open project and menus. Physical-device verification of context loss and activity recreation is still required.

## Existing-app update identity

`release-metadata.properties` preserves the verified application ID, display name, previous version name, and signing alias from the Windows project `D:\CodenameOne\NCPlannerTWD`. The original settings and verified `Keychain.ks` backup were retrieved and saved outside Git in `$HOME/.local/share/plannerator-android-release`, with private filesystem permissions. They are required to sign an update with the existing certificate. Never commit these files or credentials.

Debug installs use `net.ncplanner.plannerator.debug` and the ordinary Android debug key, so they do not replace the released app. Release installs use the original `net.ncplanner.plannerator` identity. The Android app version name is set in `release-metadata.properties`; the embedded planner engine keeps its authoritative Plannerator version.

The original settings record version `1.09` but no Android versionCode. Read the highest existing versionCode in Play Console and explicitly provide the next one. The build rejects release packaging without it and the original signing files.

```bash
./build-android.sh :app:assembleRelease -PreleaseVersionCode=NEXT_CODE
```

Use `-PreleaseSigningProperties=/private/path/codenameone_settings.properties` and `-PreleaseKeystore=/private/path/Keychain.ks` to relocate the private release files. The old Codename One build hints are not used by this app.

## Shared platform boundary

The shared Plannerator checkout must also include commit `e6deee34` (or its equivalent), providing `Core.bindHostedUIThread()` to rebind UI dispatch when the retained session moves to a replacement surface thread.

The shared DizzyEngine checkout must include `DizzyEngine.restoreHostedGraphics`, `ResourceManager.invalidateContextTextures`, `Renderer.restoreElements`, and `Shader.restoreContext` for context-loss recovery.

DizzyEngine's existing public desktop APIs and LWJGL defaults remain available. The Android host explicitly installs the GL, polled-input, image-codec, and font backends, then uses the hosted rendering entry points. Plannerator's optional host supplies document selection, URL opening, closing, storage paths, and the choice to skip optional configurations. JVM class scanning remains the desktop default; the Android build derives registration lists from the existing annotations for Plannerator and NCPF. Shrinker rules preserve reflective model constructors, fields, annotations, and generic signatures.

Runtime dependencies are existing planner libraries: JOML, Gson, and DSSL. LWJGL, ClassGraph, and Discord integrations are compile-only desktop dependencies; no desktop native libraries are packaged.

## Release packaging

Run `./release-android.sh NEXT_UNUSED_VERSION_CODE` after checking the highest code
in Play Console (including testing tracks). The script discovers the cached JDK 21
and Android SDK, builds and lints the optimized release, and copies a signed AAB,
signed universal APK, R8 mapping, and SHA-256 checksums outside Git to
`~/.local/share/plannerator-android-release/releases/VERSION_CODE`.
Override that destination with `PLANNERATOR_RELEASE_DIR`. Do not reuse version codes.
The target SDK is 36; the minimum remains Android 15 / API 35.

Upload the `.aab` to Play Console. Keep `mapping.txt` for crash deobfuscation.
Publish the `.apk` as a GitHub release asset for direct installation and Obtainium;
use a versioned release tag and increasing versionCode on every update. Both
artifacts use the preserved release certificate. If Play App Signing uses a
different app signing certificate, Play installs and direct APK installs cannot
update each other without uninstalling. Verify that certificate in Play Console.
No release is published automatically by these scripts.

### F-Droid source-build route

F-Droid's official catalog builds from reviewed public source; uploading this APK
alone is not a submission. An unsigned release can be built without private keys:

```bash
./build-android.sh :app:assembleRelease -PunsignedRelease=true -PreleaseVersionCode=NEXT_CODE
```

Before submitting, pin public commits for this repository, Plannerator,
DizzyEngine, and NCPF, and supply those checkouts using the documented Gradle
properties. The current build also consumes local JARs (including DSSL) from
those checkouts: replace prebuilt inputs with source builds or acceptable declared
build dependencies and audit their licenses before claiming F-Droid readiness.
F-Droid maintainers must review the resulting recipe. Their normal signing key
will differ from the direct-release key unless reproducible developer signing is
arranged. See https://f-droid.org/docs/Submitting_to_F-Droid_Quick_Start_Guide/.

Packaging validation does not resolve the draft limitations listed above. Test
editing, import/export, background/resume, activity recreation and GL context
loss on a physical device before promoting a release to production.
