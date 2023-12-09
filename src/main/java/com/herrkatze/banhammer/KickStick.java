package com.herrkatze.banhammer;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;

import java.io.IOException;
import java.util.Date;

import static net.minecraftforge.fml.loading.FMLEnvironment.dist;

public class KickStick extends Item {


    public KickStick(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if(hand == InteractionHand.MAIN_HAND && dist == Dist.CLIENT){
            BanHammerScreenLoader.loadBanReasonGui(player,true);
        }
        return super.use(world, player, hand);
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public boolean  onLeftClickEntity(ItemStack stack, Player attackerPlayer, Entity targetEntity) {
        if (targetEntity instanceof Player) {
            Player targetPlayer = (Player) targetEntity;
            Level lvl = targetPlayer.getLevel();
            if (lvl instanceof ServerLevel && attackerPlayer.hasPermissions(3)) {
                ServerLevel serverlvl = (ServerLevel) lvl;
                MinecraftServer server = serverlvl.getServer();
                GameProfile profile = targetPlayer.getGameProfile();
                CompoundTag Tag = attackerPlayer.getInventory().getSelected().getTag();
                String reason;
                if (Tag == null || Tag.getString("reason").equals("")) {
                    reason = "Kicked by an operator";
                } else {
                    reason = Tag.getString("reason");
                }
                server.getPlayerList().getPlayer(profile.getId()).connection.disconnect(Component.literal(reason));
                DiscordWebhook hook = DiscordHandler.hook(true);
                String kickreason = "Reason: " + reason;
                hook.setContent(targetPlayer.getDisplayName().getString() + " was Kicked!");
                hook.addEmbed(new DiscordWebhook.EmbedObject().setTitle(targetPlayer.getDisplayName().getString()).setDescription(kickreason).setFooter(attackerPlayer.getDisplayName().getString(), "https://mc-heads.net/head/" + attackerPlayer.getUUID() + "/right", new Date()).setThumbnail("https://mc-heads.net/head/" + targetPlayer.getUUID() + "/right"));
                try {
                    hook.execute();
                } catch (IOException e) {
                    BanHammer.LOGGER.error("Failed to execute Webhook");
                }
            }
            else if (lvl instanceof ServerLevel && !attackerPlayer.hasPermissions(BanHammerServerConfig.banHammerPermissionLevel.get())) {
                stack.setCount(0);
                if(BanHammerCommonConfig.shouldSelfBanIfNotOp.get() && !attackerPlayer.hasPermissions(BanHammerServerConfig.selfBanPermissionLevel.get())) {
                    ServerLevel serverlvl = (ServerLevel) lvl;
                    MinecraftServer server = serverlvl.getServer();
                    UserBanList userbanlist = server.getPlayerList().getBans();
                    GameProfile profile = attackerPlayer.getGameProfile();

                    UserBanListEntry ble = new UserBanListEntry(profile, new Date(), attackerPlayer.getDisplayName().getString(), new Date(3600), "Illegally used Kick Stick\nBanned for 1 day");
                    userbanlist.add(ble);
                    server.getPlayerList().getPlayer(profile.getId()).connection.disconnect(Component.translatable("multiplayer.disconnect.banned").append(". Reason:  ").append("Illegally used Ban Hammer"));
                    DiscordWebhook hook = DiscordHandler.hook(true);
                    hook.setContent(attackerPlayer.getDisplayName().getString() + "was Banned!");
                    hook.addEmbed(new DiscordWebhook.EmbedObject().setTitle(attackerPlayer.getDisplayName().getString()).setDescription("Illegally used Kick Stick\\nExpires: <t:" + new Date(new Date().getTime() + 24 * 3600 * 1000).getTime() + ":F>").setThumbnail("https://mc-heads.net/head/"+attackerPlayer.getUUID()+"/right").setFooter("Self Ban","https://mc-heads.net/head/"+attackerPlayer.getUUID()+"/right",new Date()));
                    try {
                        hook.execute();
                    } catch (IOException e) {
                        BanHammer.LOGGER.error("Failed to execute Webhook");
                    }
                }
            }
        }
        return super.onLeftClickEntity(stack,attackerPlayer,targetEntity);
    }
}