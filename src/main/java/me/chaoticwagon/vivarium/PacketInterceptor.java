package me.chaoticwagon.vivarium;


import net.bytebuddy.asm.Advice;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.network.player.PlayerSocketConnection;

@SuppressWarnings({"UnstableApiUsage", "ReassignedVariable", "UnusedAssignment", "unused"})
class ClientboundPacketInterceptor {
    @Advice.OnMethodEnter
    public static void sendPacket(@Advice.This PlayerSocketConnection connection, @Advice.Argument(value = 0, readOnly = false) SendablePacket packet) {
        packet = ClientboundPacketInterceptorKt.INSTANCE.sendPacket(connection, packet);
    }
}

@SuppressWarnings({"UnstableApiUsage", "unused"})
class ServerboundPacketInterceptor_process {
    @Advice.OnMethodEnter
    public static void process(@Advice.Argument(value = 0) PlayerConnection connection) {
        ServerboundPacketInterceptorKt.INSTANCE.process((PlayerSocketConnection) connection);
    }
}

@SuppressWarnings({"ReassignedVariable", "UnusedAssignment", "unused"})
class ServerboundPacketInterceptor_create {
    @Advice.OnMethodExit
    public static void create(@Advice.Return(readOnly = false) ClientPacket packet) {
        packet = ServerboundPacketInterceptorKt.INSTANCE.create(packet);
    }
}

