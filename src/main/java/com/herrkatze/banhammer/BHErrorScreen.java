package com.herrkatze.banhammer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class BHErrorScreen extends Screen {
    protected Component ERROR_TITLE = Component.translatable("banhammer.errortitle");
    private Component err;

    protected Button doneButton;
    public BHErrorScreen(String err) {
        super(GameNarrator.NO_TITLE);
        this.err = Component.translatable(err);
    }
    protected void init() {
        this.doneButton = this.addRenderableWidget(new Button(this.width / 2-75 , this.height / 4 + 120 + 12, 150, 20, CommonComponents.GUI_DONE, (p_97691_) -> {
            this.onDone();
        }));

    }
    protected void onDone(){
        this.minecraft.setScreen((Screen)null);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        drawCenteredString(pPoseStack, this.font, ERROR_TITLE, this.width / 2, 20, 0xee362e);
        drawCenteredString(pPoseStack, this.font, err, this.width / 2 , 40, 10526880);
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
