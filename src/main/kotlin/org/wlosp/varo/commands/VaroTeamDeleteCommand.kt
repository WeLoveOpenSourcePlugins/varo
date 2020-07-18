package org.wlosp.varo.commands

import com.github.johnnyjayjay.spiglin.command.CommandConvention
import org.bukkit.Bukkit
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.wlosp.varo.entities.DatabaseVaroTeam
import org.wlosp.varo.entities.VaroPlayers
import org.wlosp.varo.entities.VaroTeams
import org.wlosp.varo.util.ScoreboardTeamUtils

fun CommandConvention.varoTeamDeleteCommand() {
    subCommandExecutor("delete", executor = { context ->
        context.withPermission("varo.team") {
            if (it.args.isEmpty()) return@withPermission false
            val team = transaction {
                DatabaseVaroTeam.findById(it.args.first())
            } ?: return@withPermission it.sender.sendMessage("Team not found").run { true }

            transaction {
                Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam(ScoreboardTeamUtils.formatTeamName(team.name))
                    ?.unregister() ?: error("Attempted to unregister scorboard team while world is not loaded")
                VaroPlayers.deleteWhere { VaroPlayers.team eq team.id }
                team.delete()
            }

            it.sender.sendMessage("gelöscht")
            return@withPermission true
        }
    }, tabCompleter = {
        transaction {
            DatabaseVaroTeam.find { VaroTeams.id like it.args.firstOrNull()?.plus("%").toString() }.map { it.name }
                .toMutableList()
        }
    })
}
