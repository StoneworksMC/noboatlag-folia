pluginManagement {
    repositories {
        gradlePluginPortal()
        // paperweight-userdev is published here, not on the plugin portal.
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    // Lets Gradle auto-download a matching JDK (e.g. Java 25) when the build
    // machine doesn't already have one, so a fresh clone builds with only Git installed.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "noboatlag"
