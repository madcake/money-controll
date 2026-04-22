import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)

    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)

    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = false
            freeCompilerArgs += listOf("-Xobjc-generics", "-Xg0")
        }
    }
    
    jvm()
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.coreUi)
            implementation(projects.infrastructure.persistent.room)
            implementation(projects.feature.category)
            implementation(projects.feature.record)
            implementation(projects.feature.transaction)
            implementation(projects.feature.period)

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.compose.icons)
            implementation(libs.compose.icons.extended)
            implementation(projects.shared)
            implementation(libs.kotlinx.datetime)
            // Navigation
            implementation(libs.navigation3)
            implementation(libs.navigation3.material3.adaptive)
            implementation(libs.navigation3.material3.adaptive.navigation)
            implementation(libs.navigation3.material3.adaptive.layout)
            implementation(libs.navigation3.lifecycle.viewmodel)
            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            api(libs.koin.annotations)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.navigation)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexplicit-backing-fields")
    }
}

android {
    namespace = "shiny.mc"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "shiny.mc"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

koinCompiler {
    userLogs = true  // Log component detection
}

compose.desktop {
    application {
        mainClass = "shiny.mc.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "shiny.mc.moneycontroll"
            packageVersion = "1.0.0"
            description = "MoneyControll - Personal Finance Manager"

            linux {
                shortcut = true
                iconFile.set(project.file("../media/appicon/linux_icon.png"))
            }
            windows {
                shortcut = true
                menuGroup = "MoneyControll"
                iconFile.set(project.file("../media/appicon/win_icon.ico"))
            }
            macOS {
                bundleID = "shiny.mc.moneycontroll"
                iconFile.set(project.file("../media/appicon/macos_icon.icns"))
            }

            modules("java.instrument", "jdk.unsupported")
        }

        buildTypes.release.proguard {
            optimize.set(true)
            obfuscate.set(true)
            joinOutputJars.set(true)
        }
    }
}
