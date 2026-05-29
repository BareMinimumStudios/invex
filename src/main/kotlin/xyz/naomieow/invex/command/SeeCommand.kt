package xyz.naomieow.invex.command

import com.mojang.brigadier.context.CommandContext
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.GameProfileArgument
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import xyz.naomieow.invex.InvExPermissions
import xyz.naomieow.invex.gui.SeeGUI
import kotlin.collections.first
import xyz.naomieow.invex.getOfflinePlayer

object SeeCommand {
    fun executes(guiFn: (player: ServerPlayer, target: ServerPlayer) -> SeeGUI): (CommandContext<CommandSourceStack>) -> Int {
        return cb@ { ctx ->
            val profiles = GameProfileArgument.getGameProfiles(ctx, "target")
            // Really shitty implementation
            if (profiles.size > 1) {
                ctx.source.sendFailure(Component.literal("Only one player is allowed, but the provided selector allows more than one"))
                return@cb 0
            }

            val targetPlayerProfile = profiles.first()
            val targetPlayer = ctx.source.server.getOfflinePlayer(targetPlayerProfile)!!

            if (Permissions.check(targetPlayer, InvExPermissions.IMMUNE_VIEW, 3)) {
                ctx.source.sendFailure(Component.literal("Player ${targetPlayer.name.string} is immune to being viewed."))
                return@cb 0
            }

            val gui = guiFn(ctx.source.player!!, targetPlayer)
            if (gui.open()) {
                return@cb 1
            }
            ctx.source.sendFailure(Component.literal("Failed to open menu."))
            return@cb 0
        }
    }


}