package com.herrkatze.banhammer.client;

import com.herrkatze.banhammer.BHErrorScreen;
import com.herrkatze.banhammer.BHReportScreen;
import com.herrkatze.banhammer.BanHammer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = BanHammer.MODID,value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (KeyBinds.REPORT_KEY.consumeClick()) {
                Minecraft.getInstance().setScreen(new BHReportScreen());
            }
        }
        @Mod.EventBusSubscriber(modid = BanHammer.MODID,value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents{
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinds.REPORT_KEY);
        }

        }
    }
}