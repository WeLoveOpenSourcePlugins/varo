package org.wlosp.varo.api.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.wlosp.varo.api.entities.VaroPlayer
import org.wlosp.varo.api.entities.VaroTeam

class VaroTeamDiedEvent(
    val team: VaroTeam,
    val cause: Cause
) : Event() {
    override fun getHandlers(): HandlerList = HANDLERS

    val players: List<VaroPlayer>
        get() = team.players


    enum class Cause {
        STRIKE,
        DEATH
    }

    companion object {
        @JvmStatic
        @get:JvmName("getHandlersList")
        val HANDLERS: HandlerList = HandlerList()
    }
}
