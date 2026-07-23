package com.alfixjanuarivinter.moreendgame.Items;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ModNetwork {
    public static void register() {
        PayloadTypeRegistry.clientboundPlay()
                .register(SweepParticlePacket.TYPE, SweepParticlePacket.CODEC);
    }
}