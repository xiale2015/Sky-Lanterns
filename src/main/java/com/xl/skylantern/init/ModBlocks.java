package com.xl.skylantern.init;

import com.xl.skylantern.SkyLanternsReborn;
import com.xl.skylantern.common.blocks.LitBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SkyLanternsReborn.MODID);

    public static final DeferredBlock<LitBlock> LIT = BLOCKS.register("air_lit", LitBlock::new);
}
