package me.chaoticwagon.vivarium.instance

import net.minestom.server.MinecraftServer
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType

enum class Dimension(initializer: () -> DimensionType) {
    NORMAL({
        DimensionType.builder()
            .ambientLight(2.0f)
            .build()
    });

    val type: DimensionType = initializer.invoke()

    companion object {
        init {
            for (value in entries) {
                @Suppress("UnstableApiUsage")
                MinecraftServer.getDimensionTypeRegistry().register(NamespaceID.from("vivarium:normal"), value.type)
            }
        }
    }

}
