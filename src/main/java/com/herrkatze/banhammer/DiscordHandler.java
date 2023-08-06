package com.herrkatze.banhammer;

public class DiscordHandler {


    public DiscordHandler(){



    }

    public static DiscordWebhook hook() {
        return hook(false);
    }

    public static DiscordWebhook hook(boolean isModerationLog){
        String url;
        if (isModerationLog) {
            url = BanHammerServerConfig.modlogWebhookURL.get();
        }
        else {
            url = BanHammerServerConfig.discordWebhookURL.get();
        }
        DiscordWebhook hook = new DiscordWebhook(url);
        hook.setUsername(BanHammerServerConfig.discordWebhookUsername.get());
        hook.setAvatarUrl(BanHammerServerConfig.discordWebhookAvatar.get());
        return hook;
    }

}
