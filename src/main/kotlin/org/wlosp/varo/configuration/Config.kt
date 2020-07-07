package org.wlosp.varo.configuration

import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import java.time.Duration
import java.time.ZoneId

data class Config(
    val name: String,
    val gracePeriod: Int,
    val enableWhitelist: Boolean,
    val friendlyFire: Boolean,
    val fishingRod: Boolean,
    val blockedItems: List<Material>,
    val time: TimeConfig,
    val tablist: TablistConfig,
    val strikes: Map<Int, StrikeAction>,
    val locations: Map<VaroLocation, VaroCoordinates>
) {
    data class TimeConfig(val timezone: ZoneId, val startTime: Duration, val shutdownTime: Duration)

    data class TablistConfig(val header: String, val footer: String)

    data class VaroCoordinates(val x: Int, val y: Int, val z: Int)

    enum class StrikeAction {
        PUBLISH_COORDINATES,
        DELETE_INVENTORY_AND_CHEST,
        BAN
    }

    enum class VaroLocation {
        SPAWN,
        NETHER_PORTAL
    }

    companion object {

        fun fromFileConfiguration(configuration: FileConfiguration): Config {
            val name = configuration.getString("name") ?: error("Missing name property in config")
            val gracePeriod = configuration.getInt("gracePeriod")
            val enableWhitelist = configuration.getBoolean("enableWhitelist")
            val friendlyFire = configuration.getBoolean("friendlyFire")
            val fishingRod = configuration.getBoolean("fishingRod")
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

            return Config(
                name,
                gracePeriod,
                enableWhitelist,
                friendlyFire,
                fishingRod,
                blockedItems,
                time,
                tablist,
                strikes,
                locations
            )
        }

        private fun parseDuration(input: String): Duration {
            println(input)
            val match = PATTERN.matchEntire(input) ?: error("Time has to be in HH:mm format.")
            val (_, hour, minute) = match.groupValues
            return Duration.ofHours(hour.toLong()).plusMinutes(minute.toLong())
        }

        private val PATTERN = "([0-9]{1,2}):([0-9]{1,2})".toRegex()
    }
}