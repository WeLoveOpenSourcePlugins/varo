package org.wlosp.varo.api.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.wlosp.varo.api.entities.VaroTeam

class VaroEndEvent(
    val winningTeam: VaroTeam
) : Event() {

    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        @JvmStatic
        @get:JvmName("getHandlersList")
        val HANDLERS: HandlerList = HandlerList()
    }
}