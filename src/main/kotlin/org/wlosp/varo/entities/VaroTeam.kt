package org.wlosp.varo.entities

import org.bukkit.ChatColor
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object VaroTeams : IdTable<String>("teams") {
    override val id: Column<EntityID<String>> = text("name").entityId()
    val color = enumeration("color", ChatColor::class)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

class VaroTeam(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, VaroTeam>(VaroTeams)

    val name
        get() = id.value
    var color by VaroTeams.color
}
