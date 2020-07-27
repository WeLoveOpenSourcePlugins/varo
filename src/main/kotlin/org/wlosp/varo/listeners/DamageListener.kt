package org.wlosp.varo.listeners

import com.github.johnnyjayjay.spiglin.PluginManager
import com.github.johnnyjayjay.spiglin.broadcast
import com.github.johnnyjayjay.spiglin.event.ExtendedListener
import com.github.johnnyjayjay.spiglin.event.hear
import org.bukkit.entity.FishHook
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.jetbrains.exposed.sql.transactions.transaction
import org.wlosp.varo.VaroPlugin
import org.wlosp.varo.api.events.VaroPlayerDiedEvent
import org.wlosp.varo.api.events.VaroTeamDiedEvent
import org.wlosp.varo.entities.DatabaseVaroPlayer

fun VaroPlugin.registerDamageListener(): ExtendedListener<EntityDamageEvent> = hear {
    if (it is EntityDamageByEntityEvent) {
        it.isCancelled = it.damager is FishHook && varoConfig.fishingRod
        val entity = it.entity
        if (entity is Player && entity.health - it.finalDamage <= 0.0) {
            transaction {
                val varoPlayer = DatabaseVaroPlayer.findById(entity.uniqueId)
                if (varoPlayer != null) {
                    PluginManager.callEvent(VaroPlayerDiedEvent(entity, varoPlayer))
                    varoPlayer.isAlive = false
                    if (!varoPlayer.team.isAlive) {
                        PluginManager.callEvent(VaroTeamDiedEvent(varoPlayer.team, VaroTeamDiedEvent.Cause.DEATH))
                    }
                    entity.kickPlayer("Du bist ausgeschieden!")
                }

            }

            broadcast("${entity.name} ist tot! Cause: ${it.damager}")
        }
    }
}
