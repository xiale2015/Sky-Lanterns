package com.xl.skylantern.common.events;

import com.xl.skylantern.common.entities.SkyLanternEntity;
import com.xl.skylantern.init.ModItems;
import com.xl.skylantern.utils.EnumColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import javax.annotation.Nonnull;

public class DispenserRegistry {

    public static void registerDispenserBehaviors() {
        DefaultDispenseItemBehavior behavior = new DefaultDispenseItemBehavior() {
            @Override
            @Nonnull
            public ItemStack execute(@Nonnull BlockSource source, @Nonnull ItemStack stack) {
                return spawnSkyLantern(source, stack);
            }
        };
        DispenserBlock.registerBehavior(ModItems.SKY_LANTERN_ORANGE.get(), behavior);
        DispenserBlock.registerBehavior(ModItems.SKY_LANTERN_PINK.get(), behavior);
    }

    public static ItemStack spawnSkyLantern(@Nonnull BlockSource source, @Nonnull ItemStack stack) {
        final BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
        final Level level = source.level();
        // 根据物品类型选择颜色
        EnumColor color = stack.is(ModItems.SKY_LANTERN_PINK.get()) ? EnumColor.PINK : EnumColor.ORANGE;
        final SkyLanternEntity lanternEntity = SkyLanternEntity.create(level, blockpos, color);
        if (lanternEntity == null) {
            return stack;
        }
        level.addFreshEntity(lanternEntity);
        final int count = stack.getCount() - 1;
        if (count > 0) {
            stack.setCount(count);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }
}
