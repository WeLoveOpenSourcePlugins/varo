package org.wlosp.varo.commands

import com.github.johnnyjayjay.spiglin.command.CommandConvention
import org.bukkit.Bukkit
import org.bukkit.conversations.Conversable
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.ExactMatchConversationCanceller
import org.jetbrains.exposed.sql.transactions.transaction
import org.wlosp.varo.VaroPlugin
import org.wlosp.varo.converstaion.StartPrompt
import org.wlosp.varo.converstaion.color
import org.wlosp.varo.converstaion.locations
import org.wlosp.varo.entities.DatabaseVaroPlayer
import org.wlosp.varo.entities.DatabaseVaroTeam

fun CommandConvention.varoTeamCreateCommand(plugin: VaroPlugin) {
    subCommandExecutor("create", false) { context ->
        context.withPermission("varo.team") {
            val args = it.args
            if (args.isEmpty()) return@withPermission false
            val name = args.first()

            val teamFound =
                transaction { DatabaseVaroTeam.findById(name) != null }

            if (teamFound) {
                it.sender.sendMessage("Dieses Team existiert bereits")
                return@withPermission true
            }

            val conversationFactory = ConversationFactory(plugin)
                .withFirstPrompt(StartPrompt(name))
                .withEscapeSequence("abort")
                .addConversationAbandonedListener { event ->
                    if (event.canceller !is ExactMatchConversationCanceller) {
                        it.sender.sendMessage("Das team wird erstellt!")
                        val team = transaction {
                            DatabaseVaroTeam.new(name) {
                                color = event.context.color
                            }
                        }

                        val scoreboard =
                            Bukkit.getScoreboardManager()?.mainScoreboard
                                ?: error("Could not get scoreboard manager")
                        val scoreboardTeam = scoreboard.registerNewTeam("wlosp_varo_$name")
                        scoreboardTeam.color = team.color
                        scoreboardTeam.setAllowFriendlyFire(plugin.varoConfig.friendlyFire)

                        transaction {
                            event.context.locations.forEach { (uuid, location) ->
                                val player = Bukkit.getPlayer(uuid)
                                if (player != null) {
                                    scoreboardTeam.addEntry(player.name)
                                }

                                DatabaseVaroPlayer.new(uuid) {
                                    this.team = team

                                    this.spawnLocationWorld = location.world?.name ?: error("World is not loaded")
                                    this.spawnLocationX = location.x
                                    this.spawnLocationY = location.y
                                    this.spawnLocationZ = location.z
                                    this.spawnLocationYaw = location.yaw
                                    this.spawnLocationPitch = location.pitch
                                }
                            }
                        }

                        it.sender.sendMessage("Das team mit dem Namen $name wurde erstellt")
                    }
                }

            conversationFactory.buildConversation(it.sender as Conversable).begin()
            true
        }
    }
}
