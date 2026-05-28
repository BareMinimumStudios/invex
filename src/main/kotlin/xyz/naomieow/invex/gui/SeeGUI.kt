package xyz.naomieow.invex.gui

import eu.pb4.sgui.api.ClickType
import eu.pb4.sgui.api.elements.GuiElement
import eu.pb4.sgui.api.elements.GuiElementBuilder
import eu.pb4.sgui.api.gui.SimpleGui
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Items
import xyz.naomieow.invex.InvEx
import xyz.naomieow.invex.InvExPermissions
import xyz.naomieow.invex.`invex$saveData`

abstract class SeeGUI(
    player: ServerPlayer,
    val target: ServerPlayer,
    val container: Container,
    val returnGui: SimpleGui? = null
): SimpleGui(
    getMenuType(container.containerSize),
    player,
    false
) {
    protected val maxSize = 45

    override fun open(): Boolean {
        populate()

        if (containerSize() > maxSize) {
            InvEx.warn("Container size is too large. Expected <=$maxSize but got ${containerSize()}")
            return false
        }

        var slot = containerSize() + ((45 - containerSize()) % 9)
        InvEx.warn("Slot: $slot")

        for (i in containerSize()..<slot) {
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
                .setName(Component.literal("Return"))
                .setCallback { i, type, action ->
                    returnGui.open()
                }
            )
            slot++
        }

        if (showInventoryButton()) {
            setSlot(slot, GuiElementBuilder()
                .setItem(Items.CHEST)
                .glow()
                .setName(Component.literal("Open Inventory"))
                .setCallback { i, type, action ->
                    val gui = InvSeeGUI(player, target, this)
                    gui.open()
                }
            )
            slot++
        }

        if (showEnderChestButton()) {
            setSlot(
                slot, GuiElementBuilder()
                    .setItem(Items.ENDER_CHEST)
                    .glow()
                    .setName(Component.literal("Open Ender Chest"))
                    .setCallback { i, type, action ->
                        val gui = EndSeeGUI(player, target, this)
                        gui.open()
                    }
            )
            slot++
        }

        if (showTrinketsButton()) {
            setSlot(
                slot, GuiElementBuilder()
                    .setItem(Items.GOLDEN_HELMET)
                    .glow()
                    .setName(Component.literal("Open Trinkets"))
                    .setCallback { i, type, action ->
                        val gui = TrinketSeeGUI(player, target, this)
                        gui.open()
                    }
            )
            slot++
        }

        for (i in slot..<this.size) {
            setSlot(i, GuiElementBuilder()
                .setItem(Items.LIGHT_BLUE_STAINED_GLASS_PANE)
                .setName(Component.literal(""))
            )
        }

        return super.open()
    }

    abstract fun populate()

    open fun containerSize(): Int {
        return container.containerSize
    }

    open fun showTrinketsButton(): Boolean {
        return InvEx.isModLoaded("trinkets")
               && Permissions.check(player, "invex.command.trinketsee", 2)
    }

    open fun showInventoryButton(): Boolean {
        return Permissions.check(player, "invex.command.invsee", 2)
    }

    open fun showEnderChestButton(): Boolean {
        return Permissions.check(player, "invex.command.endsee", 2)
    }

    override fun onAnyClick(index: Int, type: ClickType?, action: net.minecraft.world.inventory.ClickType?): Boolean {
        target.`invex$saveData`()
        return super.onAnyClick(index, type, action)
    }

    open fun canModify(): Boolean {
        return !Permissions.check(target, InvExPermissions.IMMUNE_MODIFY, 3)
    }

    fun conditionalSlotRedirect(index: Int, slot: Slot, condition: Boolean) {
        if (condition) {
            setSlotRedirect(index, slot)
        } else {
            setSlot(index, GuiElement(slot.item, ::onAnyClick))
        }
    }

    protected companion object {
        protected fun getMenuType(size: Int): MenuType<*> {
            var res = MenuType.GENERIC_9x1

            if (size >= 1) {
                res = MenuType.GENERIC_9x2
            }
            if (size >= 10) {
                res = MenuType.GENERIC_9x3
            }
            if (size >= 19) {
                res = MenuType.GENERIC_9x4
            }
            if (size >= 28) {
                res = MenuType.GENERIC_9x5
            }
            if (size >= 37) {
                res = MenuType.GENERIC_9x6
            }

            return res
        }
    }
}