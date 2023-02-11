package ru.melonhell.limboresourcepack

import com.google.inject.Inject
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.player.PlayerResourcePackStatusEvent.Status
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import net.elytrium.limboapi.api.Limbo
import net.elytrium.limboapi.api.LimboFactory
import net.elytrium.limboapi.api.chunk.Dimension
import net.elytrium.limboapi.api.event.LoginLimboRegisterEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.ComponentSerializer
import org.slf4j.Logger
import java.io.File
import java.nio.file.Path

@Plugin(
    id = "limboresourcepack",
    name = "LimboResourcePack",
    authors = ["MelonHell"],
    dependencies = [
        Dependency(id = "limboapi")
    ]
)
class LimboResourcePackPlugin @Inject constructor(
    val server: ProxyServer,
    private val logger: Logger,
    @DataDirectory private val dataDirectory: Path
) {

    private val factory =
        server.pluginManager.getPlugin("limboapi").flatMap(PluginContainer::getInstance).orElseThrow() as LimboFactory
    private lateinit var limbo: Limbo

    private val map = HashMap<Player, RpLimboHandler>()

    private val config = Config()

    private val configFile = File(dataDirectory.toFile(), "config.yml")

    private lateinit var serializer: ComponentSerializer<Component, Component, String>

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        config.reload(configFile)

        serializer = config.SERIALIZER.serializer!!

        val world = factory.createVirtualWorld(Dimension.THE_END, 0.0, 100.0, 0.0, 90f, 0f)
        limbo = factory.createLimbo(world)
    }

    @Subscribe(order = PostOrder.LATE)
    fun onLoginLimboRegister(event: LoginLimboRegisterEvent) {
        val player = event.player
        event.addOnJoinCallback {
            val handler = RpLimboHandler(this, logger)
            map[player] = handler
            limbo.spawnPlayer(player, handler)
        }
    }

    fun sendResourcePack(player: Player) {
        val packInfo = server.createResourcePackBuilder(config.RP_URL)
            .setShouldForce(config.RP_FORCE)
            .setPrompt(serializer.deserialize(config.RP_PROMPT_MESSAGE))
            .build()
        player.sendResourcePackOffer(packInfo)
    }


    fun onPlayerResourceStatus(player: Player, status: Status) {
        logger.info("Игрок ${player.username} отправил статус респака ${status.name}")
        when (status) {
            Status.SUCCESSFUL -> map.remove(player)?.disconnectLimbo()
            Status.DECLINED, Status.FAILED_DOWNLOAD -> {
                if (config.RP_FORCE) player.disconnect(serializer.deserialize(config.RP_FAILURE_KICK_MESSAGE))
                else map.remove(player)?.disconnectLimbo()
            }

            else -> Unit
        }
    }

    @Subscribe
    fun onDisconnect(event: DisconnectEvent) {
        map.remove(event.player)
    }
}