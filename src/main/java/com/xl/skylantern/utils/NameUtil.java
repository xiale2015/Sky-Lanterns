package com.xl.skylantern.utils;

import net.minecraft.resources.ResourceLocation;

public class NameUtil {
    public static ResourceLocation suffixPath(ResourceLocation key, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(key.getNamespace(), key.getPath() + suffix);
    }
}
