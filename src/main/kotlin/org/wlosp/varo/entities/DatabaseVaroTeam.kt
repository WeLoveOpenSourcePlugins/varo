package org.wlosp.varo.entities

import org.bukkit.ChatColor
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.transactions.transaction
import org.wlosp.varo.api.entities.VaroPlayer
import org.wlosp.varo.api.entities.VaroTeam

object VaroTeams : IdTable<String>("teams") {
    override val id: Column<EntityID<String>> = text("name").entityId()
    val color: Column<ChatColor> = enumeration("color", ChatColor::class)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

class DatabaseVaroTeam(id: EntityID<String>) : Entity<String>(id), VaroTeam {
    companion object : EntityClass<String, DatabaseVaroTeam>(VaroTeams)

    override val name: String
        get() = id.value
    override var color: ChatColor by VaroTeams.color

    internal val internalPlayers: SizedIterable<VaroPlayer> by DatabaseVaroPlayer referrersOn VaroPlayers.team

    override val players: List<VaroPlayer>
        get() = internalPlayers.toList()
}
