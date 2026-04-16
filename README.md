# MoneyControll

**MoneyControll** is a cross-platform personal finance management application built with Kotlin Multiplatform and Compose Multiplatform. It allows users to track income and expenses, plan budgets by periods, and synchronize data across multiple devices.

## Features

- **Cross-Platform Support**: Target platforms include Android, iOS, Desktop (JVM), and a dedicated Backend server.
- **Period Management**: Organize transactions by months and years for better financial tracking.
- **Flexible Categories**: Create and customize categories for detailed spending analysis.
- **Modern UI/UX**: Built with Material 3 and Adaptive Layouts for a consistent experience on all screen sizes.
- **Local-First Reliability**: Uses Room for robust local data storage and offline capabilities.

## Tech Stack

- **UI Framework**: [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- **Dependency Injection**: [Koin](https://insert-koin.io/)
- **Database**: [Room](https://developer.android.com/kotlin/multiplatform/room)
- **Networking**: [Ktor](https://ktor.io/)
- **Serialization**: [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- **Architecture**: MVVM / Command Pattern

## Project Structure

* [`/composeApp`](./composeApp) — Common UI and shared logic for client applications (Android, iOS, Desktop).
* [`/server`](./server) — Ktor-based backend server.
* [`/core`](./core) — Core business logic, domain models, and repository interfaces.
* [`/shared`](./shared) — Shared data sources and platform-specific implementations.
* [`/iosApp`](./iosApp) — iOS entry point and Swift-specific code.

## Getting Started

### Prerequisites
- JDK 17 or higher
- Android Studio or IntelliJ IDEA
- Xcode (for iOS development)

### Build and Run

#### Android
```shell
./gradlew :composeApp:assembleDebug
```

#### Desktop (JVM)
```shell
./gradlew :composeApp:run
```

#### Server
```shell
./gradlew :server:run
```

#### iOS
1. Navigate to the `/iosApp` directory.
2. Open `iosApp.xcworkspace` in Xcode.
3. Select a simulator or device and click **Run**.

---
Developed using modern Kotlin Multiplatform best practices.
