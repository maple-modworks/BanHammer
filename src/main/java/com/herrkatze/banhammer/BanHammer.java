package com.herrkatze.banhammer;

import com.herrkatze.banhammer.lists.itemList;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import org.slf4j.Logger;

import java.util.Optional;

import static com.herrkatze.banhammer.BanHammerServerConfig.GENERAL_SPEC;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BanHammer.MODID)
public class BanHammer
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "banhammer";
    // Directly reference a slf4j logger
    static final Logger LOGGER = LogUtils.getLogger();

    public static DiscordHandler discordHandler = new DiscordHandler();

    public BanHammer()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered

        // Register the Deferred Register to the mod event bus so items get registered
        itemList.ITEMS.register(modEventBus);
        // Register forge config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER,GENERAL_SPEC,"banhammer-server.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,BanHammerCommonConfig.GENERAL_SPEC,"banhammer-common.toml");
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        int id = 0;
        BanHammerPacketHandler.CHANNEL.registerMessage(id++,BanReasonPacket.class,BanReasonPacket::toBytes,BanReasonPacket::fromBytes,BanReasonPacket::handlePacket, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        BanHammerPacketHandler.CHANNEL.registerMessage(id++,KickReasonPacket.class,KickReasonPacket::toBytes,KickReasonPacket::fromBytes,KickReasonPacket::handlePacket,Optional.of(NetworkDirection.PLAY_TO_SERVER));
        BanHammerPacketHandler.CHANNEL.registerMessage(id++,BHReportPacket.class,BHReportPacket::toBytes,BHReportPacket::fromBytes,BHReportPacket::handlePacket,Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
        @SubscribeEvent
        public static void BuildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
            if(event.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
                event.accept(itemList.BAN_HAMMER);
                event.accept(itemList.KICK_STICK);
            }
        }
    }
}
