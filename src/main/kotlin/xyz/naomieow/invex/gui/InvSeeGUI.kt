package xyz.naomieow.invex.gui

import eu.pb4.sgui.api.elements.GuiElementBuilder
import eu.pb4.sgui.api.gui.SimpleGui
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Items

class InvSeeGUI(
    player: ServerPlayer,
    val target: ServerPlayer,
    val returnGui: SimpleGui? = null
): SimpleGui(MenuType.GENERIC_9x6, player, false) {
    override fun open(): Boolean {
        title = Component.literal("Viewing ${target.name.string}'s Inventory")
        populate()

        var slot = 45

        for (i in target.inventory.containerSize..<slot) {
            setSlot(i, GuiElementBuilder()
                .setItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE)
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
                .setName(Component.literal("Return"))
                .setCallback { i, type, action ->
                    returnGui.open()
                }
            )
            slot++
        }

        setSlot(slot, GuiElementBuilder()
            .setItem(Items.ENDER_CHEST)
            .glow()
            .setName(Component.literal("Open Ender Chest"))
            .setCallback { i, type, action ->
                val gui = EndSeeGUI(player, target, this)
                gui.open()
            }
        )
        slot++

        for (i in slot..<this.size) {
            setSlot(i, GuiElementBuilder()
                .setItem(Items.LIGHT_GRAY_STAINED_GLASS_PANE)
                .setName(Component.literal(""))
            )
        }

        return super.open()
    }
    fun populate() {
        for (i in 0..<target.inventory.containerSize) {
            if (Inventory.isHotbarSlot(i)) {
                setSlotRedirect(i + 27, Slot(target.inventory, i, 0, 0))
            } else if (i >=target.inventory.containerSize - 5) {
                // Check if armour/offhand slot
                setSlotRedirect(i, Slot(target.inventory, i, 0, 0))
            }else {
                setSlotRedirect(i - 9, Slot(target.inventory, i, 0, 0))
            }
//            var item = target.inventory.getItem(i)
//            setSlot(i - 9, GuiElement(item, ::clickSlot))
        }
    }
}