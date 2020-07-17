package org.wlosp.varo.commands

import com.github.johnnyjayjay.spiglin.command.CommandConvention
import org.bukkit.Bukkit
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.wlosp.varo.entities.VaroPlayers
import org.wlosp.varo.entities.VaroTeams

fun CommandConvention.varoResetCommand() {
    subCommandExecutor("reset") {
        val scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard ?: error("world not loaded")
        scoreboard.teams.forEach { team ->
            if (team.name.startsWith("wlosp_varo")) {
                team.unregister()
            }
        }

        transaction {
            VaroPlayers.deleteAll()
            VaroTeams.deleteAll()
        }

        it.sender.sendMessage("reseated")
        true
    }
}