package com.xl.skylantern.init;

import com.xl.skylantern.SkyLanternsReborn;
import com.xl.skylantern.common.items.SkyLanternItem;
import com.xl.skylantern.utils.EnumColor;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SkyLanternsReborn.MODID);

    public static final DeferredItem<SkyLanternItem> SKY_LANTERN_ORANGE = ITEMS.register("sky_lantern_orange",
            () -> new SkyLanternItem(EnumColor.ORANGE, new Item.Properties().stacksTo(16)));

    public static final DeferredItem<SkyLanternItem> SKY_LANTERN_PINK = ITEMS.register("sky_lantern_pink",
            () -> new SkyLanternItem(EnumColor.PINK, new Item.Properties().stacksTo(16)));
}
