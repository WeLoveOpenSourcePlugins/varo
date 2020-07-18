package org.wlosp.varo.commands

import com.github.johnnyjayjay.spiglin.command.command
import org.wlosp.varo.VaroPlugin

internal fun VaroPlugin.registerVaroCommand() = command("varo") {
    subCommand("team") {
        varoTeamCreateCommand(this@registerVaroCommand)
        varoTeamDeleteCommand()
    }
    varoResetCommand()
    varoStartCommand(this@registerVaroCommand)
}