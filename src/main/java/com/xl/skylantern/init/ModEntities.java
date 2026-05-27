package com.xl.skylantern.init;

import com.xl.skylantern.SkyLanternsReborn;
import com.xl.skylantern.common.entities.SkyLanternEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, SkyLanternsReborn.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<SkyLanternEntity>> SKY_LANTERN = ENTITIES.register(
            "sky_lantern",
            () -> EntityType.Builder.<SkyLanternEntity>of(SkyLanternEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.0F)
                    .clientTrackingRange(8)
                    .updateInterval(20)
                    .build("sky_lantern")
    );
}
