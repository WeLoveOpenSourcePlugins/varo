package org.wlosp.varo.commands

import com.github.johnnyjayjay.spiglin.command.CommandContext
import org.bukkit.ChatColor

fun CommandContext.withPermission(permission: String, command: (CommandContext) -> Boolean): Boolean {
    if (!sender.hasPermission(permission)) {
        sender.sendMessage(ChatColor.RED.toString() + "No Permission")
        return true
    }

    return command(this)
}