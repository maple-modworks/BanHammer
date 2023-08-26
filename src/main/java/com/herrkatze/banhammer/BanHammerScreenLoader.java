package com.herrkatze.banhammer;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;

import static net.minecraftforge.fml.loading.FMLEnvironment.dist;

public class BanHammerScreenLoader {

    private BanHammerScreenLoader() {
    }

    public static void loadBanReasonGui(final Player playerIn,final boolean kick) {
        // only load client-side, of course


        // open the gui
        if (!kick) {
            Minecraft.getInstance().setScreen(new BanHammerScreen());
        }
        else{
            Minecraft.getInstance().setScreen(new KickStickScreen());
        }
    }
}
