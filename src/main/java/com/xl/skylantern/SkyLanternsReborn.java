package com.xl.skylantern;

import com.xl.skylantern.common.configs.ModConfig;
import com.xl.skylantern.common.entities.SkyLanternEntity;
import com.xl.skylantern.common.events.DispenserRegistry;
import com.xl.skylantern.init.ModBlocks;
import com.xl.skylantern.init.ModEntities;
import com.xl.skylantern.init.ModItems;
import com.xl.skylantern.init.ModTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(SkyLanternsReborn.MODID)
public class SkyLanternsReborn {
    public static final String MODID = "skylanternsreborn";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SkyLanternsReborn(IEventBus modEventBus, ModContainer modContainer) {
        // 注册 Deferred Register
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModTabs.CREATIVE_MODE_TABS.register(modEventBus);

        // 注册事件监听
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addAttributes);

        // 注册 NeoForge 游戏事件
        NeoForge.EVENT_BUS.register(this);

        // 注册配置
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModConfig.CONFIG_SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenserRegistry.registerDispenserBehaviors();
        });
    }

    private void addAttributes(final EntityAttributeCreationEvent event) {
        event.put(ModEntities.SKY_LANTERN.get(), SkyLanternEntity.setAttributes());
    }

    public static net.minecraft.resources.ResourceLocation rl(String path) {
        return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Sky Lanterns Reborn loaded!");
    }
}
