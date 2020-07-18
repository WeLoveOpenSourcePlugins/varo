package org.wlosp.varo.api.events

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.wlosp.varo.api.entities.VaroPlayer
import org.wlosp.varo.api.entities.VaroTeam

class VaroPlayerDiedEvent(
    val player: Player,
    val varoPlayer: VaroPlayer
) : Event() {

    val varoTeam: VaroTeam by lazy { varoPlayer.team }

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    companion object {
        @JvmStatic
        @get:JvmName("getHandlersList")
        val HANDLERS: HandlerList = HandlerList()
    }
}
