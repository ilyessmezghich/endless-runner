# Endless Runner

A small Android endless-runner game built with Kotlin and a custom `SurfaceView`. Tap the screen to jump over obstacles, survive as long as possible, and beat your high score.

## Features

- Tap-to-start and tap-to-jump gameplay
- Procedurally timed obstacles
- Score and high-score tracking
- Lightweight custom game loop and physics
- Unit tests for game physics and scoring

## Requirements

- Android Studio with Android SDK Platform 34
- JDK 17
- Android SDK Build Tools 34.0.0

The project supports Android API 24 and newer.

## Build and test

Run the unit tests and checks:

```bash
./gradlew check
```

Build a debug APK:

```bash
./gradlew assembleDebug
```

The APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

On Windows, use `gradlew.bat` instead:

```powershell
.\gradlew.bat check
.\gradlew.bat assembleDebug
```

## How to play

1. Launch the app and tap **TAP TO START**.
2. Tap the screen to make the player jump.
3. Avoid the red obstacles.
4. After a collision, tap **TAP TO RESTART** to try again.

## Project structure

```text
app/src/main/java/com/example/runner/
├── MainActivity.kt   # Activity, rendering, input, and game loop
├── GamePhysics.kt    # Jump and gravity simulation
└── ScoreBoard.kt     # Current score and high-score tracking
```

## Continuous integration

GitHub Actions runs checks and unit tests, builds the debug APK, exports runtime dependencies, scans dependencies for vulnerabilities, and publishes an APK release for builds from `master`.
