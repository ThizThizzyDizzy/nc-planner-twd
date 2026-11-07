#!/usr/bin/env bash
set -euo pipefail
repo_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cache_dir="${PLANNERATOR_ANDROID_CACHE:-${XDG_CACHE_HOME:-$HOME/.cache}/plannerator-android}"
gradle_version=8.13
mkdir -p "$cache_dir"
if [[ ! -x "$cache_dir/gradle-$gradle_version/bin/gradle" ]]; then
    curl --fail --location "https://services.gradle.org/distributions/gradle-$gradle_version-bin.zip" --output "$cache_dir/gradle.zip"
    unzip -q "$cache_dir/gradle.zip" -d "$cache_dir"
    python3 -c 'import pathlib,sys; pathlib.Path(sys.argv[1]).unlink()' "$cache_dir/gradle.zip"
fi
exec "$cache_dir/gradle-$gradle_version/bin/gradle" --project-dir "$repo_dir" --project-cache-dir "$cache_dir/project-cache" "${@:-:app:assembleDebug}"
