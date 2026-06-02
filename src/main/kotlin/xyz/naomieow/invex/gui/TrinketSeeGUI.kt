package xyz.naomieow.invex.gui

import dev.emi.trinkets.api.TrinketComponent
import dev.emi.trinkets.api.TrinketsApi
import eu.pb4.sgui.api.gui.SimpleGui
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.SimpleContainer
import net.minecraft.world.inventory.Slot
import xyz.naomieow.invex.InvExPermissions
import kotlin.collections.forEach

class TrinketSeeGUI(
    player: ServerPlayer,
    target: ServerPlayer,
    returnGui: SimpleGui? = null
): SeeGUI(
    player,
    target,
    SimpleContainer(trinketSlotCount(
        TrinketsApi.getTrinketComponent(target).get()
    )),
    returnGui
) {
    val component = TrinketsApi.getTrinketComponent(target).get()

    override fun populate() {
        var slot = 0
        component.inventory.values.forEach { map ->
            map.values.forEach { inventory ->
                for (i in 0..<inventory.containerSize) {
                    setSlotRedirect(slot, Slot(inventory, i, 0, 0))
                    slot++
                }
            }
        }
    }

    override fun containerSize(): Int {
        return trinketSlotCount(component)
    }

    override fun showTrinketsButton(): Boolean {
        return false
    }

    override fun canModify(): Boolean {
        return super.canModify() && Permissions.check(player, InvExPermissions.TRINKETSEE_MODIFY, 2)
    }

    private companion object {
        private fun trinketSlotCount(component: TrinketComponent): Int {
            var count = 0
            component.inventory.values.forEach { map ->
                map.values.forEach { inventory ->
                    count += inventory.containerSize
                }
            }
            return count
        }
    }
}