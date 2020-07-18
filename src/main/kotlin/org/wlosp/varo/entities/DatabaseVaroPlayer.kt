package org.wlosp.varo.entities

import org.bukkit.Bukkit
import org.bukkit.Location
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.wlosp.varo.api.entities.VaroPlayer
import java.util.*

object VaroPlayers : IdTable<UUID>("varo_players") {

    override val id: Column<EntityID<UUID>> = uuid("uuid").entityId()
    val team: Column<EntityID<String>> = reference("team_id", VaroTeams)
    val isAlive: Column<Boolean> = bool("is_alive").default(true)
    val spawnLocationWorld: Column<String> = text("spawn_location_world")
    val spawnLocationX: Column<Double> = double("spawn_location_x")
    val spawnLocationY: Column<Double> = double("spawn_location_y")
    val spawnLocationZ: Column<Double> = double("spawn_location_z")
    val spawnLocationYaw: Column<Float> = float("spawn_location_yaw")
    val spawnLocationPitch: Column<Float> = float("spawn_location_pitch")

    override val primaryKey: PrimaryKey = PrimaryKey(id)

}

class DatabaseVaroPlayer(id: EntityID<UUID>) : Entity<UUID>(id), VaroPlayer {
    companion object : EntityClass<UUID, DatabaseVaroPlayer>(VaroPlayers)

    override val uuid: UUID
        get() = id.value

    override var team: DatabaseVaroTeam by DatabaseVaroTeam referencedOn VaroPlayers.team
    override var isAlive: Boolean by VaroPlayers.isAlive

    var spawnLocationWorld: String by VaroPlayers.spawnLocationWorld
    var spawnLocationX: Double by VaroPlayers.spawnLocationX
    var spawnLocationY: Double by VaroPlayers.spawnLocationY
    var spawnLocationZ: Double by VaroPlayers.spawnLocationZ
    var spawnLocationYaw: Float by VaroPlayers.spawnLocationYaw
    var spawnLocationPitch: Float by VaroPlayers.spawnLocationPitch

    override val spawnLocation: Location by lazy {
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
