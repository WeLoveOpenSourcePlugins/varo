package org.wlosp.varo.api

import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.transactions.transaction
import org.wlosp.varo.api.entities.VaroPlayer
import org.wlosp.varo.api.entities.VaroTeam
import org.wlosp.varo.entities.DatabaseVaroPlayer
import org.wlosp.varo.entities.DatabaseVaroTeam
import java.util.*

class VaroApiImpl : VaroInterface {

    init {
        APIInstance.instance = this
    }

    override val teams: VaroInterface.TeamApi = TeamsApiImpl()
    override val players: VaroInterface.PlayerApi = PlayersApiImpl()

    private class TeamsApiImpl : VaroInterface.TeamApi {
        override val teams: List<VaroTeam>
            get() = transaction {
                DatabaseVaroTeam.all().toList()
            }

        override fun get(name: String): VaroTeam? = transaction {
            DatabaseVaroTeam.findById(name)?.load(DatabaseVaroTeam::internalPlayers)
        }
    }

    private class PlayersApiImpl : VaroInterface.PlayerApi {
        override val players: List<VaroPlayer>
            get() = transaction { DatabaseVaroPlayer.all().map { it.load(DatabaseVaroPlayer::team) } }
        override val onlinePlayers: List<VaroPlayer>
            get() = players.filterNot { it.toPlayer() == null }

        override fun get(uuid: UUID): VaroPlayer? = transaction {
            DatabaseVaroPlayer.findById(uuid)
        }
    }
}
