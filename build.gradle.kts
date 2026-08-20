plugins {
    java
    // Pulls a Paper "dev bundle" and produces a mojang-mapped plugin jar that loads
    // on modern Paper/Folia without the server remapping it. Published on the papermc
    // repo (see settings.gradle.kts pluginManagement).
    id("io.papermc.paperweight.userdev") version "2.0.0-SNAPSHOT"
}

group = "uk.cloudmc.microwavedram"
version = "1.5-SNAPSHOT"

base {
    archivesName.set("noboatlag")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Folia 26.2 is a Paper fork; the NMS this plugin touches (Boat/Raft/canCollideWith,
    // ServerLevel, CraftWorld) is all Paper-inherited, so we compile against the Paper
    // dev bundle for the matching version. The resulting jar runs on Folia 26.2.
    paperweight.paperDevBundle("26.2.build.112-stable")
}

java {
    // Folia 26.2 runs on Java 25. Gradle itself can run on an older JDK; the toolchain
    // makes it compile with 25 (auto-provisioned via the foojay resolver if absent).
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}
