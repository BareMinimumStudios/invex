package xyz.naomieow.invex.command

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import xyz.naomieow.invex.gui.EndSeeGUI
import xyz.naomieow.invex.gui.InvSeeGUI

object InvExCommands {
    val invsee = Commands.literal("invsee")
        .requires { source -> source.hasPermission(2) }
        .then(
            // TODO(naomieow): Custom ArgumentType that pulls from offline players too
            Commands.argument("target", EntityArgument.player())
                .executes { ctx ->
                    val target = EntityArgument.getPlayer(ctx, "target")
                    val gui = InvSeeGUI(ctx.source.player!!, target)

                    ctx.source.sendSuccess({
                        target.name
                    }, false)
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