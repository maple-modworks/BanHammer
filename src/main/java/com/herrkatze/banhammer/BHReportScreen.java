package com.herrkatze.banhammer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class BHReportScreen extends Screen {
    protected Component REPORT_REASON = Component.translatable("banhammer.reportreason");
    protected Component REPORT_SCREEN_TITLE = Component.translatable("banhammer.reportscreentitle");
    protected Component REPORT_USERNAME = Component.translatable("banhammer.reportusername");
    protected Component REPORT_EVIDENCE = Component.translatable("banhammer.evidenceInput");
    protected EditBox reasonBox;

    protected EditBox usernameBox;
    protected EditBox evidenceBox;
    protected Button doneButton;
    public BHReportScreen() {
        super(GameNarrator.NO_TITLE);
    }
    protected void init() {

        this.doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (p_97691_) -> {
            this.onDone();
        }).bounds(this.width / 2-75 , this.height / 4 + 120 + 12, 150, 20).build());
        this.reasonBox = new EditBox(this.font, this.width / 2 - 150, 90, 300, 20, Component.translatable("banhammer.reasonInput")) {};
        this.usernameBox = new EditBox(this.font, this.width / 2 - 50, 50, 100, 20, Component.translatable("banhammer.usernameInput")) {};

        this.evidenceBox = new EditBox(this.font,this.width/2-150,130,300,20,Component.translatable("banhammer.evidenceInput"));

        this.reasonBox.setMaxLength(32500);
        this.evidenceBox.setMaxLength(32500);
        this.usernameBox.setMaxLength(16);
        this.addWidget(this.usernameBox);
        this.addWidget(this.reasonBox);
        this.addWidget(this.evidenceBox);
        this.setInitialFocus(this.usernameBox);

    }
    protected void onDone(){
        if (!usernameBox.getValue().equals("") && !reasonBox.getValue().equals("") && !evidenceBox.getValue().equals("")) {
            BanHammerPacketHandler.CHANNEL.sendToServer(new BHReportPacket(usernameBox.getValue(), reasonBox.getValue(), evidenceBox.getValue()));
            this.minecraft.setScreen((Screen) null);
        }
        else{
            this.minecraft.setScreen(new BHErrorScreen("banhammer.reportArgsRequired"));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(graphics);
        graphics.drawCenteredString(this.font, REPORT_SCREEN_TITLE, this.width / 2, 20, 0xFFFFFF);
        graphics.drawString(this.font, REPORT_USERNAME, this.width / 2-50 , 40, 0xA0A0A0);
        graphics.drawString(this.font,REPORT_REASON,this.width/2 - 150,80,0xA0A0A0);
        graphics.drawString(this.font,REPORT_EVIDENCE,this.width/2-150,120,0xA0A0A0);
        this.usernameBox.render(graphics,pMouseX,pMouseY,pPartialTick);
        this.reasonBox.render(graphics, pMouseX, pMouseY, pPartialTick);
        this.evidenceBox.render(graphics,pMouseX,pMouseY,pPartialTick);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

}
