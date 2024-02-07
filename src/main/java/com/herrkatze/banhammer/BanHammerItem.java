package com.herrkatze.banhammer;

import com.herrkatze.banhammer.lists.damageTypeList;
import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static net.minecraftforge.fml.loading.FMLEnvironment.dist;


public class BanHammerItem extends Item {
    public BanHammerItem(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if(hand == InteractionHand.MAIN_HAND && dist == Dist.CLIENT){
            BanHammerScreenLoader.loadBanReasonGui(player,false);
        }
        return super.use(world, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable(this.getDescriptionId() + ".description_1").withStyle(ChatFormatting.GOLD));
            pTooltipComponents.add(Component.translatable(this.getDescriptionId() + ".description_2").withStyle(ChatFormatting.GOLD));
            pTooltipComponents.add(Component.translatable(this.getDescriptionId() + ".description_3").withStyle(ChatFormatting.GOLD));
        }
        else{
            pTooltipComponents.add(Component.translatable("banhammer.hold_shift").withStyle(ChatFormatting.DARK_GRAY));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player attackerPlayer, Entity targetEntity) {
        if (targetEntity instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) targetEntity;
            if (target instanceof Player) {
                Player targetPlayer = (Player) target;
                Level lvl = targetPlayer.level();
                if (lvl instanceof ServerLevel && attackerPlayer.hasPermissions(BanHammerServerConfig.banHammerPermissionLevel.get())) {
                    Registry<DamageType> damageTypeRegistry = lvl.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
                    DamageSource source = new DamageSource(damageTypeRegistry.getHolderOrThrow(damageTypeList.BAN),targetPlayer,attackerPlayer);
                    targetPlayer.hurt(source, targetPlayer.getHealth() * 6 + targetPlayer.getAbsorptionAmount() * 6);
                    ServerLevel serverlvl = (ServerLevel) lvl;
                    MinecraftServer server = serverlvl.getServer();
                    UserBanList userbanlist = server.getPlayerList().getBans();
                    GameProfile profile = targetPlayer.getGameProfile();
                    CompoundTag Tag = attackerPlayer.getInventory().getSelected().getTag();
                    String reason;
                    Date expiryTime = null;
                    if (Tag == null || Tag.getString("reason").equals("")) {
                        reason = "Banned by an operator";
                    } else {
                        reason = Tag.getString("reason");
                        if (Tag.getLong("time") != 0) {
                            expiryTime = new Date(Tag.getLong("time") + new Date().getTime());
                        }
                    }
                    targetPlayer.getInventory().dropAll();
                    UserBanListEntry banListEntry = new UserBanListEntry(profile, new Date(), attackerPlayer.getDisplayName().getString(), expiryTime, reason);
                    userbanlist.add(banListEntry);

                    server.getPlayerList().getPlayer(profile.getId()).connection.disconnect(Component.translatable("multiplayer.disconnect.banned").append(". Reason:  ").append(reason));
                    DiscordWebhook hook = DiscordHandler.hook(true);
                    String banreason = "Reason: "+reason;
                    if(expiryTime != null) {
                        banreason = banreason + "\\nExpires: <t:" +expiryTime.getTime()/1000+":F>";
                    }
                    hook.setContent(targetPlayer.getDisplayName().getString() + " was Banned!");
                    hook.addEmbed(new DiscordWebhook.EmbedObject().setTitle(targetPlayer.getDisplayName().getString()).setDescription(banreason).setFooter(attackerPlayer.getDisplayName().getString(),"https://mc-heads.net/head/"+ attackerPlayer.getUUID() + "/right",new Date()).setThumbnail("https://mc-heads.net/head/"+targetPlayer.getUUID()+"/right"));
                    try {
                        hook.execute();
                    } catch (IOException e) {
                        BanHammer.LOGGER.error("Failed to execute Webhook");
                    }

                    return super.onLeftClickEntity(stack, attackerPlayer, targetEntity);
                }
                else if (lvl instanceof ServerLevel && !attackerPlayer.hasPermissions(BanHammerServerConfig.banHammerPermissionLevel.get())) {
                    stack.setCount(0);
                    if(BanHammerCommonConfig.shouldSelfBanIfNotOp.get() && !attackerPlayer.hasPermissions(BanHammerServerConfig.selfBanPermissionLevel.get())) {
                        ServerLevel serverlvl = (ServerLevel) lvl;
                        MinecraftServer server = serverlvl.getServer();
                        UserBanList userbanlist = server.getPlayerList().getBans();
                        GameProfile profile = attackerPlayer.getGameProfile();

                        UserBanListEntry ble = new UserBanListEntry(profile, new Date(), attackerPlayer.getDisplayName().getString(), new Date(3600), "Illegally used Ban Hammer\nBanned for 1 day");
                        userbanlist.add(ble);
                        server.getPlayerList().getPlayer(profile.getId()).connection.disconnect(Component.translatable("multiplayer.disconnect.banned").append(". Reason:  ").append("Illegally used Ban Hammer"));
                        DiscordWebhook hook = DiscordHandler.hook(true);
                        hook.setContent(attackerPlayer.getDisplayName().getString() + " was Banned!");
                        hook.addEmbed(new DiscordWebhook.EmbedObject().setTitle(attackerPlayer.getDisplayName().getString()).setDescription("Illegally used Ban Hammer\\nExpires: <t:" + new Date(new Date().getTime() + 24 * 3600 * 1000).getTime() + ":F>").setThumbnail("https://mc-heads.net/head/"+attackerPlayer.getUUID()+"/right").setFooter("Self Ban","https://mc-heads.net/head/"+attackerPlayer.getUUID()+"/right",new Date()));
                        try {
                            hook.execute();
                        } catch (IOException e) {
                            BanHammer.LOGGER.error("Failed to execute Webhook");
                        }

                    }
                }
            }
            else {
                Level lvl = attackerPlayer.level();
                Registry<DamageType> damageTypeRegistry = lvl.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
                DamageSource source = new DamageSource(damageTypeRegistry.getHolderOrThrow(damageTypeList.BAN),target,attackerPlayer);
                target.hurt(source, target.getHealth() * 6 + target.getAbsorptionAmount() * 6);
            }
        }
        return super.onLeftClickEntity(stack, attackerPlayer, targetEntity);
    }


}
