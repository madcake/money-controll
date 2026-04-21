plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.koin.compiler)
}

kotlin {

    android {
        namespace = "shiny.mc.infrastructure.persistent.room"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = 31

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
    val xcfName = "infrastructure:persistent:roomKit"

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
                implementation(projects.core)
                // Room
                implementation(libs.room.runtime)
                implementation(libs.sqlite.bundled)
                // Koin
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                api(libs.koin.annotations)
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

        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }

}

dependencies {
    ksp(libs.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
    generateKotlin = true
}

koinCompiler {
    userLogs = true  // Log component detection
}

tasks.matching { it.name.startsWith("ksp") && it.name != "kspCommonMainKotlinMetadata" }.configureEach {
    dependsOn("kspCommonMainKotlinMetadata")
}

// KSP Metadata Trigger
tasks.matching { it.name.startsWith("ksp") && it.name != "kspCommonMainKotlinMetadata" }.configureEach {
    dependsOn("kspCommonMainKotlinMetadata")
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}