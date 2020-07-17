package org.wlosp.varo.database

import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.wlosp.varo.VaroPlugin
import org.wlosp.varo.configuration.Config
import org.wlosp.varo.dependencies.Dependency
import org.wlosp.varo.dependencies.loadDependency
import org.wlosp.varo.entities.VaroPlayers
import org.wlosp.varo.entities.VaroTeams
import java.nio.file.Files

internal fun VaroPlugin.connectToDatabase() {
    val databaseConfig = varoConfig.database
    dependencyManager.loadDependency(databaseConfig.databaseType.driver)
    if (databaseConfig.databaseType.useHikari) {
        dependencyManager.loadDependency(Dependency.HIKARI)
    }

    val jdbcString = when (databaseConfig.databaseType) {
        DatabaseType.SQLITE -> {
            val file = dataFolder.toPath().resolve((databaseConfig as Config.FileDatabaseConfig).file)
            if (!Files.exists(file)) {
                Files.createFile(file)
            }
            "jdbc:sqlite:${file.toUri()}"
        }
        DatabaseType.MYSQL -> {
            val config = databaseConfig as Config.ServerDatabaseConfig
            "jdbc:mysql://${config.username}:${config.password}@${config.host}:${config.port}/${config.database}}"
        }
        DatabaseType.POSTGRESQL -> {
            val config = databaseConfig as Config.ServerDatabaseConfig
            "jdbc:postgresql://${config.host}:${config.port}/${config.database}?username=${config.username}&password=${config.password}&ssl=${config.useSSL}"
        }
    }

    if (databaseConfig.databaseType.useHikari) {
        val hikari = HikariDataSource().apply {
            jdbcUrl = jdbcString
        }
        Database.connect(datasource = hikari)
    } else {
        Database.connect(jdbcString)
    }

    transaction {
        SchemaUtils.createMissingTablesAndColumns(VaroTeams, VaroPlayers)
    }
}
