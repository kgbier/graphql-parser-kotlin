plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin
    kotlin("multiplatform") version "1.8.0"
}

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js(IR) {
        browser()
        nodejs()
        binaries.executable()
    }

    sourceSets {
        val commonTest by getting {
            dependencies {
                // This adds a transient dependency on JUnit 4
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("reflect"))
            }
        }

        all {
            languageSettings.apply {
                optIn("kotlin.js.ExperimentalJsExport")
            }
        }
    }
}
