plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin
    kotlin("multiplatform") version "1.4.10"
}

repositories {
    jcenter()
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by sourceSets.getting { }
        val commonTest by sourceSets.getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by sourceSets.getting {
            dependencies {
                implementation(kotlin("reflect"))
            }
        }
        val jvmTest by sourceSets.getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
    }
}
