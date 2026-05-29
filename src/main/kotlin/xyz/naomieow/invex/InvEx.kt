package xyz.naomieow.invex

import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xyz.naomieow.invex.command.InvExCommands

const val MOD_ID: String = "assets/invex"
const val MOD_NAME: String = "InvEx"

object InvEx :
	ModInitializer,
	Logger by LoggerFactory.getLogger(MOD_NAME)
{
	override fun onInitialize() {
		InvExCommands.registerCommands()
		info("Peeping at player's inventories since 1984!")
	}

	fun isModLoaded(id: String): Boolean {
		return FabricLoader.getInstance().isModLoaded(id)
	}
}