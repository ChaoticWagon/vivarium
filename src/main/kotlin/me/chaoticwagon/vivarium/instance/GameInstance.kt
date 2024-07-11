package me.chaoticwagon.vivarium.instance

import net.minestom.server.entity.Player
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.world.DimensionType
import java.util.UUID

class GameInstance : InstanceContainer(UUID.randomUUID(), DimensionType.OVERWORLD) {
    val what: HashMap<Player, Pair<Int, Int>> = HashMap()

    override fun tick(time: Long) {
        super.tick(time)
        if (players.isEmpty()) return
        players.first().sendMessage("${players.first().position.chunkX()} ${players.first().position.chunkZ()}")
    }
}