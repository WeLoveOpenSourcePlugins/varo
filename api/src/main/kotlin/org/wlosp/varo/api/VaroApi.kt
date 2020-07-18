package org.wlosp.varo.api

import kotlin.properties.Delegates

/**
 * Singleton for Varo API.
 *
 * @see VaroInterface for API documentation
 */
object VaroApi : VaroInterface by APIInstance.instance

object VaroTeamApi : VaroTeamApiInterface by VaroApi.teams

object VaroPlayerApi : VaroPlayerApiInterface by VaroApi.players
