package com.herrkatze.banhammer;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class BanHammerScreenLoader {

    private BanHammerScreenLoader() {
        //
    }

    public static void loadBanReasonGui(final Player playerIn,final boolean kick) {
        // only load client-side, of course
        if (!playerIn.getCommandSenderWorld().isClientSide()) {
            return;
        }
        // open the gui
        if (!kick) {
            Minecraft.getInstance().setScreen(new BanHammerScreen());
        }
        else{
            Minecraft.getInstance().setScreen(new KickStickScreen());
        }
    }
}
