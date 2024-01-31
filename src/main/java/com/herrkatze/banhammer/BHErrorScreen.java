package com.herrkatze.banhammer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;

public class BHErrorScreen extends Screen {
    protected Component ERROR_TITLE = Component.translatable("banhammer.errortitle");
    private Component err;

    protected Button doneButton;
    public BHErrorScreen(String err) {
        super(GameNarrator.NO_TITLE);
        this.err = Component.translatable(err);
    }

    protected void init() {
        this.doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE,(awefa) -> {
            this.onDone();
        }).bounds(this.width / 2-75 , this.height / 4 + 120 + 12, 150, 20).build());
    }
    protected void onDone(){
        this.minecraft.setScreen((Screen)null);
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(graphics);
        
        graphics.drawCenteredString(this.font, ERROR_TITLE, this.width / 2, 20, 0xee362e);
        graphics.drawCenteredString(this.font, err, this.width / 2 , 40, 10526880);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }



}
