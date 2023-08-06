package com.herrkatze.banhammer;

import net.minecraftforge.common.ForgeConfigSpec;

public class BanHammerServerConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }
    public static ForgeConfigSpec.ConfigValue<String> discordWebhookURL;
    public static ForgeConfigSpec.ConfigValue<String> modlogWebhookURL;
    public static ForgeConfigSpec.ConfigValue<String> discordWebhookUsername;
    public static ForgeConfigSpec.ConfigValue<String> discordWebhookAvatar;
    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        discordWebhookURL = builder.define("discord_webhook_url","PUT_WEBHOOK_URL_HERE");
        modlogWebhookURL = builder.define("moderation_webhook_url","PUT_MDOERATION_WEBHOOK_URL_HERE");
        discordWebhookUsername = builder.define("username","Ban Hammer");
        discordWebhookAvatar = builder.define("avatar","https://cdn.discordapp.com/avatars/1071948908336529492/4c3e372262e35a7b0e5cdbb5957a57b5.png");

    }
}
