package org.wlosp.varo.api

import org.bukkit.OfflinePlayer
import org.wlosp.varo.api.entities.VaroPlayer
import org.wlosp.varo.api.entities.VaroTeam

/**
 * Converts this [OfflinePlayer] to a [VaroPlayer].
 *
 * @see VaroPlayerApi.get
 */
fun OfflinePlayer.toVaroPlayer(): VaroPlayer? = VaroPlayerApi[this]

/**
 * A list of all [VaroPlayer]s.
 *
 * @see VaroPlayerApi.players
 */
val varoPlayers: List<VaroPlayer>
    get() = VaroPlayerApi.players

/**
 * A list of all online [VaroPlayer]s.
 *
 * @see VaroPlayerApi.onlinePlayers
 */
val onlineVaroPlayers: List<VaroPlayer>
    get() = VaroPlayerApi.onlinePlayers

/**
 * A list of all [VaroTeam]s.
 *
 * @see VaroTeamApi.teams
 */
val varoTeams: List<VaroTeam>
    get() = VaroTeamApi.teams
