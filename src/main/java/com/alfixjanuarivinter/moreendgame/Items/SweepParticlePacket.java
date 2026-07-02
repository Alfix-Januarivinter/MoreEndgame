package com.alfixjanuarivinter.moreendgame.Items;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

public record SweepParticlePacket(double x, double y, double z, float yRot) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SweepParticlePacket> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath("moreendgame", "sweep_particle"));

    public static final StreamCodec<FriendlyByteBuf, SweepParticlePacket> CODEC =
            StreamCodec.of(
                    (buf, packet) -> {
                        buf.writeDouble(packet.x);
                        buf.writeDouble(packet.y);
                        buf.writeDouble(packet.z);
                        buf.writeFloat(packet.yRot);
                    },
                    buf -> new SweepParticlePacket(
                            buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat()
                    )
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // Send to all players tracking the player (and the player themselves)
    public static void sendToAll(ServerPlayer player) {
        SweepParticlePacket packet = new SweepParticlePacket(
                player.getX(), player.getY(), player.getZ(), player.getYRot()
        );
        for (ServerPlayer other : PlayerLookup.tracking(player)) {
            ServerPlayNetworking.send(other, packet);
        }
        ServerPlayNetworking.send(player, packet);
    }
}