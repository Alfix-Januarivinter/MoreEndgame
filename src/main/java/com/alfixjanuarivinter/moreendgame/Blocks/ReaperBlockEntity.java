package com.alfixjanuarivinter.moreendgame.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason; // FIXED: Modern mapping name
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.Zombie; // FIXED: Reorganized subpackage path
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ReaperBlockEntity extends BlockEntity {
    private int spawnDelay = 200;

    public ReaperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.REAPER_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void tick(Level level, BlockPos pos) {
        if (level == null || level.isClientSide()) return;

        if (level instanceof ServerLevel serverLevel) {
            this.spawnDelay--;
            if (this.spawnDelay <= 0) {
                // FIXED: Uses EntitySpawnReason.SPAWNER
                Zombie zombie = EntityType.ZOMBIE.create(serverLevel, EntitySpawnReason.SPAWNER);
                if (zombie != null) {
                    // This resolves automatically now that Zombie is correctly imported!
                    zombie.moveTo(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 0.0F, 0.0F);
                    serverLevel.addFreshEntity(zombie);
                }
                this.spawnDelay = 200;
                this.setChanged();
            }
        }
    }
}