package org.wlosp.varo.api.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.wlosp.varo.api.entities.StrikeAction
import org.wlosp.varo.api.entities.VaroTeam

class VaroStrikeEvent(
    val team: VaroTeam,
    val action: StrikeAction?,
    val reason: String?,
    val amount: Int
) : Event() {

    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        @JvmStatic
        @get:JvmName("getHandlersList")
        val HANDLERS: HandlerList = HandlerList()
    }
}
