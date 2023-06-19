package com.herrkatze.banhammer;

import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;


public class BanHammerItem extends Item {
    public BanHammerItem(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if(hand == InteractionHand.MAIN_HAND && world.isClientSide()){
            BanHammerScreenLoader.loadBanReasonGui(player,false);
        }
        return super.use(world, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable(this.getDescriptionId() + ".description").withStyle(ChatFormatting.GOLD));
        }
        else{
            pTooltipComponents.add(Component.translatable(this.getDescriptionId() + ".hold_shift").withStyle(ChatFormatting.DARK_GRAY));
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
                Level lvl = targetPlayer.getLevel();
                if (lvl instanceof ServerLevel && attackerPlayer.hasPermissions(3)) {
                    targetPlayer.hurt(new EntityDamageSource("ban.player", attackerPlayer).bypassArmor().bypassEnchantments().bypassInvul().bypassMagic(), targetPlayer.getHealth() * 6 + targetPlayer.getAbsorptionAmount() * 6);
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
                    return super.onLeftClickEntity(stack, attackerPlayer, targetEntity);
                }
                else if (lvl instanceof ServerLevel && !attackerPlayer.hasPermissions(3)) {
                    stack.setCount(0);
                    if(BanHammerCommonConfig.shouldSelfBanIfNotOp.get()) {
                        ServerLevel serverlvl = (ServerLevel) lvl;
                        MinecraftServer server = serverlvl.getServer();
                        UserBanList userbanlist = server.getPlayerList().getBans();
                        GameProfile profile = attackerPlayer.getGameProfile();

                        UserBanListEntry ble = new UserBanListEntry(profile, new Date(), attackerPlayer.getDisplayName().getString(), new Date(3600), "Illegally used Ban Hammer\nBanned for 1 day");
                        userbanlist.add(ble);
                        server.getPlayerList().getPlayer(profile.getId()).connection.disconnect(Component.translatable("multiplayer.disconnect.banned").append(". Reason:  ").append("Illegally used Ban Hammer"));
                    }
                }
            }
            else {
                target.hurt(new EntityDamageSource("ban.player", attackerPlayer).bypassArmor().bypassEnchantments().bypassInvul().bypassMagic(), target.getHealth() * 6 + target.getAbsorptionAmount() * 6);
            }
        }
        return super.onLeftClickEntity(stack, attackerPlayer, targetEntity);
    }


}
