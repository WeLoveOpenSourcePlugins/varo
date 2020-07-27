package org.wlosp.varo.util

import org.bukkit.command.CommandSender
import org.bukkit.conversations.ConversationContext

val ConversationContext.sender: CommandSender
    get() = forWhom as CommandSender