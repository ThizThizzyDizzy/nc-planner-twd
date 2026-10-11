# Nuclearcraft Planner TWD Edition — native Android draft

This repository hosts the Java Android app. Plannerator remains the sole source of planner logic, menus, configurations, textures, and file formats. DizzyEngine supplies the renderer and UI; Android supplies a GL surface and platform adapters. No Codename One, ads, analytics, WebView, or custom native binaries are included.

## Build

Requirements: JDK 21, Android SDK platform 35 and build-tools 35.0.0, and the three authoritative source checkouts. This first draft requires Android 15 / API 35 and OpenGL ES 3.0.

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

## Draft scope

The app loads the standard NuclearCraft configuration and the existing menus/editor, with a single touch mapped to the left mouse button. Optional modpack configurations are skipped during Android startup. UI redesign, touch gestures, software keyboard integration, VR, Discord bot mode, desktop updates, and DSSL native interop are outside this draft. The Android document picker adapts imported/exported files through private cache files. Pause/resume with a preserved GL context is verified; recovery after GL context loss or activity recreation still needs lifecycle work before production release.

## Existing-app update identity

`release-metadata.properties` preserves the verified application ID, display name, previous version name, and signing alias from the Windows project `D:\CodenameOne\NCPlannerTWD`. The original settings and verified `Keychain.ks` backup were retrieved and saved outside Git in `$HOME/.local/share/plannerator-android-release`, with private filesystem permissions. They are required to sign an update with the existing certificate. Never commit these files or credentials.

Debug installs use `net.ncplanner.plannerator.debug` and the ordinary Android debug key, so they do not replace the released app. Release installs use the original `net.ncplanner.plannerator` identity. The app version name comes from the authoritative Plannerator properties.

The original settings record version `1.09` but no Android versionCode. Read the highest existing versionCode in Play Console and explicitly provide the next one. The build rejects release packaging without it and the original signing files.

```bash
./build-android.sh :app:assembleRelease -PreleaseVersionCode=NEXT_CODE
```

Use `-PreleaseSigningProperties=/private/path/codenameone_settings.properties` and `-PreleaseKeystore=/private/path/Keychain.ks` to relocate the private release files. The old Codename One build hints are not used by this app.

## Shared platform boundary

DizzyEngine's existing public desktop APIs and LWJGL defaults remain available. The Android host explicitly installs the GL, polled-input, image-codec, and font backends, then uses the hosted rendering entry points. Plannerator's optional host supplies document selection, URL opening, closing, storage paths, and the choice to skip optional configurations. JVM class scanning remains the desktop default; the Android build derives registration lists from the existing annotations for Plannerator and NCPF. Shrinker rules preserve reflective model constructors, fields, annotations, and generic signatures.

Runtime dependencies are existing planner libraries: JOML, Gson, and DSSL. LWJGL, ClassGraph, and Discord integrations are compile-only desktop dependencies; no desktop native libraries are packaged.
