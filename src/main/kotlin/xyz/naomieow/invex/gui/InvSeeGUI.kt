package xyz.naomieow.invex.gui

import eu.pb4.sgui.api.gui.SimpleGui
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.Slot

class InvSeeGUI(
    player: ServerPlayer,
    target: ServerPlayer,
    returnGui: SimpleGui? = null
): SeeGUI(player, target, target.inventory, returnGui) {
    override fun populate()  {
        title = Component.literal("Viewing ${target.name.string}'s Inventory")
        for (i in 0..<target.inventory.containerSize) {
            if (Inventory.isHotbarSlot(i)) {
                setSlotRedirect(i + 27, Slot(target.inventory, i, 0, 0))
            } else if (i >=target.inventory.containerSize - 5) {
                // Check if armour/offhand slot
                setSlotRedirect(i, Slot(target.inventory, i, 0, 0))
            }else {
                setSlotRedirect(i - 9, Slot(target.inventory, i, 0, 0))
            }
        }
    }

    override fun showInventoryButton(): Boolean {
        return false
    }
}