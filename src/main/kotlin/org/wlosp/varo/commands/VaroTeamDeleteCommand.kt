package org.wlosp.varo.commands

import com.github.johnnyjayjay.spiglin.command.CommandConvention
import org.bukkit.Bukkit
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.wlosp.varo.entities.VaroPlayers
import org.wlosp.varo.entities.VaroTeam
import org.wlosp.varo.entities.VaroTeams
import org.wlosp.varo.util.ScoreboardTeamUtils

fun CommandConvention.varoTeamDeleteCommand() {
    subCommandExecutor("delete", executor = {
        if (it.args.isEmpty()) return@subCommandExecutor false
        val team = transaction {
            VaroTeam.findById(it.args.first())
        } ?: return@subCommandExecutor it.sender.sendMessage("Team not found").run { true }

        transaction {
            Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam(ScoreboardTeamUtils.formatTeamName(team.name))
                ?.unregister() ?: error("Attempted to unregister scorboard team while world is not loaded")
            VaroPlayers.deleteWhere { VaroPlayers.team eq team.id }
            team.delete()
        }

        it.sender.sendMessage("gelöscht")
        return@subCommandExecutor true
    }, tabCompleter = {
        transaction {
            VaroTeam.find { VaroTeams.id like it.args.firstOrNull()?.plus("%").toString() }.map { it.name }
                .toMutableList()
        }
    })
}