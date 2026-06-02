package xyz.naomieow.invex

import com.mojang.authlib.GameProfile
import com.mojang.serialization.Dynamic
import net.minecraft.Util
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtIo
import net.minecraft.nbt.NbtOps
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.storage.LevelResource
import java.io.File

actual fun Player.`invex$saveData`() {
    val dir = this.server?.getWorldPath(LevelResource.PLAYER_DATA_DIR)?.toFile()
    try {
        val tag = this.saveWithoutId(CompoundTag())
        val file = File.createTempFile("${this.stringUUID}.tmp", ".dat", dir)
        NbtIo.writeCompressed(tag, file)
        val current = File(dir, "${this.stringUUID}.dat")
        val old = File(dir, "${this.stringUUID}.dat_old")
        Util.safeReplaceFile(current, file, old)
    } catch (e: Exception) {
        InvEx.warn("Failed to save player ${this.name.string}'s data. Error: $e")
    }
}

actual fun MinecraftServer.getOfflinePlayer(gameProfile: GameProfile): ServerPlayer? {
    var targetPlayer = this.playerList.getPlayerByName(gameProfile.name)

    if (targetPlayer == null) {
        targetPlayer = this.playerList.getPlayerForLogin(gameProfile)
        val tag = this.playerList.load(targetPlayer)
        if (tag != null) {
            val level = this.getLevel(
                DimensionType.parseLegacy(
                    Dynamic(
                        NbtOps.INSTANCE,
                        tag.get("Dimension")
                    )
                ).result().get()
            )

            if (level != null) {
                targetPlayer.setServerLevel(level)
            }
        }
    }

    return targetPlayer
}

actual fun trinketsCompatLoaded(): Boolean {
    return InvEx.isModLoaded("tclayer")
}