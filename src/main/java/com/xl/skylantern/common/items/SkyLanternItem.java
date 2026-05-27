package com.xl.skylantern.common.items;

import com.xl.skylantern.common.entities.SkyLanternEntity;
import com.xl.skylantern.utils.EnumColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import java.util.List;

public class SkyLanternItem extends Item {

    private final EnumColor color;

    public SkyLanternItem(EnumColor color, Properties properties) {
        super(properties);
        this.color = color;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        final Level level = pContext.getLevel();
        final BlockPos pos = pContext.getClickedPos();
        if (!level.isClientSide) {
            final ItemStack stack = pContext.getItemInHand();
            if (!stack.isEmpty()) {
                pContext.getPlayer().swing(pContext.getHand());

                final SkyLanternEntity entity = SkyLanternEntity.create(level, new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()), color);
                if (entity == null) {
                    return InteractionResult.FAIL;
                }
                level.addFreshEntity(entity);
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if (player.isShiftKeyDown() && player.getVehicle() instanceof LivingEntity targetEntity) {
            if (!level.isClientSide) {
                final AABB bound = new AABB(targetEntity.getX() - 0.2, targetEntity.getY() - 0.5, targetEntity.getZ() - 0.2,
                        targetEntity.getX() + 0.2, targetEntity.getY() + targetEntity.getDimensions(targetEntity.getPose()).height() + 4, targetEntity.getZ() + 0.2);
                final List<SkyLanternEntity> balloonsNear = level.getEntitiesOfClass(SkyLanternEntity.class, bound);
                for (SkyLanternEntity balloon : balloonsNear) {
                    if (balloon.latchedEntity == targetEntity) {
                        balloon.setUnlatched();
                        return InteractionResultHolder.success(player.getItemInHand(hand));
                    }
                }
                final SkyLanternEntity balloon = SkyLanternEntity.create(targetEntity, color);
                if (balloon == null) {
                    return InteractionResultHolder.fail(player.getItemInHand(hand));
                }
                level.addFreshEntity(balloon);
                player.getItemInHand(hand).shrink(1);
            }
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }

    @Nonnull
    @Override
    public InteractionResult interactLivingEntity(@Nonnull ItemStack stack, @Nonnull Player player, @Nonnull LivingEntity entity, @Nonnull InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            if (!player.level().isClientSide) {
                final AABB bound = new AABB(entity.getX() - 0.2, entity.getY() - 0.5, entity.getZ() - 0.2,
                        entity.getX() + 0.2, entity.getY() + entity.getDimensions(entity.getPose()).height() + 4, entity.getZ() + 0.2);
                final List<SkyLanternEntity> balloonsNear = player.level().getEntitiesOfClass(SkyLanternEntity.class, bound);
                for (SkyLanternEntity balloon : balloonsNear) {
                    if (balloon.latchedEntity == entity) {
                        balloon.setUnlatched();
                        return InteractionResult.SUCCESS;
                    }
                }
                final SkyLanternEntity balloon = SkyLanternEntity.create(entity, color);
                if (balloon == null) {
                    return InteractionResult.FAIL;
                }
                player.level().addFreshEntity(balloon);
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
