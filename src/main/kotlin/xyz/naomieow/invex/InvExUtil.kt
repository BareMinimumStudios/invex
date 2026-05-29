package xyz.naomieow.invex

import com.mojang.authlib.GameProfile
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

expect fun Player.`invex$saveData`()
expect fun MinecraftServer.getOfflinePlayer(gameProfile: GameProfile): ServerPlayer?