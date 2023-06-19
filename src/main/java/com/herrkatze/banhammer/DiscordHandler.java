package com.herrkatze.banhammer;

public class DiscordHandler {


    public DiscordHandler(){



    }

    public static DiscordWebhook hook(){
        DiscordWebhook hook = new DiscordWebhook(BanHammerServerConfig.discordWebhookURL.get());
        hook.setUsername(BanHammerServerConfig.discordWebhookUsername.get());
        hook.setAvatarUrl(BanHammerServerConfig.discordWebhookAvatar.get());
        return hook;
    }

}
