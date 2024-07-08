package me.chaoticwagon.vivarium.instance

import net.minestom.server.instance.InstanceContainer
import net.minestom.server.world.DimensionType
import java.util.UUID

class GameInstance(): InstanceContainer(UUID.randomUUID(), DimensionType.OVERWORLD) {
}