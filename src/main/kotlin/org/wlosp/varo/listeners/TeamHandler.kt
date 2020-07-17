package org.wlosp.varo.listeners

import com.github.johnnyjayjay.spiglin.event.hear
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerLoginEvent
import org.jetbrains.exposed.sql.transactions.transaction
import org.wlosp.varo.VaroPlugin
import org.wlosp.varo.entities.VaroPlayer
import org.wlosp.varo.util.ScoreboardTeamUtils

fun VaroPlugin.registerTeamHandler() {
    hear<PlayerLoginEvent> {
        val varoPlayer = transaction { VaroPlayer.findById(it.player.uniqueId) }

        if (varoPlayer == null) {
            if (!it.player.hasPermission("varo.joinbypass")) {
                it.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Du nimmst nicht an Varo teil!")
            }
            return@hear
        }

        val scoreboardTeamName = transaction { ScoreboardTeamUtils.formatTeamName(varoPlayer.team.name) }
        val scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard ?: error("Missing scoreboard")
        val team = scoreboard.getTeam(scoreboardTeamName) ?: error("Missing scoreboard team")

        val playerName = it.player.name
        if (playerName !in team.entries) {
            team.addEntry(playerName)
        }
    }
}