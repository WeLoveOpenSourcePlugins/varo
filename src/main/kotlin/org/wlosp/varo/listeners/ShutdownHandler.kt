package org.wlosp.varo.listeners

import com.github.johnnyjayjay.spiglin.event.hear
import com.github.johnnyjayjay.spiglin.onlinePlayers
import com.github.johnnyjayjay.spiglin.scheduler.run
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import org.bukkit.event.player.PlayerLoginEvent
import org.wlosp.varo.VaroPlugin
import org.wlosp.varo.util.toHoursPart
import org.wlosp.varo.util.toMinutesPart
import java.time.*

internal fun VaroPlugin.registerShutdownHandler() {

    val date = LocalDate.now(varoConfig.time.timezone)
    val joinDate = with(varoConfig.time.startTime) {
        OffsetDateTime.of(
            date,
            LocalTime.of(toHoursPart(), toMinutesPart()),
            varoConfig.time.timezone.rules.getOffset(LocalDateTime.now())
        )
    }

    val kickDate =
        with(varoConfig.time.shutdownTime) {
            OffsetDateTime.of(
                date,
                LocalTime.of(toHoursPart(), toMinutesPart()),
                varoConfig.time.timezone.rules.getOffset(LocalDateTime.now())
            )
        }

    GlobalScope.launch {
        delay(Duration.between(OffsetDateTime.now(), kickDate))
        onlinePlayers.forEach {
            if (!it.hasPermission("varo.joinbypass")) {
                this@registerShutdownHandler.run {
                    it.kickPlayer("pDer server schließt!")
                }
            }
        }
    }

    hear<PlayerLoginEvent> { event ->
        val now = OffsetDateTime.ofInstant(Instant.now(), varoConfig.time.timezone)
        if (now.isBefore(joinDate) or now.isAfter(kickDate)) {
            if (!event.player.hasPermission("varo.joinbypass")) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Varo server is closed!")
            }
        }
    }
}