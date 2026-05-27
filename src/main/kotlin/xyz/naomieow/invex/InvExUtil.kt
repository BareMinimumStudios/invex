package xyz.naomieow.invex

import net.minecraft.Util
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtIo
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.storage.LevelResource
import java.io.File

fun Player.`invex$saveData`() {
    InvEx.info("Saving data!")
    val dir = this.server?.getWorldPath(LevelResource.PLAYER_DATA_DIR)?.toFile()
    try {
        val tag = this.saveWithoutId(CompoundTag())
        InvEx.info("Tag: $tag")
        val file = File.createTempFile("${this.stringUUID}.tmp", ".dat", dir)
        NbtIo.writeCompressed(tag, file)
        val current = File(dir, "${this.stringUUID}.dat")
        val old = File(dir, "${this.stringUUID}.dat_old")
        Util.safeReplaceFile(current, file, old)
    } catch (e: Exception) {
        InvEx.warn("Failed to save player ${this.name.string}'s data. Error: $e")
    }
}