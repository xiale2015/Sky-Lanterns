package com.xl.skylantern.common.configs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfig {
    public static final Common COMMON;
    public static final ModConfigSpec CONFIG_SPEC;

    static {
        final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        COMMON = new Common(builder);
        CONFIG_SPEC = builder.build();
    }

    public static class Common {
        public final ModConfigSpec.IntValue lightUpdateRate;
        public final ModConfigSpec.IntValue lightUpdateDistanceAccuracy;
        public final ModConfigSpec.IntValue lightUpdateDistanceToGround;

        public Common(ModConfigSpec.Builder builder) {
            lightUpdateRate = builder
                    .comment("检查天灯是否应该放置一个新的不可见光源的时间间隔，性能敏感，设置为-1以使其永远不会放置不可见光块")
                    .defineInRange("lightUpdateRate", 2, -1, Integer.MAX_VALUE);
            lightUpdateDistanceAccuracy = builder
                    .comment("天灯在放置新光源之前需要与之前放置的光源相距多远，性能敏感")
                    .defineInRange("lightUpdateDistanceAccuracy", 2, 0, Integer.MAX_VALUE);
            lightUpdateDistanceToGround = builder
                    .comment("天灯需要离地面多近才能放置光源，这样可以防止将光源放置在天空中的高处，这会降低性能，对性能非常敏感")
                    .defineInRange("lightUpdateDistanceToGround", 256, 0, Integer.MAX_VALUE);
        }
    }
}
