# CryptoTracker

A simple Android app that displays cryptocurrency information using Jetpack Compose and CoinGecko API.

## 🧪 Running Tests

This project includes both **unit tests** and **instrumented UI tests**.

### ✅ Unit Tests

To run unit tests:

```bash
./gradlew test
```

You can also run from Android Studio:
- Right-click the `test` directory (e.g. `src/test/java`)
- Click **Run 'All Tests'**

### 📱 Instrumented Tests

To run instrumented (UI) tests on a device or emulator:

```bash
./gradlew connectedAndroidTest
```

You can also run from Android Studio:
- Connect an emulator or physical device
- Right-click the `androidTest` directory (e.g. `src/androidTest/java`)
- Click **Run 'All Tests'**

---

## 🛠 Requirements

- Android Studio Hedgehog or newer
- Kotlin 1.9+
- JDK 17 (recommended)
- Gradle 8+

---

## 📦 Notes

- Uses Jetpack Compose
- MockK used for unit testing
- Turbine used for Flow testing
- CoinGecko REST API