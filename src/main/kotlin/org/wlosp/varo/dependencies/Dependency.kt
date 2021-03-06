package org.wlosp.varo.dependencies

enum class Dependency(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val repository: String? = null
) {

    SPIGLIN("com.github.johnnyjayjay", "spiglin", "2.0.3", "https://dl.bintray.com/johnnyjayjay/spiglin"),
    KORD("com.gitlab.kordlib.kord", "kord-core", "0.5.6"),
    COROUTINES("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8", "1.3.4")
}