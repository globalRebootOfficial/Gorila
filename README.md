# Gorilla Android

ARM64 Android diagnostic shell: Kotlin UI + C++20 JNI + CMake. It reports its own
process status and checks whether the Android overlay permission is granted.
It does not attach to, read, or modify another process; it does not synthesize input.

## Requirements
- Android Studio with Android SDK 34
- JDK 17
- Android NDK (r26 or newer)
- CMake 3.22.1

## Build
Open this directory in Android Studio and let Gradle sync, then run:

```sh
./gradlew :app:assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`.

The Gradle wrapper is not bundled; generate it with a local Gradle installation
using `gradle wrapper` or open the project in Android Studio.

## Overlay permission
The app exposes a button to open Android's overlay permission settings. The sample
overlay is a non-interactive `TYPE_APPLICATION_OVERLAY` window with a `SurfaceView`.
It draws only a local diagnostic label.

## Notes
The manifest uses target SDK 34 and min SDK 24. The app is restricted to arm64-v8a.
No game offsets or IL2CPP layout assumptions are embedded.
