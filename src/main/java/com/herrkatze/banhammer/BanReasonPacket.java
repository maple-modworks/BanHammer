package com.herrkatze.banhammer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BanReasonPacket {



    public BanReasonPacket(String reason,long time){
        this.reason = reason;
        this.time = time;
    }
    protected long time;
    protected String reason;
    public static void toBytes(final BanReasonPacket msg, final FriendlyByteBuf buf){
        buf.writeUtf(msg.reason);
        buf.writeLong(msg.time);
    }
    public static BanReasonPacket fromBytes(final FriendlyByteBuf buf){
        final String reason = buf.readUtf();
        final long time = buf.readLong();
        return new BanReasonPacket(reason,time);
    }
    public static void handlePacket(final BanReasonPacket message, final Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
            context.enqueueWork(() -> {
                // locate the server-side entity and play the sound at its location
                ServerPlayer player = context.getSender();
                ItemStack stack = player.getInventory().getSelected();
                if (stack.getItem() instanceof BanHammerItem){
                    CompoundTag Tag =stack.getOrCreateTag();
                    Tag.putString("reason",message.reason);
                    Tag.putLong("time",message.time);
                    stack.setTag(Tag);
                }

            });
        }
        context.setPacketHandled(true);
    }
}
