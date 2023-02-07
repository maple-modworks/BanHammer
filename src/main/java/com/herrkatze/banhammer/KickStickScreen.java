package com.herrkatze.banhammer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.GameNarrator;
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
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.doneButton = this.addRenderableWidget(new Button(this.width / 2 - 75, this.height / 4 + 120 + 12, 150, 20, CommonComponents.GUI_DONE, (p_97691_) -> {
            this.onDone();
        }));
        this.reasonBox = new EditBox(this.font, this.width / 2 - 150, 50, 300, 20, Component.translatable("banhammer.reasonInput")) {};
        this.reasonBox.setMaxLength(32500);
        this.addWidget(this.reasonBox);
        this.setInitialFocus(this.reasonBox);
        this.reasonBox.setFocus(true);
    }
    protected void onDone(){
        BanHammerPacketHandler.CHANNEL.sendToServer(new KickReasonPacket(reasonBox.getValue()));
        this.minecraft.setScreen((Screen)null);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        drawCenteredString(pPoseStack, this.font, SET_BAN_REASON, this.width / 2, 20, 16777215);
        drawString(pPoseStack, this.font, BAN_REASON, this.width / 2-150 , 40, 10526880);
        this.reasonBox.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
    public void renderBackground(PoseStack pPoseStack) {
        this.renderBackground(pPoseStack, 0);
    }
    public void renderBackground(PoseStack pPoseStack, int pVOffset) {
        if (this.minecraft.level != null) {
            this.fillGradient(pPoseStack, 0, 0, this.width, this.height, -1072689136, -804253680);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.ScreenEvent.BackgroundRendered(this, pPoseStack));
        } else {
            this.renderDirtBackground(pVOffset);
        }

    }

}
