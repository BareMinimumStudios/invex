package xyz.naomieow.invex.gui

import eu.pb4.sgui.api.gui.SimpleGui
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.inventory.Slot
import xyz.naomieow.invex.InvExPermissions

class EndSeeGUI(
    player: ServerPlayer,
    target: ServerPlayer,
    returnGui: SimpleGui? = null
): SeeGUI(player, target, target.enderChestInventory, returnGui) {
    override fun populate() {
        title = Component.literal("Viewing ${target.name.string}'s Ender Chest")
        for (i in 0..<target.enderChestInventory.containerSize) {
            conditionalSlotRedirect(i, Slot(target.enderChestInventory, i, 0, 0), canModify())
        }
    }

    override fun showEnderChestButton(): Boolean {
        return false
    }


    override fun canModify(): Boolean {
        return super.canModify() && Permissions.check(player, InvExPermissions.ENDSEE_MODIFY, 2)
    }
}