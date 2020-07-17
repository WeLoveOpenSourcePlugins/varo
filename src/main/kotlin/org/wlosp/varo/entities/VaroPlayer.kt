package org.wlosp.varo.entities

import org.bukkit.Bukkit
import org.bukkit.Location
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object VaroPlayers : IdTable<UUID>("varo_players") {

    override val id: Column<EntityID<UUID>> = uuid("uuid").entityId()
    val team = reference("team_id", VaroTeams)
    val spawnLocationWorld = text("spawn_location_world")
    val spawnLocationX = double("spawn_location_x")
    val spawnLocationY = double("spawn_location_y")
    val spawnLocationZ = double("spawn_location_z")
    val spawnLocationYaw = float("spawn_location_yaw")
    val spawnLocationPitch = float("spawn_location_pitch")

    override val primaryKey: PrimaryKey = PrimaryKey(id)

}

class VaroPlayer(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, VaroPlayer>(VaroPlayers)

    val uuid: UUID
        get() = id.value

    var team by VaroTeam referencedOn VaroPlayers.team

    var spawnLocationWorld by VaroPlayers.spawnLocationWorld
    var spawnLocationX by VaroPlayers.spawnLocationX
    var spawnLocationY by VaroPlayers.spawnLocationY
    var spawnLocationZ by VaroPlayers.spawnLocationZ
    var spawnLocationYaw by VaroPlayers.spawnLocationYaw
    var spawnLocationPitch by VaroPlayers.spawnLocationPitch

    val location by lazy {
        Location(
            Bukkit.getWorld(spawnLocationWorld),
            spawnLocationX,
            spawnLocationY,
            spawnLocationZ,
            spawnLocationYaw,
            spawnLocationPitch
        )
    }
}
