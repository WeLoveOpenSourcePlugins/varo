package org.wlosp.varo.converstaion

import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.conversations.*
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.wlosp.varo.entities.VaroPlayer
import org.wlosp.varo.entities.VaroPlayers
import org.wlosp.varo.util.AccountUtil
import java.util.*
import java.util.regex.Pattern

@Suppress("UNCHECKED_CAST") // it works trust me
internal var ConversationContext.locations: MutableMap<UUID, Location>
    get() = allSessionData["locations"] as MutableMap<UUID, Location>
    set(value) {
        allSessionData["locations"] = value
    }

internal var ConversationContext.color: ChatColor
    get() = allSessionData["color"] as ChatColor
    set(value) {
        allSessionData["color"] = value
    }

class StartPrompt(private val teamName: String) : MessagePrompt() {
    override fun getNextPrompt(context: ConversationContext): Prompt? {
        context.locations = mutableMapOf()
        return ColorPrompt
    }

    override fun getPromptText(context: ConversationContext): String = """
        Du erstellst das team $teamName
    """.trimIndent()
}

class PlayerNamePrompt(private val playerIndex: Int = 1) :
    RegexPrompt(Pattern.compile("[a-zA-Z0-9_]{3,16}")) {

    override fun acceptInput(context: ConversationContext, input: String?): Prompt? {
        if (input == "-finish") return null
        return super.acceptInput(context, input)
    }

    override fun acceptValidatedInput(context: ConversationContext, input: String): Prompt? {
        val uuid = AccountUtil.fetchUUID(input) ?: return ErrorPrompt(getFailedValidationText(context, input), this)
        if (uuid in context.locations) return ErrorPrompt("Dieser spieler ist bereits in diesem team", this)
        val foundPlayer = transaction { VaroPlayer.findById(uuid) }
        if (foundPlayer != null) {
            return ErrorPrompt("Dieser spieler ist bereits in einem team", this)
        }
        return SpawnLocationPrompt(playerIndex, uuid = uuid)
    }

    override fun getPromptText(context: ConversationContext): String {
        return "Bitte gebe den Namen von Spieler $playerIndex ein."
    }

    override fun getFailedValidationText(context: ConversationContext, invalidInput: String): String {
        return "Dieser name war falsch"
    }


}

object ColorPrompt : EnumPrompt<ChatColor>(ChatColor::class) {

    override fun acceptValidatedInput(context: ConversationContext, input: String): Prompt? {
        context.color = parseEnum(input)
        return PlayerNamePrompt()
    }

    override fun getPromptText(context: ConversationContext): String = "bitte farbe angeben"

}

class SpawnLocationPrompt(
    private val playerIndex: Int = 1,
    private val uuid: UUID
) :
    FixedSetPrompt("here") {

    override fun acceptValidatedInput(context: ConversationContext, input: String): Prompt? {
        return when (input) {
            "here" -> {
                val location = (context.forWhom as Player).location
                if (context.locations.containsValue(location)) {
                    return ErrorPrompt("Diese location wurde bereits verwendet", this)
                }
                val foundLocation =
                    transaction {
                        VaroPlayer.find { (VaroPlayers.spawnLocationX eq location.x) and (VaroPlayers.spawnLocationY eq location.y) and (VaroPlayers.spawnLocationZ eq location.z) }
                            .firstOrNull()
                    }
                if (foundLocation != null) {
                    return ErrorPrompt("Diese location wurde bereits verwendet", this)
                }
                context.locations[uuid] = location
                PlayerNamePrompt(playerIndex + 1)
            }
            else -> null
        }
    }

    override fun getPromptText(context: ConversationContext): String {
        return """Bitte gehe zur spawn location für user $playerIndex und gebe "here" ein"""
    }
}

class ErrorPrompt(private val text: String, private val nextPrompt: Prompt) : MessagePrompt() {
    override fun getNextPrompt(context: ConversationContext): Prompt? = nextPrompt

    override fun getPromptText(context: ConversationContext): String = text

}
