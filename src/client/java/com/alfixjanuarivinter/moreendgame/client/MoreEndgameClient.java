package com.alfixjanuarivinter.moreendgame.client;

import com.alfixjanuarivinter.moreendgame.Items.SweepParticlePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.particles.ParticleTypes;

public class MoreEndgameClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(SweepParticlePacket.TYPE, (payload, context) -> {
			context.client().execute(() -> {
				var level = context.client().level;
				var player = context.client().player;
				if (level == null || player == null) return;

				double yaw = Math.toRadians(payload.yRot());
				double radius = 2.5;
				int particles = 30;

				for (int i = 0; i < particles; i++) {
					// Sweep from left (-90°) to right (+90°) in front of the player
					double angleOffset = Math.toRadians((i - particles / 2.0) * 6); // approx -90° to +90°
					// Minecraft's yaw: 0 = south, increases clockwise. X-axis = east, Z-axis = south.
					// To get a point in front of the player, we need the angle from the X-axis.
					double worldAngle = yaw + Math.PI / 2.0 + angleOffset;
					double x = payload.x() + Math.cos(worldAngle) * radius;
					double z = payload.z() + Math.sin(worldAngle) * radius;
					double vx = Math.cos(worldAngle) * 0.5;
					double vz = Math.sin(worldAngle) * 0.5;

					// Two height layers for depth
					level.addParticle(ParticleTypes.CRIT, x, payload.y() + 0.5, z, vx, 0.0, vz);
					level.addParticle(ParticleTypes.CRIT, x, payload.y() + 1.5, z, vx, 0.0, vz);
				}
			});
		});
	}
}