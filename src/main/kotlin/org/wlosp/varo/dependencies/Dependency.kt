package org.wlosp.varo.dependencies

enum class Dependency(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val repository: String? = null,
    val dependencies: List<Dependency> = emptyList()
) {
    OKIO("com.squareup.okio", "okio", "2.7.0"),
    OKHTTP("com.squareup.okhttp3", "okhttp", "4.8.0", dependencies = listOf(OKIO)),

    SLF4J("org.slf4j", "slf4j-api", "1.7.25"),

    SPIGLIN("com.github.johnnyjayjay", "spiglin", "develop-SNAPSHOT", "https://jitpack.io"),

    KORD("com.gitlab.kordlib.kord", "kord-core", "0.5.6"),

    COROUTINES_CORE("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.3.4"),
    COROUTINES("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8", "1.3.4", dependencies = listOf(COROUTINES_CORE)),

    SQLITE_DRIVER("org.xerial", "sqlite-jdbc", "3.32.3"),
    MYSQL_DRIVER("mysql", "mysql-connector.java", "5.1.49"),
    POSTGRESQL_DRIVER("org.postgresql", "postgresql", "42.2.14"),

    EXPOSED_DAO("org.jetbrains.exposed", "exposed-dao", "0.25.1"),
    EXPOSED_JDBC("org.jetbrains.exposed", "exposed-jdbc", "0.25.1"),
    EXPOSED(
        "org.jetbrains.exposed",
        "exposed-core",
        "0.25.1",
        dependencies = listOf(EXPOSED_DAO, EXPOSED_JDBC, SLF4J)
    ),

    HIKARI("com.zaxxer", "HikariCP", "3.4.5")
}