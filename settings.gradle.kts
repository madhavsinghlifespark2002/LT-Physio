enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
include(":unityLibrary")
project(":unityLibrary").projectDir = file("./unityLibrary")
dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        flatDir {
            dirs = setOf(file("${project(":unityLibrary").projectDir}/libs"))
        }

        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven {
            url = uri("`[`https://maven.pkg.jetbrains.space/public/p/ktor/eap`](https://maven.pkg.jetbrains.space/public/p/ktor/eap)`")
            url = uri("/Users/Igor/Work/lets-plot-skia/.maven-publish-dev-repo")
        }
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        mavenLocal()
    }
}
rootProject.name = "LSPhysio"
include(":androidApp")
include(":shared")