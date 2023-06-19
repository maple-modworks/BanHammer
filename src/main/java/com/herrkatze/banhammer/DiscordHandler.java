package com.herrkatze.banhammer;

public class DiscordHandler {


    public DiscordHandler(){



    }

    public static DiscordWebhook hook(){
        DiscordWebhook hook = new DiscordWebhook(BanHammerConfig.discordWebhookURL.get());
        hook.setUsername(BanHammerConfig.discordWebhookUsername.get());
        hook.setAvatarUrl(BanHammerConfig.discordWebhookAvatar.get());
        return hook;
    }

}
