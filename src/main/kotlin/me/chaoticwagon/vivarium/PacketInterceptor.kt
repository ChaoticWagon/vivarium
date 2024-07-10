@file:Suppress("UnstableApiUsage", "NAME_SHADOWING")

package me.chaoticwagon.vivarium

import me.chaoticwagon.vivarium.util.negOffsetByChunks
import me.chaoticwagon.vivarium.util.offsetByChunks
import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.asm.Advice
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.bytebuddy.matcher.ElementMatchers.named
import net.minestom.server.network.PacketProcessor
import net.minestom.server.network.packet.client.ClientPacket
import net.minestom.server.network.packet.client.play.ClientPlayerBlockPlacementPacket
import net.minestom.server.network.packet.server.CachedPacket
import net.minestom.server.network.packet.server.SendablePacket
import net.minestom.server.network.packet.server.play.BlockChangePacket
import net.minestom.server.network.packet.server.play.ChunkDataPacket
import net.minestom.server.network.player.PlayerSocketConnection

object PacketInterceptor {
    fun init() {
        ByteBuddyAgent.install()

        ByteBuddy()
            .redefine(PlayerSocketConnection::class.java)
            .visit(Advice.to(ClientboundPacketInterceptor::class.java).on(named("sendPacket")))
            .make()
            .load(PlayerSocketConnection::class.java.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())

        ByteBuddy()
            .redefine(PacketProcessor::class.java)
            .visit(Advice.to(ServerboundPacketInterceptor_create::class.java).on(named("create")))
            .visit(Advice.to(ServerboundPacketInterceptor_process::class.java).on(named("process")))
            .make()
            .load(PlayerSocketConnection::class.java.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
    }
}

object ClientboundPacketInterceptorKt {
    fun sendPacket(
        connection: PlayerSocketConnection,
        packet: SendablePacket
    ): SendablePacket {
        var packet = packet

        if (packet is CachedPacket) {
            packet = packet.packet(connection.connectionState)
        }

        val offset = if (connection.loginUsername.equals("Insprill")) {
            1
        } else {
            -1
        }

        packet = when (packet) {
            is ChunkDataPacket -> {
                ChunkDataPacket(
                    packet.chunkX() + offset,
                    packet.chunkZ() + offset,
                    packet.chunkData(),
                    packet.lightData()
                )
            }

            is BlockChangePacket -> {
                BlockChangePacket(packet.blockPosition().negOffsetByChunks(offset), packet.blockStateId())
            }

            else -> packet
        }

        return packet
    }
}

internal object ServerboundPacketInterceptorKt {
    // LMFAO
    private var connections: HashMap<Thread, PlayerSocketConnection> = HashMap()

    fun process(connection: PlayerSocketConnection) {
        connections[Thread.currentThread()] = connection
    }

    fun create(packet: ClientPacket): ClientPacket {
        val connection = connections[Thread.currentThread()] ?: return packet

        val offset = if (connection.loginUsername.equals("Insprill")) {
            1
        } else {
            -1
        }

        var packet = packet
        if (packet is ClientPlayerBlockPlacementPacket) {
            packet = ClientPlayerBlockPlacementPacket(
                packet.hand(),
                packet.blockPosition().offsetByChunks(offset),
                packet.blockFace(),
                packet.cursorPositionX() + (16 * offset),
                packet.cursorPositionY(),
                packet.cursorPositionZ() + (16 * offset),
                packet.insideBlock(),
                packet.sequence()
            )
        }

        return packet
    }
}
