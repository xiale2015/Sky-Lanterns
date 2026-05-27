package com.xl.skylantern;

import com.xl.skylantern.client.model.PaperLanternPinkModel;
import com.xl.skylantern.client.render.SkyLanternRender;
import com.xl.skylantern.init.ModEntities;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = SkyLanternsReborn.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = SkyLanternsReborn.MODID, value = Dist.CLIENT)
public class SkyLanternsRebornClient {

    public SkyLanternsRebornClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        SkyLanternsReborn.LOGGER.info("Sky Lanterns Reborn client setup");
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SKY_LANTERN.get(), SkyLanternRender::new);
    }

    @SubscribeEvent
    static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PaperLanternPinkModel.LAYER_LOCATION, PaperLanternPinkModel::createBodyLayer);
    }
}
