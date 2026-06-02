package xyz.naomieow.invex.gui

import eu.pb4.sgui.api.gui.SimpleGui
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.Slot
import xyz.naomieow.invex.InvExPermissions

class InvSeeGUI(
    player: ServerPlayer,
    target: ServerPlayer,
    returnGui: SimpleGui? = null
): SeeGUI(player, target, target.inventory, returnGui) {
    override fun populate()  {
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

    override fun canModify(): Boolean {
        return super.canModify() && Permissions.check(player, InvExPermissions.INVSEE_MODIFY, 2)
    }
}