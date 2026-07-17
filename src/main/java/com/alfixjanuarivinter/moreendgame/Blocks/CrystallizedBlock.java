package com.alfixjanuarivinter.moreendgame.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class CrystallizedBlock extends Block {
    public static final EnumProperty<Variant> VARIANT = EnumProperty.create("variant", Variant.class);

    public enum Variant implements StringRepresentable {
        STONE("stone"),
        DEEPSLATE("deepslate");

        private final String name;
        Variant(String name) { this.name = name; }
        @Override public String getSerializedName() { return this.name; }
    }

    public CrystallizedBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, Variant.STONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Variant currentVariant = state.getValue(VARIANT);
        BlockState fillState = (currentVariant == Variant.DEEPSLATE) ? Blocks.DEEPSLATE.defaultBlockState() : Blocks.STONE.defaultBlockState();

        // Check 3x3x3 area
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip the center block

                    BlockPos targetPos = pos.offset(x, y, z);
                    if (level.getBlockState(targetPos).isAir()) {
                        level.setBlock(targetPos, fillState, Block.UPDATE_ALL);
                    }
                }
            }
        }
    }
}