package org.wlosp.varo.configuration

import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.wlosp.varo.api.entities.StrikeAction
import org.wlosp.varo.database.DatabaseType
import java.time.Duration
import java.time.ZoneId

data class Config(
    val name: String,
    val startCountDown: Int,
    val gracePeriod: Int,
    val enableWhitelist: Boolean,
    val friendlyFire: Boolean,
    val fishingRod: Boolean,
    val itemBlockStrategy: ItemBlockStrategy,
    val blockedItems: List<Material>,
    val time: TimeConfig,
    val tablist: TablistConfig,
    val strikes: Map<Int, StrikeAction>,
    val locations: Map<VaroLocation, VaroCoordinates>,
    val database: DatabaseConfig
) {
    data class TimeConfig(val timezone: ZoneId, val startTime: Duration, val shutdownTime: Duration)

    data class TablistConfig(val header: String, val footer: String)

    data class VaroCoordinates(val x: Int, val y: Int, val z: Int)

    interface DatabaseConfig {
        val databaseType: DatabaseType
    }

    data class FileDatabaseConfig(override val databaseType: DatabaseType, val file: String) : DatabaseConfig

    data class ServerDatabaseConfig(
        override val databaseType: DatabaseType,
        val host: String,
        val port: Int,
        val username: String,
        val database: String,
        val password: String,
        val useSSL: Boolean
    ) :
        DatabaseConfig

    enum class VaroLocation {
        SPAWN,
        NETHER_PORTAL
    }

    enum class ItemBlockStrategy {
        DELETE,
        IGNORE
    }

    companion object {

        fun fromFileConfiguration(configuration: FileConfiguration): Config {
            val name = configuration.getString("name") ?: error("Missing name property in config")
            val gracePeriod = configuration.getInt("gracePeriod")
            val startCountDown = configuration.getInt("startCountDown")
            val enableWhitelist = configuration.getBoolean("enableWhitelist")
            val friendlyFire = configuration.getBoolean("friendlyFire")
            val fishingRod = configuration.getBoolean("fishingRod")
            val itemBlockStrategy = configuration.getString("itemBlockStrategy")?.let { ItemBlockStrategy.valueOf(it) }
                ?: error("Missing itemBlockStrategy property in config")
            val blockedItems = configuration.getStringList("blockedItems").map(Material::valueOf)

            val timeNode = configuration.getConfigurationSection("time") ?: error("Missing time property in config")
            val zone = timeNode.getString("timezone")?.let(ZoneId::of) ?: error("Missing time.timezone property config")
            val startDuration =
                timeNode.getString("startTime")?.let(::parseDuration) ?: error("Missing time.startTime property config")
            val shutdownDuration = timeNode.getString("shutdownTime")?.let(::parseDuration)
                ?: error("Missing time.shutdownTime property config")
            val time = TimeConfig(zone, startDuration, shutdownDuration)

            val tablistNode =
                configuration.getConfigurationSection("tablist") ?: error("Missing tablist property config")
            val header = tablistNode.getString("footer") ?: error("Missing tablist.header property config")
            val footer = tablistNode.getString("footer") ?: error("Missing tablist.footer property config")
            val tablist = TablistConfig(header, footer)

            val strikesNode =
                configuration.getConfigurationSection("strikes") ?: error("Missing strikes property config")
            val strikes = strikesNode.getValues(false)
                .mapKeys { it.key.toInt() }
                .mapValues {
                    StrikeAction.valueOf(it.value.toString().toUpperCase())
                }

            val locationsNode =
                configuration.getConfigurationSection("locations") ?: error("Missing locations property config")
            val locations = locationsNode.getValues(false)
                .mapKeys {
                    VaroLocation.valueOf(it.key)
                }
                .mapValues {
                    val coordinatesNode = it.value as ConfigurationSection
                    VaroCoordinates(
                        coordinatesNode.getInt("x"),
                        coordinatesNode.getInt("y"),
                        coordinatesNode.getInt("z")
                    )
                }

            val databaseNode =
                configuration.getConfigurationSection("database") ?: error("Missing config property database")
            val type = databaseNode.getString("type")?.let { DatabaseType.valueOf(it.toUpperCase()) }
                ?: error("Missing config property database")
            val database = when (type) {
                DatabaseType.SQLITE -> {
                    val file = databaseNode.getString("file") ?: error("Missing config property database.file")
                    FileDatabaseConfig(type, file)
                }
                DatabaseType.MYSQL, DatabaseType.POSTGRESQL -> {
                    val host = databaseNode.getString("host") ?: error("Missing config property database.host")
                    val port = databaseNode.getInt("port")
                    val username =
                        databaseNode.getString("username") ?: error("Missing config property database.username")
                    val database =
                        databaseNode.getString("database") ?: error("Missing config property database.database")
                    val password =
                        databaseNode.getString("password") ?: error("Missing config property database.password")
                    val useSSL = databaseNode.getBoolean("useSSL")

                    ServerDatabaseConfig(type, host, port, username, database, password, useSSL)
                }
            }

            return Config(
                name,
                startCountDown,
                gracePeriod,
                enableWhitelist,
                friendlyFire,
                fishingRod,
                itemBlockStrategy,
                blockedItems,
                time,
                tablist,
                strikes,
                locations,
                database
            )
        }

        private fun parseDuration(input: String): Duration {
            val match = PATTERN.matchEntire(input) ?: error("Time has to be in HH:mm format.")
            val (_, hour, minute) = match.groupValues
            return Duration.ofHours(hour.toLong()).plusMinutes(minute.toLong())
        }

        private val PATTERN = "([0-9]{1,2}):([0-9]{1,2})".toRegex()
    }
}