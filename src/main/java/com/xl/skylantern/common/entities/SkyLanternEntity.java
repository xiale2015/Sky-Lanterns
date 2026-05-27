package com.xl.skylantern.common.entities;

import com.xl.skylantern.SkyLanternsReborn;
import com.xl.skylantern.common.configs.ModConfig;
import com.xl.skylantern.init.ModBlocks;
import com.xl.skylantern.init.ModEntities;
import com.xl.skylantern.utils.EnumColor;
import com.xl.skylantern.utils.NBTUtils;
import com.xl.skylantern.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.DustParticleOptions;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class SkyLanternEntity extends Mob {

    private static final EntityDataAccessor<Byte> IS_LATCHED = SynchedEntityData.defineId(SkyLanternEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> LATCHED_X = SynchedEntityData.defineId(SkyLanternEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LATCHED_Y = SynchedEntityData.defineId(SkyLanternEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LATCHED_Z = SynchedEntityData.defineId(SkyLanternEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LATCHED_ID = SynchedEntityData.defineId(SkyLanternEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(SkyLanternEntity.class, EntityDataSerializers.INT);

    public BlockPos posLight = BlockPos.ZERO;
    public LivingEntity latchedEntity;
    private EnumColor color = EnumColor.DARK_BLUE;
    private BlockPos latched;
    private boolean hasCachedEntity;
    private UUID cachedEntityUUID;

    public SkyLanternEntity(EntityType<? extends SkyLanternEntity> type, Level level) {
        super(type, level);
        setPos(getX() + 0.5F, getY() + 3F, getZ() + 0.5F);
    }

    @Nullable
    public static SkyLanternEntity create(Level level, double x, double y, double z, EnumColor c) {
        final SkyLanternEntity balloon = ModEntities.SKY_LANTERN.get().create(level);
        if (balloon == null) {
            return null;
        }
        balloon.setPos(x + 0.5F, y + 3F, z + 0.5F);
        balloon.xo = balloon.getX();
        balloon.yo = balloon.getY();
        balloon.zo = balloon.getZ();
        balloon.setColor(c);
        return balloon;
    }

    @Nullable
    public static SkyLanternEntity create(LivingEntity entity, EnumColor c) {
        final SkyLanternEntity balloon = ModEntities.SKY_LANTERN.get().create(entity.level());
        if (balloon == null) {
            return null;
        }
        balloon.latchedEntity = entity;
        float height = balloon.latchedEntity.getDimensions(balloon.latchedEntity.getPose()).height();
        balloon.setPos(balloon.latchedEntity.getX(), balloon.latchedEntity.getY() + height + 1.7F, balloon.latchedEntity.getZ());
        balloon.xo = balloon.getX();
        balloon.yo = balloon.getY();
        balloon.zo = balloon.getZ();
        balloon.setColor(c);
        balloon.entityData.set(IS_LATCHED, (byte) 2);
        balloon.entityData.set(LATCHED_ID, entity.getId());
        return balloon;
    }

    @Nullable
    public static SkyLanternEntity create(Level level, BlockPos pos, EnumColor c) {
        final SkyLanternEntity balloon = ModEntities.SKY_LANTERN.get().create(level);
        if (balloon == null) {
            return null;
        }
        balloon.latched = pos;
        balloon.setPos(balloon.latched.getX() + 0.5F, balloon.latched.getY() + 1.8F, balloon.latched.getZ() + 0.5F);
        balloon.xo = balloon.getX();
        balloon.yo = balloon.getY();
        balloon.zo = balloon.getZ();
        balloon.setColor(c);
        balloon.entityData.set(IS_LATCHED, (byte) 1);
        balloon.entityData.set(LATCHED_X, balloon.latched.getX());
        balloon.entityData.set(LATCHED_Y, balloon.latched.getY());
        balloon.entityData.set(LATCHED_Z, balloon.latched.getZ());
        return balloon;
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .build();
    }

    public static double processYSpeed(double y, net.minecraft.util.RandomSource random) {
        if (y == 0) return 0;
        return Math.min(y + random.nextDouble() * 0.1, 0.2F);
    }

    public EnumColor getColor() {
        return EnumColor.byIndexStatic(entityData.get(COLOR));
    }

    public void setColor(EnumColor color) {
        this.color = color;
        this.entityData.set(COLOR, color.ordinal());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_LATCHED, (byte) 0);
        builder.define(LATCHED_X, 0);
        builder.define(LATCHED_Y, 0);
        builder.define(LATCHED_Z, 0);
        builder.define(LATCHED_ID, -1);
        builder.define(COLOR, 0);
    }

    @Override
    public void tick() {
        xo = getX();
        yo = getY();
        zo = getZ();

        super.tick();

        this.setNoGravity(true);
        this.setPersistenceRequired();

        if (getY() >= level().getMaxBuildHeight()) {
            pop();
            return;
        } else {
            if (level().random.nextInt(5) == 0) {
                final double d0 = xo;
                final double d1 = yo + 0.15D;
                final double d2 = zo;
                level().addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                level().addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }

        if (level().isClientSide) {
            if (entityData.get(IS_LATCHED) == 1) {
                latched = new BlockPos(entityData.get(LATCHED_X), entityData.get(LATCHED_Y), entityData.get(LATCHED_Z));
            } else {
                latched = null;
            }
            if (entityData.get(IS_LATCHED) == 2) {
                latchedEntity = (LivingEntity) level().getEntity(entityData.get(LATCHED_ID));
            } else {
                latchedEntity = null;
            }
        } else {
            if (hasCachedEntity) {
                if (level() instanceof ServerLevel serverLevel) {
                    final Entity entity = serverLevel.getEntity(cachedEntityUUID);
                    if (entity instanceof LivingEntity living) {
                        latchedEntity = living;
                    }
                }
                cachedEntityUUID = null;
                hasCachedEntity = false;
            }
            if (tickCount == 1) {
                byte isLatched;
                if (latched != null) {
                    isLatched = (byte) 1;
                } else if (latchedEntity != null) {
                    isLatched = (byte) 2;
                } else {
                    isLatched = (byte) 0;
                }
                entityData.set(IS_LATCHED, isLatched);
                entityData.set(LATCHED_X, latched == null ? 0 : latched.getX());
                entityData.set(LATCHED_Y, latched == null ? 0 : latched.getY());
                entityData.set(LATCHED_Z, latched == null ? 0 : latched.getZ());
                entityData.set(LATCHED_ID, latchedEntity == null ? -1 : latchedEntity.getId());
            }
        }

        if (!level().isClientSide) {
            if (latched != null) {
                final Optional<BlockState> blockState = WorldUtils.getBlockState(level(), latched);
                if (blockState.isPresent() && blockState.get().isAir()) {
                    latched = null;
                    entityData.set(IS_LATCHED, (byte) 0);
                }
            }
            if (latchedEntity != null && (latchedEntity.getHealth() <= 0 || !latchedEntity.isAlive() || !latchedEntity.isAddedToLevel())) {
                latchedEntity = null;
                entityData.set(IS_LATCHED, (byte) 0);
            }
        }

        if (!isLatched()) {
            Vec3 motion;
            {
                motion = getDeltaMovement();

                if (motion.y() < 0.05D) {
                    if (tickCount >= 40) {
                        motion = new Vec3(motion.x(), motion.y() + random.nextDouble() * 0.006D, motion.z());
                    } else {
                        motion = new Vec3(motion.x(), motion.y() + random.nextDouble() * 0.003D, motion.z());
                    }
                }
                setDeltaMovement(motion);
                move(MoverType.SELF, getDeltaMovement());

                motion = getDeltaMovement();
                if (!level().isClientSide && !this.isLeashed() && this.getLeashHolder() == null) {
                    long time = (this.getId() * 3L) + level().getGameTime() * 3;
                    float timeClampSpeed = ((time) % 360) - 180;
                    float tiltMax = (float) Math.toRadians(timeClampSpeed);

                    double speed = 0.006;
                    if (tickCount < 40) {
                        speed = 0.004F;
                    }
                    motion = new Vec3(motion.x() - Math.cos(tiltMax) * speed, motion.y(), motion.z() + Math.sin(tiltMax) * speed);
                }
            }

            setDeltaMovement(motion);

            if (!this.level().noCollision(this.getBoundingBox())) {
                this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
            }

        } else if (latched != null) {
            setDeltaMovement(Vec3.ZERO);
        } else if (latchedEntity != null && latchedEntity.getHealth() > 0) {
            final int floor = getFloor(latchedEntity);
            final Vec3 motion = latchedEntity.getDeltaMovement();
            if (latchedEntity.getY() - (floor + 1) < -0.1) {
                latchedEntity.setDeltaMovement(motion.x(), Math.max(0.04, motion.y() * 1.015), motion.z());
            } else if (latchedEntity.getY() - (floor + 1) > 0.1) {
                latchedEntity.setDeltaMovement(motion.x(), Math.min(-0.04, motion.y() * 1.015), motion.z());
            } else {
                latchedEntity.setDeltaMovement(motion.x(), 0, motion.z());
            }
            setPos(latchedEntity.getX(), latchedEntity.getY() + getAddedHeight(), latchedEntity.getZ());
        }

        if (ModConfig.COMMON.lightUpdateRate.get() != -1) {
            if (!this.level().isClientSide && (ModConfig.COMMON.lightUpdateRate.get() <= 0 || level().getGameTime() % ModConfig.COMMON.lightUpdateRate.get() == 0)) {
                final double dist = distanceToSqr(posLight.getX(), posLight.getY(), posLight.getZ());
                if (dist >= ModConfig.COMMON.lightUpdateDistanceAccuracy.get() || level().getBlockState(posLight).getBlock() != ModBlocks.LIT.get()) {
                    clearCurrentLightBlock();
                    posLight = blockPosition();
                    if (level().isEmptyBlock(posLight) && level().getBlockFloorHeight(posLight) + ModConfig.COMMON.lightUpdateDistanceToGround.get() > getY()) {
                        level().setBlockAndUpdate(posLight, ModBlocks.LIT.get().defaultBlockState());
                    }
                }
            }
        } else {
            if (!this.level().isClientSide) {
                clearCurrentLightBlock();
            }
        }

        this.fallDistance = 0;
    }

    public double getAddedHeight() {
        return latchedEntity.getDimensions(latchedEntity.getPose()).height() + 0.8;
    }

    private int getFloor(LivingEntity entity) {
        final BlockPos pos = BlockPos.containing(entity.position());
        for (BlockPos posi = pos; posi.getY() > level().getMinBuildHeight(); posi = posi.below()) {
            if (posi.getY() < level().getMaxBuildHeight() && !level().isEmptyBlock(posi)) {
                return posi.getY() + 1 + (entity instanceof Player ? 1 : 0);
            }
        }
        return -1;
    }

    public void clearCurrentLightBlock() {
        if (!posLight.equals(BlockPos.ZERO)) {
            final BlockState state = level().getBlockState(posLight);
            if (state.getBlock() == ModBlocks.LIT.get()) {
                level().setBlockAndUpdate(posLight, Blocks.AIR.defaultBlockState());
            }
            posLight = BlockPos.ZERO;
        }
    }

    @Override
    public boolean isPushable() {
        return latched == null;
    }

    @Override
    public boolean isPickable() {
        return isAlive();
    }

    @Override
    protected boolean isImmobile() {
        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        NBTUtils.setEnumIfPresent(compound, "color", EnumColor::byIndexStatic, color -> this.setColor(color));
        NBTUtils.setBlockPosIfPresent(compound, "latched", pos -> latched = pos);
        NBTUtils.setUUIDIfPresent(compound, "owner", uuid -> {
            hasCachedEntity = true;
            cachedEntityUUID = uuid;
        });
        posLight = new BlockPos(compound.getInt("light_X"), compound.getInt("light_Y"), compound.getInt("light_Z"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("color", color.ordinal());
        if (latched != null) {
            compound.put("latched", NbtUtils.writeBlockPos(latched));
        }
        if (latchedEntity != null) {
            compound.putUUID("owner", latchedEntity.getUUID());
        }
        compound.putInt("light_X", posLight.getX());
        compound.putInt("light_Y", posLight.getY());
        compound.putInt("light_Z", posLight.getZ());
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    public boolean skipAttackInteraction(@Nonnull Entity entity) {
        pop();
        return true;
    }

    @Override
    public boolean hurt(@Nonnull DamageSource dmgSource, float damage) {
        if (isInvulnerableTo(dmgSource)) {
            return false;
        }
        markHurt();
        if (dmgSource != level().damageSources().magic() && dmgSource != level().damageSources().drown() && dmgSource != level().damageSources().fall()) {
            pop();
            return true;
        }
        return false;
    }

    private void pop() {
        if (!level().isClientSide) {
            Vector3f vec = new Vector3f(color.getColor(0), color.getColor(1), color.getColor(2));
            final DustParticleOptions dustData = new DustParticleOptions(vec, 1.0F);
            for (int i = 0; i < 10; i++) {
                ((ServerLevel) level()).sendParticles(dustData,
                        getX() + 0.6 * random.nextFloat() - 0.3,
                        getY() + 0.6 * random.nextFloat() - 0.3,
                        getZ() + 0.6 * random.nextFloat() - 0.3,
                        1, 0, 0, 0, 0);
            }
        }
        discard();
    }

    @Override
    public void setPos(double x, double y, double z) {
        setPosRaw(x, y, z);
        if (isAddedToLevel() && !this.level().isClientSide) {
            this.level().getChunk((int) Math.floor(x) >> 4, (int) Math.floor(z) >> 4);
        }
        setBoundingBox(makeBoundingBox(x, y, z));
    }

    private AABB makeBoundingBox(double x, double y, double z) {
        return getDimensions(Pose.STANDING).makeBoundingBox(x, y - 0.5F, z);
    }

    @Override
    public void refreshDimensions() {
        //NO-OP
    }

    public void setBoundingBoxInternal(AABB bb) {
        super.setBoundingBox(bb);
    }

    public boolean isLatched() {
        if (level().isClientSide) {
            return entityData.get(IS_LATCHED) > 0;
        }
        return latched != null || latchedEntity != null;
    }

    public boolean isLatchedToEntity() {
        return entityData.get(IS_LATCHED) == 2 && latchedEntity != null;
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (latchedEntity != null) {
            latchedEntity.hasImpulse = false;
        }
    }

    public void setUnlatched() {
        latched = null;
        entityData.set(IS_LATCHED, (byte) 0);
        if (!level().isClientSide)
            level().playSound(null, this, SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.NEUTRAL, 1F, 1F);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double dist) {
        return dist <= 4096;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    public boolean isLeashed() {
        return super.isLeashed();
    }
}
