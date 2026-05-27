package com.xl.skylantern.utils;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.network.chat.TextColor;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum EnumColor implements IIncrementalEnum<EnumColor> {
    BLACK("\u00a70", "Black", "black", new int[]{64, 64, 64}, DyeColor.BLACK),
    DARK_BLUE("\u00a71", "Blue", "blue", new int[]{54, 107, 208}, DyeColor.BLUE),
    DARK_GREEN("\u00a72", "Green", "green", new int[]{89, 193, 95}, DyeColor.GREEN),
    DARK_AQUA("\u00a73", "Cyan", "cyan", new int[]{0, 243, 208}, DyeColor.CYAN),
    DARK_RED("\u00a74", "Dark Red", "dark_red", new int[]{201, 7, 31}, MapColor.NETHER, null),
    PURPLE("\u00a75", "Purple", "purple", new int[]{164, 96, 217}, DyeColor.PURPLE),
    ORANGE("\u00a76", "Orange", "orange", new int[]{255, 161, 96}, DyeColor.ORANGE),
    GRAY("\u00a77", "Light Gray", "light_gray", new int[]{207, 207, 207}, DyeColor.LIGHT_GRAY),
    DARK_GRAY("\u00a78", "Gray", "gray", new int[]{122, 122, 122}, DyeColor.GRAY),
    INDIGO("\u00a79", "Light Blue", "light_blue", new int[]{85, 158, 255}, DyeColor.LIGHT_BLUE),
    BRIGHT_GREEN("\u00a7a", "Lime", "lime", new int[]{117, 255, 137}, DyeColor.LIME),
    AQUA("\u00a7b", "Aqua", "aqua", new int[]{48, 255, 249}, MapColor.COLOR_LIGHT_BLUE, null),
    RED("\u00a7c", "Red", "red", new int[]{255, 56, 60}, DyeColor.RED),
    PINK("\u00a7d", "Magenta", "magenta", new int[]{213, 94, 203}, DyeColor.MAGENTA),
    YELLOW("\u00a7e", "Yellow", "yellow", new int[]{255, 221, 79}, DyeColor.YELLOW),
    WHITE("\u00a7f", "White", "white", new int[]{255, 255, 255}, DyeColor.WHITE),
    BROWN("\u00a76", "Brown", "brown", new int[]{161, 118, 73}, DyeColor.BROWN),
    BRIGHT_PINK("\u00a7d", "Pink", "pink", new int[]{255, 188, 196}, DyeColor.PINK);

    private static final EnumColor[] COLORS = values();
    public final String code;
    private final String englishName;
    private final String registryPrefix;
    @Nullable
    private final DyeColor dyeColor;
    private final MapColor mapColor;
    private int[] rgbCode;
    private TextColor color;

    EnumColor(String s, String englishName, String registryPrefix, int[] rgbCode, DyeColor dyeColor) {
        this(s, englishName, registryPrefix, rgbCode, dyeColor.getMapColor(), dyeColor);
    }

    EnumColor(String code, String englishName, String registryPrefix, int[] rgbCode, MapColor mapColor,
              @Nullable DyeColor dyeColor) {
        this.code = code;
        this.englishName = englishName;
        this.dyeColor = dyeColor;
        this.registryPrefix = registryPrefix;
        setColorFromAtlas(rgbCode);
        this.mapColor = mapColor;
    }

    public static EnumColor byIndexStatic(int index) {
        return MathUtil.getByIndexMod(COLORS, index);
    }

    public String getRegistryPrefix() {
        return registryPrefix;
    }

    public String getEnglishName() {
        return englishName;
    }

    public MapColor getMapColor() {
        return mapColor;
    }

    @Nullable
    public DyeColor getDyeColor() {
        return dyeColor;
    }

    public float getColor(int index) {
        return rgbCode[index] / 255F;
    }

    public TextColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return code;
    }

    @Nonnull
    @Override
    public EnumColor byIndex(int index) {
        return byIndexStatic(index);
    }

    public void setColorFromAtlas(int[] color) {
        rgbCode = color;
        this.color = TextColor.fromRgb(rgbCode[0] << 16 | rgbCode[1] << 8 | rgbCode[2]);
    }

    public int[] getRgbCode() {
        return rgbCode;
    }

    public float[] getRgbCodeFloat() {
        return new float[]{getColor(0), getColor(1), getColor(2)};
    }
}
