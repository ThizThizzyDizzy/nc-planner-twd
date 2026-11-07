#!/usr/bin/env bash
set -euo pipefail

repo_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cache_dir="${PLANNERATOR_ANDROID_CACHE:-${XDG_CACHE_HOME:-$HOME/.cache}/plannerator-android}"
tools_dir="$cache_dir/tools"
serial="${ANDROID_SERIAL:-}"

if [[ "${1:-}" == --help || "${1:-}" == -h ]]; then
    cat <<'EOF'
Usage: ./debug-android.sh [--serial DEVICE] [Gradle options]

Build, install, and launch a fresh debug APK on a connected Android device.
Enable USB debugging on your phone and accept its authorization prompt first.
Use --serial when multiple devices are connected. Requires Android 15 or newer.

Uses ANDROID_HOME (or ANDROID_SDK_ROOT), JAVA_HOME, and ADB when supplied;
otherwise uses the existing project tools under the Plannerator cache.
Does not change PATH. Additional Gradle options are passed to the build.
EOF
    exit 0
fi
if [[ "${1:-}" == --serial ]]; then
    if [[ -z "${2:-}" ]]; then
        echo 'Missing device serial after --serial.' >&2
        exit 1
    fi
    serial="$2"
    shift 2
fi

export ANDROID_HOME="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-$tools_dir/sdk}}"
adb="${ADB:-$ANDROID_HOME/platform-tools/adb}"
if [[ ! -x "$adb" ]]; then
    echo "ADB not found at $adb. Set ANDROID_HOME to your Android SDK or ADB to its executable." >&2
    exit 1
fi
device_args=()
if [[ -n "$serial" ]]; then
    device_args=(-s "$serial")
fi
if ! "$adb" "${device_args[@]}" get-state; then
    "$adb" devices -l
    echo 'Connect your phone, enable USB debugging, and accept its authorization prompt. Use --serial DEVICE if multiple devices are connected.' >&2
    exit 1
fi
api_level="$("$adb" "${device_args[@]}" shell getprop ro.build.version.sdk | tr -d '\r')"
if [[ ! "$api_level" =~ ^[0-9]+$ ]] || (( api_level < 35 )); then
    echo "This app requires Android 15 / API 35 or newer (device API: $api_level)." >&2
    exit 1
fi

if [[ -z "${JAVA_HOME:-}" ]]; then
    for jdk in "$tools_dir"/jdk-21*; do
        if [[ -x "$jdk/bin/java" ]]; then
            export JAVA_HOME="$jdk"
            break
        fi
    done
fi
if [[ -z "${JAVA_HOME:-}" || ! -x "$JAVA_HOME/bin/java" ]]; then
    echo 'Set JAVA_HOME to a JDK 21 installation.' >&2
    exit 1
fi

output_dir="$cache_dir/build"
if [[ -x "$tools_dir/gradle-8.13/bin/gradle" ]]; then
    PLANNERATOR_ANDROID_CACHE="$tools_dir" "$repo_dir/build-android.sh" :app:assembleDebug "$@" "-PandroidBuildDir=$output_dir"
else
    "$repo_dir/build-android.sh" :app:assembleDebug "$@" "-PandroidBuildDir=$output_dir"
fi
"$adb" "${device_args[@]}" install -r "$output_dir/app/outputs/apk/debug/app-debug.apk"
"$adb" "${device_args[@]}" shell am start -W -n net.ncplanner.plannerator.debug/net.ncplanner.plannerator.android.PlanneratorActivity
