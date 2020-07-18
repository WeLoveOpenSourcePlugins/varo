package org.wlosp.varo.commands

import com.github.johnnyjayjay.spiglin.broadcast
import com.github.johnnyjayjay.spiglin.command.CommandConvention
import com.github.johnnyjayjay.spiglin.scheduler.repeat
import org.bukkit.scheduler.BukkitTask
import org.jetbrains.exposed.sql.transactions.transaction
import org.wlosp.varo.VaroPlugin
import org.wlosp.varo.entities.DatabaseVaroPlayer
import org.wlosp.varo.entities.DatabaseVaroTeam
import org.wlosp.varo.entities.Stage

private var countdownTask: BukkitTask? = null

fun CommandConvention.varoStartCommand(plugin: VaroPlugin) {
    subCommandExecutor("abort") { context ->
        context.withPermission("varo.start") {
            plugin.stage = Stage.SETUP
            countdownTask?.cancel()
            true
        }
    }

    subCommandExecutor("prepare") { context ->
        context.withPermission("varo.start") {
            val teamsCount = transaction {
                DatabaseVaroTeam.all().count()
            }

            if (teamsCount < 2) {
                it.sender.sendMessage("Du braucht min. 2 teams!")
                return@withPermission true
            }

            val players = transaction {
                DatabaseVaroPlayer.all().toList()
            }

            players.forEach {
                val player = it.toPlayer()
                if (player == null) {
                    context.sender.sendMessage("Es sind nicht alle spieler online")
                    return@withPermission true
                }
                player.teleport(it.spawnLocation)
            }

            plugin.stage = Stage.READY
            true
        }
    }

    subCommandExecutor("start") { context ->
        context.withPermission("varo.start") {
            if (plugin.stage != Stage.READY) {
                it.sender.sendMessage("Bitte mache zuerst /varo prepare")
                return@withPermission true
            }
            plugin.stage = Stage.STARTING
            countdownTask = plugin.repeat(
                progression = plugin.varoConfig.startCountDown downTo 0,
                async = true
            ) { number ->
                if (number == 0) {
                    broadcast("start")
                } else if (number % 5 == 0 || number <= 5) {
                    broadcast(number.toString())
                }
            }
            true
        }
        plugin.stage = Stage.RUNNING
        true
    }
}