package com.herrkatze.banhammer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Date;

public class BanHammerScreen extends Screen {
    protected Component BAN_REASON = Component.translatable("banhammer.banreason");
    protected Component SET_BAN_REASON = Component.translatable("banhammer.setbanreason");
    protected Component SET_HOURS = Component.translatable("banhammer.hours");
    protected Component SET_DAYS = Component.translatable("banhammer.days");
    protected EditBox reasonBox;
    protected EditBox hours;
    protected EditBox days;
    protected Button doneButton;
    protected BanHammerScreen() {
        super(GameNarrator.NO_TITLE);
    }
    protected void init() {


        this.doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (p_97691_) -> {
            this.onDone();
        }).bounds(this.width / 2-75 , this.height / 4 + 120 + 12, 150, 20).build());
        this.reasonBox = new EditBox(this.font, this.width / 2 - 150, 50, 300, 20, Component.translatable("banhammer.reasonInput")) {};
        this.days = new EditBox(this.font, this.width / 2 - 150, 90, 100, 20, Component.translatable("banhammer.daysInput")) {};
        this.hours = new EditBox(this.font, this.width / 2 + 50 , 90, 100, 20, Component.translatable("banhammer.hoursInput")) {};
        this.reasonBox.setMaxLength(32500);
        this.days.setMaxLength(100);

        this.hours.setMaxLength(100);
        this.addWidget(this.reasonBox);
        this.addWidget(this.days);
        this.addWidget(this.hours);
        this.setInitialFocus(this.reasonBox);

    }
    protected void onDone(){
        long time=0;
        int hour=0;
        String hourstr = hours.getValue();
        String daystr = days.getValue();
        if (!hourstr.equals("") || !daystr.equals("")) {
            if (hourstr.equals("")){
                hourstr = "0";
            }
            if(daystr.equals("")){
             daystr = "0";
            }
            try {
                time = Long.parseLong(daystr);

            }
            catch(NumberFormatException e){
                this.minecraft.setScreen(new BHErrorScreen("banhammer.days_error"));
                return;
            }
            try{
                hour = Integer.parseInt(hourstr);
            }
            catch (NumberFormatException e){
                this.minecraft.setScreen(new BHErrorScreen("banhammer.hours_error"));
                return;
            }

            time = time * 24;
            time = time + hour;
            time = time * 60 * 60 * 1000;
        }
        else {
            time = 0;
        }
        BanHammerPacketHandler.CHANNEL.sendToServer(new BanReasonPacket(reasonBox.getValue(),time));
        this.minecraft.setScreen((Screen)null);
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(graphics);
        graphics.drawCenteredString(this.font, SET_BAN_REASON, this.width / 2, 20, 16777215);
        graphics.drawString(this.font, BAN_REASON, this.width / 2-150 , 40, 10526880);
        graphics.drawString(this.font,SET_DAYS,this.width/2 - 150,80,10526880);
        graphics.drawString(this.font,SET_HOURS,this.width/2 + 50,80,10526880);
        this.reasonBox.render(graphics, pMouseX, pMouseY, pPartialTick);
        this.hours.render(graphics,pMouseX,pMouseY,pPartialTick);
        this.days.render(graphics,pMouseX,pMouseY,pPartialTick);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }


}
