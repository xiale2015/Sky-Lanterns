package com.xl.skylantern.utils;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.DoubleConsumer;

public class NBTUtils {
    private NBTUtils() {
    }

    private static boolean hasTag(CompoundTag nbt, String key, int type) {
        return nbt.contains(key, type);
    }

    // Tag type constants from CompoundTag
    private static final int TAG_BYTE = 1;
    private static final int TAG_SHORT = 2;
    private static final int TAG_INT = 3;
    private static final int TAG_LONG = 4;
    private static final int TAG_FLOAT = 5;
    private static final int TAG_DOUBLE = 6;
    private static final int TAG_BYTE_ARRAY = 7;
    private static final int TAG_STRING = 8;
    private static final int TAG_LIST = 9;
    private static final int TAG_COMPOUND = 10;
    private static final int TAG_INT_ARRAY = 11;
    private static final int TAG_LONG_ARRAY = 12;
    private static final int TAG_ANY_NUMERIC = 99;

    public static void setByteIfPresent(CompoundTag nbt, String key, Consumer<Byte> setter) {
        if (hasTag(nbt, key, TAG_BYTE)) {
            setter.accept(nbt.getByte(key));
        }
    }

    public static void setBooleanIfPresent(CompoundTag nbt, String key, Consumer<Boolean> setter) {
        if (hasTag(nbt, key, TAG_BYTE)) {
            setter.accept(nbt.getBoolean(key));
        }
    }

    public static void setShortIfPresent(CompoundTag nbt, String key, Consumer<Short> setter) {
        if (hasTag(nbt, key, TAG_SHORT)) {
            setter.accept(nbt.getShort(key));
        }
    }

    public static void setIntIfPresent(CompoundTag nbt, String key, IntConsumer setter) {
        if (hasTag(nbt, key, TAG_INT)) {
            setter.accept(nbt.getInt(key));
        }
    }

    public static void setLongIfPresent(CompoundTag nbt, String key, LongConsumer setter) {
        if (hasTag(nbt, key, TAG_LONG)) {
            setter.accept(nbt.getLong(key));
        }
    }

    public static void setFloatIfPresent(CompoundTag nbt, String key, Consumer<Float> setter) {
        if (hasTag(nbt, key, TAG_FLOAT)) {
            setter.accept(nbt.getFloat(key));
        }
    }

    public static void setDoubleIfPresent(CompoundTag nbt, String key, DoubleConsumer setter) {
        if (hasTag(nbt, key, TAG_DOUBLE)) {
            setter.accept(nbt.getDouble(key));
        }
    }

    public static void setByteArrayIfPresent(CompoundTag nbt, String key, Consumer<byte[]> setter) {
        if (hasTag(nbt, key, TAG_BYTE_ARRAY)) {
            setter.accept(nbt.getByteArray(key));
        }
    }

    public static void setStringIfPresent(CompoundTag nbt, String key, Consumer<String> setter) {
        if (hasTag(nbt, key, TAG_STRING)) {
            setter.accept(nbt.getString(key));
        }
    }

    public static void setListIfPresent(CompoundTag nbt, String key, int type, Consumer<ListTag> setter) {
        if (hasTag(nbt, key, TAG_LIST)) {
            setter.accept(nbt.getList(key, type));
        }
    }

    public static void setCompoundIfPresent(CompoundTag nbt, String key, Consumer<CompoundTag> setter) {
        if (hasTag(nbt, key, TAG_COMPOUND)) {
            setter.accept(nbt.getCompound(key));
        }
    }

    public static void setIntArrayIfPresent(CompoundTag nbt, String key, Consumer<int[]> setter) {
        if (hasTag(nbt, key, TAG_INT_ARRAY)) {
            setter.accept(nbt.getIntArray(key));
        }
    }

    public static void setLongArrayIfPresent(CompoundTag nbt, String key, Consumer<long[]> setter) {
        if (hasTag(nbt, key, TAG_LONG_ARRAY)) {
            setter.accept(nbt.getLongArray(key));
        }
    }

    public static boolean hasOldUUID(CompoundTag nbt, String key) {
        return nbt.contains(key + "Most", TAG_ANY_NUMERIC) && nbt.contains(key + "Least", TAG_ANY_NUMERIC);
    }

    public static UUID getOldUUID(CompoundTag nbt, String key) {
        return new UUID(nbt.getLong(key + "Most"), nbt.getLong(key + "Least"));
    }

    public static void setUUIDIfPresent(CompoundTag nbt, String key, Consumer<UUID> setter) {
        if (nbt.hasUUID(key)) {
            setter.accept(nbt.getUUID(key));
        } else if (hasOldUUID(nbt, key)) {
            setter.accept(getOldUUID(nbt, key));
        }
    }

    public static void setUUIDIfPresentElse(CompoundTag nbt, String key, Consumer<UUID> setter, Runnable notPresent) {
        if (nbt.hasUUID(key)) {
            setter.accept(nbt.getUUID(key));
        } else if (hasOldUUID(nbt, key)) {
            setter.accept(getOldUUID(nbt, key));
        } else {
            notPresent.run();
        }
    }

    public static void setBlockPosIfPresent(CompoundTag nbt, String key, Consumer<BlockPos> setter) {
        if (hasTag(nbt, key, TAG_COMPOUND)) {
            setter.accept(NbtUtils.readBlockPos(nbt, key).orElse(null));
        }
    }

    public static void setItemStackIfPresent(CompoundTag nbt, String key, HolderLookup.Provider lookupProvider, Consumer<ItemStack> setter) {
        if (hasTag(nbt, key, TAG_COMPOUND)) {
            setter.accept(ItemStack.parseOptional(lookupProvider, nbt.getCompound(key)));
        }
    }

    public static void setResourceLocationIfPresent(CompoundTag nbt, String key, Consumer<ResourceLocation> setter) {
        if (hasTag(nbt, key, TAG_STRING)) {
            ResourceLocation value = ResourceLocation.tryParse(nbt.getString(key));
            if (value != null) {
                setter.accept(value);
            }
        }
    }

    public static void setResourceLocationIfPresentElse(CompoundTag nbt, String key, Consumer<ResourceLocation> setter, Runnable notPresent) {
        if (hasTag(nbt, key, TAG_STRING)) {
            ResourceLocation value = ResourceLocation.tryParse(nbt.getString(key));
            if (value == null) {
                notPresent.run();
            } else {
                setter.accept(value);
            }
        }
    }

    public static <ENUM extends Enum<ENUM>> void setEnumIfPresent(CompoundTag nbt, String key, it.unimi.dsi.fastutil.ints.Int2ObjectFunction<ENUM> indexLookup, Consumer<ENUM> setter) {
        if (hasTag(nbt, key, TAG_INT)) {
            setter.accept(indexLookup.apply(nbt.getInt(key)));
        }
    }
}
