package com.xl.skylantern.init;

import com.xl.skylantern.SkyLanternsReborn;
import com.xl.skylantern.common.items.SkyLanternItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SkyLanternsReborn.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SKY_LANTERNS_TAB = CREATIVE_MODE_TABS.register("sky_lanterns_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.skylanternsreborn"))
                    .icon(() -> new ItemStack(ModItems.SKY_LANTERN_ORANGE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.SKY_LANTERN_ORANGE.get());
                        output.accept(ModItems.SKY_LANTERN_PINK.get());
                    })
                    .build());
}
