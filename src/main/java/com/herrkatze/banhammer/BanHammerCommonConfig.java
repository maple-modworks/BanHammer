package com.herrkatze.banhammer;

import net.minecraftforge.common.ForgeConfigSpec;

public class BanHammerCommonConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }
    public static ForgeConfigSpec.BooleanValue shouldSelfBanIfNotOp;

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
    shouldSelfBanIfNotOp = builder.define("shouldSelfBanIfNotOp",false);


    }
}
