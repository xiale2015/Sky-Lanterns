package com.xl.skylantern.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class LitBlock extends AirBlock {
    public LitBlock() {
        super(BlockBehaviour.Properties.of()
                .noCollission()
                .air()
                .lightLevel(state -> 1));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
    }
}
