package com.herrkatze.banhammer.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {
    public static final String KEY_CATEGORY_BANHAMMER = "key.category.banhammer.banhammer";
    public static final String KEY_OPEN_REPORT = "key.banhammer.open_report";


    public static final KeyMapping REPORT_KEY = new KeyMapping(KEY_OPEN_REPORT, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F7,KEY_CATEGORY_BANHAMMER);
}
