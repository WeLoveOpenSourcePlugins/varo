package org.wlosp.varo.api.entities

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

/**
 * Representation of a Player in this Varo.
 *
 * @property uuid the [UUID] of the player
 * @property team the [VaroTeam] the player is in
 * @property spawnLocation the [Location] at which the player will spawn when Varo starts.
 */
interface VaroPlayer {
    val uuid: UUID
    val team: VaroTeam
    val spawnLocation: Location
    val isAlive: Boolean

    /**
     * Returns the corresponding [Player] or `null` if the player is not online.
     *
     * @see Bukkit.getPlayer
     */
    fun toPlayer(): Player? = Bukkit.getPlayer(uuid)

    /**
     * Returns the corresponding [OfflinePlayer].
     *
     * @see Bukkit.getOfflinePlayer
     */
    fun toOfflinePlayer(): OfflinePlayer = Bukkit.getOfflinePlayer(uuid)
}
