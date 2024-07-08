package me.chaoticwagon.vivarium

import me.chaoticwagon.vivarium.instance.GameInstance
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit

fun main() {
    Vivarium(MinecraftServer.init()).start()
}

class Vivarium(private val minecraftServer: MinecraftServer) {

    val playerEventNode = EventNode.type("player-listener", EventFilter.PLAYER)
    val scheduler = MinecraftServer.getSchedulerManager()

    fun start() {
        val instanceManager = MinecraftServer.getInstanceManager()

        MojangAuth.init()

        val instance = GameInstance()
        instanceManager.registerInstance(instance)
        instance.setGenerator { unit: GenerationUnit ->
            unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK)
        }

        registerListeners(instance)
        registerCommands()

        minecraftServer.start("0.0.0.0", 25565)
    }

    private fun registerListeners(instance: GameInstance) {
        val globalEventHandler = MinecraftServer.getGlobalEventHandler()
        playerEventNode.addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
            val player: Player = event.player
            event.spawningInstance = instance
            player.permissionLevel = 4
            player.respawnPoint = Pos(0.0, 42.0, 0.0, 180.0f, 0.0f)
        }

        val instanceEventNode = EventNode.type("instance-listener", EventFilter.INSTANCE)

        globalEventHandler.addChild(instanceEventNode)
        globalEventHandler.addChild(playerEventNode)
    }

    private fun registerCommands() {
        val cmdManager = MinecraftServer.getCommandManager()
    }
}