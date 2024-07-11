@file:Suppress("UnstableApiUsage", "NAME_SHADOWING")

package me.chaoticwagon.vivarium

import me.chaoticwagon.vivarium.instance.GameInstance
import me.chaoticwagon.vivarium.util.negOffsetByChunks
import me.chaoticwagon.vivarium.util.offsetByChunks
import me.chaoticwagon.vivarium.util.wrapNumber
import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.asm.Advice
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.bytebuddy.matcher.ElementMatchers.named
import net.minestom.server.coordinate.Pos
import net.minestom.server.network.PacketProcessor
import net.minestom.server.network.packet.client.ClientPacket
import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket
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
    lateinit var instance: GameInstance

    fun sendPacket(
        connection: PlayerSocketConnection,
        packet: SendablePacket
    ): SendablePacket {
        var packet = packet

        if (packet is CachedPacket) {
            packet = packet.packet(connection.connectionState)
        }

        packet = when (packet) {
            is ChunkDataPacket -> {
                if (packet.chunkX == 0) {
//                    println("${packet.chunkX},${packet.chunkZ} | ${wrapNumber(packet.chunkZ, -2, 2)}")
                }
                ChunkDataPacket(
                    wrapNumber(packet.chunkX, -2, 2).toInt(),
                    wrapNumber(packet.chunkZ, -2, 2).toInt(),
                    packet.chunkData(),
                    packet.lightData()
                )
            }

            is BlockChangePacket -> {
                BlockChangePacket(packet.blockPosition().negOffsetByChunks(), packet.blockStateId())
            }

            else -> packet
        }

        return packet
    }
}

internal object ServerboundPacketInterceptorKt {
    // LMFAO
    private var connections: HashMap<Thread, PlayerSocketConnection> = HashMap()
    lateinit var instance: GameInstance

    fun process(connection: PlayerSocketConnection) {
        connections[Thread.currentThread()] = connection
    }

    fun create(packet: ClientPacket): ClientPacket {
        val thread = Thread.currentThread()
        val connection = connections[thread] ?: return packet
        connections.remove(thread)
        val player = connection.player ?: return packet

        var packet = packet
        when (packet) {
//            is ClientPlayerBlockPlacementPacket -> {
//                packet = ClientPlayerBlockPlacementPacket(
//                    packet.hand(),
//                    packet.blockPosition().offsetByChunks(chunkOffset),
//                    packet.blockFace(),
//                    packet.cursorPositionX() + chunkOffset.first,
//                    packet.cursorPositionY(),
//                    packet.cursorPositionZ() + chunkOffset.second,
//                    packet.insideBlock(),
//                    packet.sequence()
//                )
//            }

            is ClientPlayerPositionPacket -> {
                packet = ClientPlayerPositionPacket(packet.position.offsetByChunks(), packet.onGround)
            }

            is ClientPlayerPositionAndRotationPacket -> {
                packet = ClientPlayerPositionAndRotationPacket(
                    packet.position.offsetByChunks() as Pos,
                    packet.onGround
                )
            }
        }

        return packet
    }
}
