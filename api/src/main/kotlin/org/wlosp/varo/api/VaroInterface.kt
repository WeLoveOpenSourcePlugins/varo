package org.wlosp.varo.api

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.wlosp.varo.api.entities.VaroPlayer
import org.wlosp.varo.api.entities.VaroTeam
import java.util.*

typealias VaroTeamApiInterface = VaroInterface.TeamApi

typealias VaroPlayerApiInterface = VaroInterface.PlayerApi

/**
 * API for WLOSP Varo plugin.
 *
 * @see VaroApi
 */
interface VaroInterface {

    /**
     * API for Varo teams.
     *
     * @see TeamApi
     */
    val teams: TeamApi

    /**
     * API for Varo players.
     *
     * @see PlayerApi
     */
    val players: PlayerApi

    /**
     * Teams related API.
     */
    interface TeamApi {
        /**
         * An unmodifiable list of all [VaroTeam]s.
         */
        val teams: List<VaroTeam>

        /**
         * Retrieves a [VaroTeam] by its [name] or `null` if it does not exist.
         */
        operator fun get(name: String): VaroTeam?
    }

    /**
     * Player related API.
     */
    interface PlayerApi {

        /**
         * An unmodifiable list of all [VaroPlayer].
         */
        val players: List<VaroPlayer>

        /**
         * An unmodifiable list of all [VaroPlayer], that are currently online.
         */
        val onlinePlayers: List<VaroPlayer>

        /**
         * Retrieves a [VaroPlayer] by it's [UUID].
         *
         * @throws IllegalArgumentException if the player does not participate in the currently running Varo project
         */
        operator fun get(uuid: UUID): VaroPlayer?

        /**
         * Retrieves the [VaroPlayer] corresponding to this [player].
         *
         * @throws IllegalArgumentException if the player does not participate in the currently running Varo project
         * @see get(UUID)
         */
        operator fun get(player: OfflinePlayer): VaroPlayer? = get(player.uniqueId)

        /**
         * This method only exists to tell players who don't know that [Player] extends [OfflinePlayer] to use the normal get method
         * as they cannot read docs.
         *
         * @see get(OfflinePlayer)
         */
        @Deprecated(
            "Player extends OfflinePlayer so just use the normal get method",
            ReplaceWith("get"),
            DeprecationLevel.ERROR
        )
        fun getByPlayer(player: Player): VaroPlayer? = get(player)

        /**
         * Finds the [VaroPlayer] corresponding to its [name].
         *
         * @throws IllegalArgumentException when the player is not online
         */
        operator fun get(name: String): VaroPlayer? {
            val player = Bukkit.getPlayer(name)
            requireNotNull(player) { "The player has to be online" }
            return get(player)
        }
    }
}
