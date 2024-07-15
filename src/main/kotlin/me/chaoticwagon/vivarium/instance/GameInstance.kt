package me.chaoticwagon.vivarium.instance

import net.minestom.server.MinecraftServer
import net.minestom.server.instance.InstanceContainer
import java.util.*

class GameInstance(): InstanceContainer(UUID.randomUUID(),
    MinecraftServer.getDimensionTypeRegistry().getKey(Dimension.NORMAL.type)!!
) {
}