package org.wlosp.varo.api.entities

import org.bukkit.ChatColor

/**
 * Representation of a Varo team.
 *
 * @property name the name of the team
 * @property color the color of the team (used for Discord and Scoreboard)
 * @property players an unmodifiable list of [VaroPlayer]s which are in this team
 * @property isAlive whether the team is still alive or not
 */
interface VaroTeam {
    val name: String
    val color: ChatColor

    val players: List<VaroPlayer>

    val isAlive: Boolean
        get() = players.any(VaroPlayer::isAlive)
}
