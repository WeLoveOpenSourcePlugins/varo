package org.wlosp.varo.converstaion

import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.ValidatingPrompt
import kotlin.reflect.KClass

abstract class EnumPrompt<T : Enum<T>>(private val clazz: KClass<T>) : ValidatingPrompt() {

    private val values = clazz.java.enumConstants

    override fun isInputValid(context: ConversationContext, input: String): Boolean =
        values.any { (it as Enum<*>).name == input.toUpperCase() }

    protected fun parseEnum(input: String): T = java.lang.Enum.valueOf(clazz.java, input.toUpperCase())
}
