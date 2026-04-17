plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)

    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {

    android {
        namespace = "shiny.mc.core_ui"
        compileSdk {
            version = release(36) {
                minorApiLevel = 1
            }
        }
        minSdk = 31

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        androidResources.enable = true
    }

    val xcfName = "core-uiKit"

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                api(libs.compose.runtime)
                api(libs.compose.foundation)
                api(libs.compose.material3)
                api(libs.compose.ui)
                api(libs.compose.uiToolingPreview)
                api(libs.androidx.lifecycle.viewmodelCompose)
                api(libs.androidx.lifecycle.runtimeCompose)
                api(libs.compose.icons)
                api(libs.compose.icons.extended)
                api(libs.compose.components.resources)
                api(projects.shared)
                api(libs.kotlinx.datetime)
                // Navigation
                api(libs.navigation3)
                api(libs.navigation3.material3.adaptive)
                api(libs.navigation3.material3.adaptive.navigation)
                api(libs.navigation3.material3.adaptive.layout)
                api(libs.navigation3.lifecycle.viewmodel)
                // Koin
                api(project.dependencies.platform(libs.koin.bom))
                api(libs.koin.core)
                api(libs.koin.annotations)
                api(libs.koin.compose)
                api(libs.koin.compose.viewmodel)
                api(libs.koin.compose.navigation)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies { }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.testExt.junit)
            }
        }

        iosMain {
            dependencies { }
        }

        jvmMain {
            dependencies {
                api(compose.desktop.currentOs)
                api(libs.kotlinx.coroutinesSwing)
                api(libs.androidx.lifecycle.runtimeCompose)
            }
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexplicit-backing-fields")
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "shiny.mc.core_ui.resources"
}