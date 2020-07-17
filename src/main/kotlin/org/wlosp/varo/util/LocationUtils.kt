package org.wlosp.varo.util

import org.bukkit.Bukkit
import org.bukkit.Location

fun Location.toSerializedString() = "$world:$x:$y:$z:$yaw:$pitch"

fun String.toLocation() = with(split(':')) {
    val (worldName, x, y, z) = this
    val yaw = getOrNull(3)?.toFloat() ?: 0F
    val pitch = getOrNull(4)?.toFloat() ?: 0F

    val world = Bukkit.getWorld(worldName)
    Location(world, x.toDouble(), y.toDouble(), z.toDouble(), yaw, pitch)
}