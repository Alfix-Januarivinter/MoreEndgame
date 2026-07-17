package com.alfixjanuarivinter.moreendgame.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class TreeHeartBlock extends Block {
    public static final EnumProperty<TreeType> TYPE = EnumProperty.create("type", TreeType.class);

    public enum TreeType implements StringRepresentable {
        OAK("oak", Blocks.OAK_LOG),
        SPRUCE("spruce", Blocks.SPRUCE_LOG),
        BIRCH("birch", Blocks.BIRCH_LOG),
        JUNGLE("jungle", Blocks.JUNGLE_LOG),
        ACACIA("acacia", Blocks.ACACIA_LOG),
        DARK_OAK("dark_oak", Blocks.DARK_OAK_LOG),
        MANGROVE("mangrove", Blocks.MANGROVE_LOG),
        CHERRY("cherry", Blocks.CHERRY_LOG),
        PALE_OAK("pale_oak", Blocks.PALE_OAK_LOG);

        private final String name;
        private final Block logBlock;

        TreeType(String name, Block logBlock) {
            this.name = name;
            this.logBlock = logBlock;
        }

        @Override
        public String getSerializedName() { return this.name; }
        public BlockState getLogState() { return this.logBlock.defaultBlockState(); }
    }

    public TreeHeartBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, TreeType.OAK));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(5) == 0) {
            growLogChain(level, pos, Direction.DOWN, state.getValue(TYPE));
            growLogChain(level, pos, Direction.UP, state.getValue(TYPE));
        }
    }

    private void growLogChain(ServerLevel level, BlockPos pos, Direction growthDir, TreeType type) {
        BlockPos.MutableBlockPos currentPos = pos.mutable();
        int maxChainLength = 8;

        for (int i = 0; i < maxChainLength; i++) {
            currentPos.move(growthDir);
            BlockState currentState = level.getBlockState(currentPos);

            if (currentState.isAir()) {
                BlockState newLog = type.getLogState();
                if (newLog.canSurvive(level, currentPos)) {
                    level.setBlock(currentPos, newLog, Block.UPDATE_ALL);
                }
                return;
            }
            if (currentState.is(type.logBlock)) {
                continue;
            } else {
                return;
            }
        }
    }
}