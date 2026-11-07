#!/usr/bin/env bash
set -euo pipefail
repo_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
if [[ ! "${1:-}" =~ ^[1-9][0-9]*$ ]] || (( $1 > 2100000000 )); then
    echo 'Usage: ./release-android.sh NEXT_UNUSED_VERSION_CODE [Gradle options]' >&2
    exit 1
fi
version_code="$1"
shift
cache_dir="${PLANNERATOR_ANDROID_CACHE:-${XDG_CACHE_HOME:-$HOME/.cache}/plannerator-android}"
tools_dir="$cache_dir/tools"
export ANDROID_HOME="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-$tools_dir/sdk}}"
if [[ -z "${JAVA_HOME:-}" ]]; then
    for jdk in "$tools_dir"/jdk-21*; do
        if [[ -x "$jdk/bin/java" ]]; then export JAVA_HOME="$jdk"; break; fi
    done
fi
output_dir="${PLANNERATOR_RELEASE_DIR:-$HOME/.local/share/plannerator-android-release/releases/$version_code}"
mkdir -p "$output_dir"
# All caches, reports, and intermediates stay outside the source checkout.
"$repo_dir/build-android.sh" :app:assembleRelease :app:bundleRelease :app:lintRelease "$@" \
    "-PreleaseVersionCode=$version_code" -PunsignedRelease=false "-PandroidBuildDir=$output_dir/build"
cp "$output_dir/build/app/outputs/apk/release/app-release.apk" "$output_dir/plannerator-$version_code.apk"
cp "$output_dir/build/app/outputs/bundle/release/app-release.aab" "$output_dir/plannerator-$version_code.aab"
cp "$output_dir/build/app/outputs/mapping/release/mapping.txt" "$output_dir/mapping.txt"
(cd "$output_dir" && sha256sum "plannerator-$version_code.apk" "plannerator-$version_code.aab" mapping.txt > SHA256SUMS)
echo "Release artifacts: $output_dir"
