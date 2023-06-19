package com.herrkatze.banhammer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

import static com.herrkatze.banhammer.BanHammer.discordHandler;

public class BHReportPacket {

    public BHReportPacket(String username, String reason,String evidence){
        this.username = username;
        this.reason = reason;
        this.evidence = evidence;
    }

    protected String username;
    protected String reason;
    protected String evidence;
    public static void toBytes(final BHReportPacket msg, final FriendlyByteBuf buf){
        buf.writeUtf(msg.username);
        buf.writeUtf(msg.reason);
        buf.writeUtf(msg.evidence);

    }
    public static BHReportPacket fromBytes(final FriendlyByteBuf buf){
        final String username = buf.readUtf();
        final String reason = buf.readUtf();
        final String evidence = buf.readUtf();
        return new BHReportPacket(username,reason,evidence);
    }
    public static void handlePacket(final BHReportPacket message, final Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
            context.enqueueWork(() -> {
                DiscordWebhook hook = discordHandler.hook();
                hook.setContent("You have received a report");
                hook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Report for "+message.username).setDescription(message.username+" was reported by "+context.getSender().getName().getString()+"\\nfor reason\\n"+message.reason+"\\nEvidence provided:\\n"+ message.evidence));
                try {
                    hook.execute();
                } catch (IOException e) {

                    BanHammer.LOGGER.error(String.valueOf(e));
                }
            });
        }
        context.setPacketHandled(true);
    }
}
