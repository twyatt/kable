plugins {
    // Android plugin must be before multiplatform plugin until https://youtrack.jetbrains.com/issue/KT-34038 is fixed.
    id("com.android.library")
    kotlin("multiplatform")
    id("kotlinx-atomicfu")
    id("org.jmailen.kotlinter")
    id("org.jetbrains.dokka")
    id("com.vanniktech.maven.publish")
}

kotlin {
    explicitApi()

    js().browser()
    android {
        publishAllLibraryVariants()
    }
    iosX64()
    iosArm64()
    macosX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(coroutines("core", version = "1.5.0-native-mt"))
                api(uuid())
            }
        }

        val jsMain by getting {
            dependencies {
                api(coroutines("core"))
            }
        }

        val androidMain by getting {
            dependencies {
                api(coroutines("android"))
                implementation(atomicfu("jvm"))
                implementation(androidx.startup())
            }
        }

        val macosX64Main by getting {
            dependencies {
                api(coroutines("core", version = "1.5.0-native-mt!!"))
                implementation(stately("isolate-macosx64"))
            }
        }

        val iosX64Main by getting {
            dependencies {
                api(coroutines("core", version = "1.5.0-native-mt!!"))
                implementation(stately("isolate-iosx64"))
            }
        }

        val iosArm64Main by getting {
            dependencies {
                api(coroutines("core", version = "1.5.0-native-mt!!"))
                implementation(stately("isolate-iosarm64"))
            }
        }

        all {
            languageSettings.enableLanguageFeature("InlineClasses")
        }
    }
}

atomicfu {
    transformJvm = true
    transformJs = false
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
    }

    buildFeatures {
        buildConfig = false
    }

    lintOptions {
        isAbortOnError = true
        isWarningsAsErrors = true
    }

    sourceSets {
        val main by getting {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }
}
