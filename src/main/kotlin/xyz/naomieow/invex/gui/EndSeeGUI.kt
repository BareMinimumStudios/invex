package xyz.naomieow.invex.gui

import eu.pb4.sgui.api.ClickType
import eu.pb4.sgui.api.elements.GuiElementBuilder
import eu.pb4.sgui.api.gui.SimpleGui
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Items
import xyz.naomieow.invex.`invex$saveData`

class EndSeeGUI(
    player: ServerPlayer,
    val target: ServerPlayer,
    val returnGui: SimpleGui? = null
): SimpleGui(MenuType.GENERIC_9x4, player, false) {
    override fun open(): Boolean {
        title = Component.literal("Viewing ${target.name.string}'s Ender Chest")
        populate()
        var slot = 27

        for (i in target.enderChestInventory.containerSize..<slot) {
            setSlot(i, GuiElementBuilder()
                .setItem(Items.LIGHT_BLUE_STAINED_GLASS_PANE)
                .setName(Component.literal(""))
            )
        }

        setSlot(slot, GuiElementBuilder()
            .setItem(Items.BARRIER)
            .glow()
            .setName(Component.literal("Exit"))
            .setCallback { i, type, action ->
                this.close()
            }
        )
        slot++

        if (returnGui != null) {
            setSlot(slot, GuiElementBuilder()
                .setItem(Items.STRUCTURE_VOID)
                .glow()
                .setName(Component.literal("Back"))
                .setCallback { i, type, action ->
                    returnGui.open()
                }
            )
            slot++
        }

        setSlot(slot, GuiElementBuilder()
            .setItem(Items.CHEST)
            .glow()
            .setName(Component.literal("Inventory"))
            .setCallback { i, type, action ->
                val gui = InvSeeGUI(player, target, this)
                gui.open()
            }
        )
        slot++

        for (i in slot..<this.size) {
            setSlot(i, GuiElementBuilder()
                .setItem(Items.LIGHT_BLUE_STAINED_GLASS_PANE)
                .setName(Component.literal(""))
            )
        }

        return super.open()
    }

    fun populate() {
        for (i in 0..<target.enderChestInventory.containerSize) {
            setSlotRedirect(i, Slot(target.enderChestInventory, i, 0, 0))
        }
    }

    override fun onAnyClick(index: Int, type: ClickType?, action: net.minecraft.world.inventory.ClickType?): Boolean {
        target.`invex$saveData`()
        return super.onAnyClick(index, type, action)
    }
}