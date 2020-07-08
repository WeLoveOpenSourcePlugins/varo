package org.wlosp.varo.listeners

import com.github.johnnyjayjay.spiglin.event.hear
import org.bukkit.entity.FishHook
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.wlosp.varo.VaroPlugin

fun VaroPlugin.registerDamageListener() = hear<EntityDamageEvent> {
    if (it is EntityDamageByEntityEvent) {
        it.isCancelled = it.damager is FishHook && varoConfig.fishingRod
    }
}