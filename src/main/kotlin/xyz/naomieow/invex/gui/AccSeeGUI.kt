package xyz.naomieow.invex.gui

import eu.pb4.sgui.api.gui.SimpleGui
import io.wispforest.accessories.api.AccessoriesCapability
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.SimpleContainer
import net.minecraft.world.inventory.Slot
import xyz.naomieow.invex.InvExPermissions
import kotlin.collections.forEach

class AccSeeGUI(
    player: ServerPlayer,
    target: ServerPlayer,
    returnGui: SimpleGui? = null
): SeeGUI(
    player,
    target,
    SimpleContainer(accessoriesSlotCount(
        AccessoriesCapability.get(target)!!
    )),
    returnGui
) {
    val capability = AccessoriesCapability.get(target)!!

    override fun populate() {
        var slot = 0

        capability.containers.values.forEach { container ->
           for (i in 0..<container.accessories.containerSize) {
               setSlotRedirect(slot, Slot(container.accessories, i, 0, 0))
               slot++
           }
        }
    }

    override fun containerSize(): Int {
        return accessoriesSlotCount(capability)
    }

    override fun showAccessoriesButton(): Boolean {
        return false
    }

    override fun canModify(): Boolean {
        return super.canModify() && Permissions.check(player, InvExPermissions.ACCSEE_MODIFY, 2)
    }

    private companion object {
        private fun accessoriesSlotCount(capability: AccessoriesCapability): Int {
            var count = 0
            capability.containers.values.forEach { container ->
                count += container.accessories.containerSize
            }
            return count
        }
    }
}