package com.herrkatze.banhammer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class KickStickScreen extends Screen {
    protected Component BAN_REASON = Component.translatable("banhammer.kickreason");
    protected Component SET_BAN_REASON = Component.translatable("banhammer.setkickreason");
    protected EditBox reasonBox;
    protected Button doneButton;
    protected KickStickScreen() {
        super(GameNarrator.NO_TITLE);
    }
    protected void init() {
        this.doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (p_97691_) -> {
            this.onDone();
        }).bounds(this.width / 2 - 75, this.height / 4 + 120 + 12, 150, 20).build());
        this.reasonBox = new EditBox(this.font, this.width / 2 - 150, 50, 300, 20, Component.translatable("banhammer.reasonInput")) {};
        this.reasonBox.setMaxLength(32500);
        this.addWidget(this.reasonBox);
        this.setInitialFocus(this.reasonBox);
        this.reasonBox.setFocused(true);
    }
    protected void onDone(){
        BanHammerPacketHandler.CHANNEL.sendToServer(new KickReasonPacket(reasonBox.getValue()));
        this.minecraft.setScreen((Screen)null);
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(graphics);
        graphics.drawCenteredString(this.font, SET_BAN_REASON, this.width / 2, 20, 16777215);
        graphics.drawString(this.font, BAN_REASON, this.width / 2-150 , 40, 10526880);
        this.reasonBox.render(graphics, pMouseX, pMouseY, pPartialTick);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

}
