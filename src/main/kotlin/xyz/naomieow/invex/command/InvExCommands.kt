package xyz.naomieow.invex.command

import com.bibireden.opc.api.OfflinePlayerCacheAPI
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.suggestion.SuggestionProvider
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.GameProfileArgument
import xyz.naomieow.invex.InvExPermissions
import xyz.naomieow.invex.gui.EndSeeGUI
import xyz.naomieow.invex.gui.InvSeeGUI

object InvExCommands {
    // TODO(naomieow): Custom ArgumentType that pulls from offline players too instead of
    // a string suggestion provider
    val SUGGEST_NAMES = SuggestionProvider<CommandSourceStack> { ctx, builder ->
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
        .requires(Permissions.require(InvExPermissions.INVSEE_COMMAND, 2))
        .then(
        Commands.argument("target", GameProfileArgument.gameProfile())
            .suggests(SUGGEST_NAMES)
            .executes(SeeCommand::executes { player, target ->
                InvSeeGUI(player, target)
            })
        )!!

    val endsee = Commands.literal("endsee")
        .requires(Permissions.require(InvExPermissions.ENDSEE_COMMAND, 2))
        .then(
            // TODO(naomieow): Custom ArgumentType that pulls from offline players too
        Commands.argument("target", GameProfileArgument.gameProfile())
            .suggests(SUGGEST_NAMES)
            .executes(SeeCommand::executes { player, target ->
                EndSeeGUI(player, target)
            })
        )!!

    fun registerCommands() {
        CommandRegistrationCallback.EVENT.register { dispatcher, registry, env ->
            dispatcher.register(invsee)
            dispatcher.register(endsee)
        }
    }
}