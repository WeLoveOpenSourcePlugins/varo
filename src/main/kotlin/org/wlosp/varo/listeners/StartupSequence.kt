package org.wlosp.varo.listeners

import com.github.johnnyjayjay.spiglin.event.hear
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.jetbrains.exposed.sql.transactions.transaction
import org.wlosp.varo.VaroPlugin
import org.wlosp.varo.entities.Stage
import org.wlosp.varo.entities.DatabaseVaroPlayer

fun VaroPlugin.registerStartupSequence() {
    hear<PlayerJoinEvent> {
        if (stage == Stage.STARTING) {
            val varoPlayer = transaction { DatabaseVaroPlayer.findById(it.player.uniqueId) }
            varoPlayer?.let { player ->
                it.player.teleport(player.spawnLocation)
            }
        }
    }

    hear<BlockBreakEvent> {
        it.isCancelled = (stage == Stage.STARTING || stage == Stage.READY) && !it.player.hasPermission("varo.admin")
    }

    hear<PlayerMoveEvent> {
        it.isCancelled = (stage == Stage.STARTING || stage == Stage.READY) && !it.player.hasPermission("varo.admin")
    }
}
