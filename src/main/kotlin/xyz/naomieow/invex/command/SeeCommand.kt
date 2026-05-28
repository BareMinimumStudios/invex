package xyz.naomieow.invex.command

import com.mojang.brigadier.context.CommandContext
import com.mojang.serialization.Dynamic
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.GameProfileArgument
import net.minecraft.nbt.NbtOps
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.dimension.DimensionType
import xyz.naomieow.invex.InvExPermissions
import xyz.naomieow.invex.gui.SeeGUI

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
            var targetPlayer = ctx.source.server.playerList.getPlayerByName(targetPlayerProfile.name)

            if (targetPlayer == null) {
                targetPlayer = ctx.source.server.playerList.getPlayerForLogin(targetPlayerProfile)
                val tag = ctx.source.server.playerList.load(targetPlayer)
                if (tag != null) {
                    val level = ctx.source.server.getLevel(
                        DimensionType.parseLegacy(
                            Dynamic(
                                NbtOps.INSTANCE,
                                tag.get("Dimension")
                            )
                        ).result().get()
                    )

                    if (level != null) {
                        targetPlayer.setLevel(level)
                    }
                }
            }

            if (Permissions.check(targetPlayer, InvExPermissions.IMMUNE_VIEW, 3)) {
                ctx.source.sendFailure(Component.literal("Player ${targetPlayer.name} is immune to being viewed."))
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