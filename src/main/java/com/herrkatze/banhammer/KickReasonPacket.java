package com.herrkatze.banhammer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class KickReasonPacket {
    private int entity;

    public KickReasonPacket(String reason){
        this.reason = reason;
    }


    protected String reason;
    public static void toBytes(final KickReasonPacket msg, final FriendlyByteBuf buf){
        buf.writeUtf(msg.reason);
    }
    public static KickReasonPacket fromBytes(final FriendlyByteBuf buf){
        final String reason = buf.readUtf();
        return new KickReasonPacket(reason);
    }
    public static void handlePacket(final KickReasonPacket message, final Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
            context.enqueueWork(() -> {
                // locate the server-side entity and play the sound at its location
                ServerPlayer player = context.getSender();
                ItemStack stack = player.getInventory().getSelected();
                if (stack.getItem() instanceof KickStick){
                    CompoundTag Tag =stack.getOrCreateTag();
                    Tag.putString("reason",message.reason);
                    stack.setTag(Tag);
                }

            });
        }
        context.setPacketHandled(true);
    }
}
