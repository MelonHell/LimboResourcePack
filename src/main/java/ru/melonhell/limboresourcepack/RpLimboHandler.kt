package ru.melonhell.limboresourcepack

import com.velocitypowered.proxy.protocol.packet.ResourcePackResponse
import net.elytrium.limboapi.api.Limbo
import net.elytrium.limboapi.api.LimboSessionHandler
import net.elytrium.limboapi.api.player.LimboPlayer
import org.slf4j.Logger

class RpLimboHandler(
    private val plugin: LimboResourcePackPlugin,
    private val logger: Logger
) : LimboSessionHandler {
    private lateinit var player: LimboPlayer
    override fun onSpawn(server: Limbo, player: LimboPlayer) {
        this.player = player
        this.player.disableFalling()
        plugin.sendResourcePack(player.proxyPlayer)

        logger.info("Игрок ${player.proxyPlayer.username} попал в лимбо респака")
    }

    override fun onGeneric(packet: Any) {
        if (packet is ResourcePackResponse) {
            plugin.onPlayerResourceStatus(player.proxyPlayer, packet.status)
        }
    }

    override fun onDisconnect() {
        logger.info("Игрок ${player.proxyPlayer.username} вышел из лимбо респака")
    }

    fun disconnectLimbo() = player.disconnect()
}