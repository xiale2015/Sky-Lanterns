package com.xl.skylantern.utils;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class WorldUtils {

    public static boolean isBlockLoaded(@Nullable BlockGetter world, @Nonnull BlockPos pos) {
        if (world == null) {
            return false;
        } else if (world instanceof LevelReader levelReader && levelReader.isOutsideBuildHeight(pos)) {
            return false;
        } else if (world instanceof LevelReader) {
            return ((LevelReader) world).hasChunkAt(pos);
        }
        return true;
    }

    @Nonnull
    public static Optional<BlockState> getBlockState(@Nullable BlockGetter world, @Nonnull BlockPos pos) {
        if (!isBlockLoaded(world, pos)) {
            return Optional.empty();
        }
        return Optional.of(world.getBlockState(pos));
    }
}
