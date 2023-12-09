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
    public static ForgeConfigSpec.IntValue selfBanPermissionLevel;
    public static ForgeConfigSpec.IntValue banHammerPermissionLevel;
    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        discordWebhookURL = builder.define("discord_webhook_url","PUT_WEBHOOK_URL_HERE");
        modlogWebhookURL = builder.define("moderation_webhook_url","PUT_MDOERATION_WEBHOOK_URL_HERE");
        discordWebhookUsername = builder.define("username","Ban Hammer");
        discordWebhookAvatar = builder.define("avatar","https://cdn.discordapp.com/avatars/1071948908336529492/4c3e372262e35a7b0e5cdbb5957a57b5.png");
        builder.comment("This controls the permission level below which you will get self banned, default: 2");
        selfBanPermissionLevel = builder.defineInRange("self_ban_permission_level",2,0,4);
        builder.comment("This controls the permission level required to use the Ban Hammer or Kick Stick: Default Value: 3");
        banHammerPermissionLevel = builder.defineInRange("ban_hammer_permission_level",3,0,4);
    }
}
