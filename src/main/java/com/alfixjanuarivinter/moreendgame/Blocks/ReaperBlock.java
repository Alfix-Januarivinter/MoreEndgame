package com.alfixjanuarivinter.moreendgame.Blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class ReaperBlock extends BaseEntityBlock {
    // 26.2 codecs require a simple matching reference mapper
    public static final MapCodec<ReaperBlock> CODEC = simpleCodec(ReaperBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public ReaperBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL; // Prevents the block from turning completely invisible
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReaperBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // Ticks helper cleanly routing logic to server-side updates
        return createTickerHelper(type, ModBlocks.REAPER_BLOCK_ENTITY_TYPE,
                (lvl, pos, st, blockEntity) -> blockEntity.tick(lvl, pos));
    }
}