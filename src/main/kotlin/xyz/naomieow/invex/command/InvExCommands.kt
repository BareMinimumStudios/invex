package xyz.naomieow.invex.command

import com.bibireden.opc.api.OfflinePlayerCacheAPI
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.serialization.Dynamic
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.GameProfileArgument
import net.minecraft.nbt.NbtOps
import net.minecraft.network.chat.Component
import net.minecraft.world.level.dimension.DimensionType
import xyz.naomieow.invex.gui.EndSeeGUI
import xyz.naomieow.invex.gui.InvSeeGUI

object InvExCommands {
    // TODO(naomieow): Custom ArgumentType that pulls from offline players too instead of
    // a string suggestion provider
    private val SUGGEST_NAMES = SuggestionProvider<CommandSourceStack> { ctx, builder ->
        val stringReader = StringReader(ctx.input)
        stringReader.cursor = builder.start
        val prefix = stringReader.readString()

        OfflinePlayerCacheAPI.getCache(ctx.source.server).usernames
            .filter { it.startsWith(prefix, true) }
            .forEach(builder::suggest)

        ctx.source.onlinePlayerNames
            .filter { it.startsWith(prefix, true) }
            .forEach(builder::suggest)

        builder.buildFuture()
    }

    val invsee = Commands.literal("invsee")
        .requires { source -> source.hasPermission(2) }
        .then(
        Commands.argument("target", GameProfileArgument.gameProfile())
            .suggests(SUGGEST_NAMES)
            .executes { ctx ->
                val profiles = GameProfileArgument.getGameProfiles(ctx, "target")
                // Really shitty implementation
                if (profiles.size > 1) {
                    ctx.source.sendFailure(Component.literal("Only one player is allowed, but the provided selector allows more than one"))
                    return@executes 0
                }
                val target = profiles.first()
                var player = ctx.source.server.playerList.getPlayerByName(target.name)

                if (player == null) {
                    player = ctx.source.server.playerList.getPlayerForLogin(target)
                    val tag = ctx.source.server.playerList.load(player)
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
                            player.setLevel(level)
                        }
                    }
                }
                val gui = InvSeeGUI(ctx.source.player!!, player)
                gui.open()
                1
            }
        )!!

    val endsee = Commands.literal("endsee")
        .requires { source -> source.hasPermission(2) }
        .then(
            // TODO(naomieow): Custom ArgumentType that pulls from offline players too
            Commands.argument("target", EntityArgument.player())
                .executes { ctx ->
                    val target = EntityArgument.getPlayer(ctx, "target")
                    val gui = EndSeeGUI(ctx.source.player!!, target)

                    ctx.source.sendSuccess({
                        target.name
                    }, false)
                    gui.open()
                    1
                }
        )!!

    fun registerCommands() {
        CommandRegistrationCallback.EVENT.register { dispatcher, registry, env ->
            dispatcher.register(invsee)
            dispatcher.register(endsee)
        }
    }
}