package org.wlosp.varo.listeners

import com.github.johnnyjayjay.spiglin.event.hear
import com.github.johnnyjayjay.spiglin.inventory.set
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.wlosp.varo.VaroPlugin
import org.wlosp.varo.configuration.Config

internal fun VaroPlugin.registerItemBlockListeners() {
    hear<PlayerDropItemEvent> {
        if (!it.player.isAllowedToUseItem(it.itemDrop.itemStack.type, varoConfig.blockedItems)) {
            it.itemDrop.remove()
        }
    }

    hear<EntityPickupItemEvent> {
        if ((it.entity as? Player)?.isAllowedToUseItem(it.item.itemStack.type, varoConfig.blockedItems) == false) {
            it.isCancelled = true
            it.item.remove()
        }
    }

    hear<BlockPlaceEvent> {
        if (!it.player.isAllowedToUseItem(it.blockPlaced.type, varoConfig.blockedItems)) {
            it.isCancelled = true
            it.player.sanitizeInventory(varoConfig.blockedItems)
        }
    }

    hear<BlockBreakEvent> {
        if (!it.player.isAllowedToUseItem(it.block.type, varoConfig.blockedItems)) {
            it.isCancelled = true
            if (varoConfig.itemBlockStrategy == Config.ItemBlockStrategy.DELETE) {
                it.block.setType(Material.AIR, false)
            }
        }
    }

    hear<PlayerJoinEvent> {
        val player = it.player
        player.sanitizeInventory(varoConfig.blockedItems)
        player.updateInventory()
    }
}

private fun Player.isAllowedToUseItem(type: Material?, blockedItems: List<Material>) =
    hasPermission("varo.bypassItemBlock") || type !in blockedItems

private fun Player.sanitizeInventory(blockedItems: List<Material>) {
    inventory.forEachIndexed { index, itemStack: ItemStack? ->
        if (isAllowedToUseItem(itemStack?.type, blockedItems)) {
            inventory[index] = null
        }
    }
}
