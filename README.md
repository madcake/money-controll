# MoneyControll

**MoneyControll** is a cross-platform personal finance management application built with Kotlin Multiplatform and Compose Multiplatform. It allows users to track income and expenses, plan budgets by periods, and synchronize data across multiple devices.

## Features

- **Cross-Platform Support**: Target platforms include Android, iOS, and Desktop (JVM).
- **Flexible Categories**: Create and customize categories for detailed spending analysis.
- **Modern UI/UX**: Built with Material 3 and Adaptive Layouts for a consistent experience on all screen sizes.
- **Local-First Reliability**: Uses Room for robust local data storage and offline capabilities.
- **Architecture**: Clean separation of concerns with a focus on Hexagonal principles (Domain, Ports, Adapters).

## Tech Stack

- **UI Framework**: [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- **Dependency Injection**: [Koin](https://insert-koin.io/)
- **Database**: [Room](https://developer.android.com/kotlin/multiplatform/room)
- **Navigation**: [Navigation3](https://developer.android.com/jetpack/compose/navigation)
- **Networking**: [Ktor](https://ktor.io/)
- **Serialization**: [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- **Architecture**: Hexagonal Architecture / MVVM / Command Pattern

## Project Structure

* [`/composeApp`](./composeApp) — Shared UI, view models, and feature navigation.
* [`/core`](./core) — Business logic, domain models, and port definitions.
* [`/infrastructure`](./infrastructure) — External implementations such as Room database and persistent storage.
* [`/shared`](./shared) — Platform-specific bridge and cross-module common utilities.
* [`/iosApp`](./iosApp) — iOS-specific entry point and configuration.

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

#### iOS
1. Navigate to the `/iosApp` directory.
2. Open `iosApp.xcworkspace` in Xcode.
3. Select a simulator or device and click **Run**.

---
Developed using modern Kotlin Multiplatform best practices.
