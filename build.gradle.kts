import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("com.github.johnrengelman.shadow") version "6.0.0"
    kotlin("jvm") version "1.3.72"
}

group = "org.wlosp"
version = "1.0-SNAPSHOT"

/**
 * Location of developers plugins directory.
 */
val spigotPluginsDir: String? by project

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    maven("https://dl.bintray.com/johnnyjayjay/spiglin")
    maven("https://repo.byteflux.net/repository/maven-releases/")
    maven("https://jitpack.io")
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(project(":api"))

    compileOnly("org.spigotmc", "spigot-api", "1.16.1-R0.1-SNAPSHOT")
    compileOnly("com.github.johnnyjayjay", "spiglin", "develop-SNAPSHOT")
    implementation("net.byteflux", "libby-bukkit", "0.0.1")

    // Database
    compileOnly("org.jetbrains.exposed", "exposed-core", "0.25.1")
    compileOnly("org.jetbrains.exposed", "exposed-dao", "0.25.1")
    compileOnly("org.jetbrains.exposed", "exposed-jdbc", "0.25.1")
    compileOnly("com.zaxxer", "HikariCP", "3.4.5")

    // HTTP
    compileOnly("com.squareup.okhttp3", "okhttp", "4.8.0")

    compileOnly("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8", "1.3.4")
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
}

tasks {
    processResources {
        from(sourceSets["main"].resources) {
            val tokens = mapOf("version" to version)
            filter(ReplaceTokens::class, mapOf("tokens" to tokens))
        }
    }


    task<Copy>("installPlugin") {
        dependsOn(shadowJar)
        from(shadowJar)
        include("*-all.jar")
        into(spigotPluginsDir ?: error("Please set spigotPluginsDir in gradle.properties"))
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}