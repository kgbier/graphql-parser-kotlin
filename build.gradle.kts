plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin
    kotlin("multiplatform") version "1.5.10"
}

repositories {
    jcenter()
}

kotlin {
    jvm()
    js {
        browser ()
        nodejs ()
    }

    sourceSets {
        val commonTest by getting {
            dependencies {
                // This brings the dependency
                // on JUnit 4 transitively
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("reflect"))
            }
        }
    }
}
