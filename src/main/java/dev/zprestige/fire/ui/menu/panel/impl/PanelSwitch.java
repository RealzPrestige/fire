package dev.zprestige.fire.ui.menu.panel.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.ui.menu.panel.PanelScreen;
import dev.zprestige.fire.ui.menu.panel.PanelSetting;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;

import java.awt.*;

public class PanelSwitch extends PanelSetting {
    protected Switch setting;
    protected float[] bg = new float[]{PanelScreen.secondBackgroundColor.getRed() / 255.0f, PanelScreen.secondBackgroundColor.getGreen() / 255.0f, PanelScreen.secondBackgroundColor.getBlue() / 255.0f, 1.0f}, ec = new float[]{PanelScreen.secondBackgroundColor.getRed() / 255.0f, PanelScreen.secondBackgroundColor.getGreen() / 255.0f, PanelScreen.secondBackgroundColor.getBlue() / 255.0f};
    protected float circleX, lastFrameX;

    public PanelSwitch(final Switch setting, final float x, final float y, final float width, final float height) {
        super(setting, x, y, width, height);
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.circleX = x + width - 25;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        final float y = this.y - secondY;
        super.render(mouseX, mouseY);
        setBackgroundColor(mouseX, mouseY, PanelScreen.animationFactor() * 10);
        setEcColor(PanelScreen.animationFactor() * 10);
        RenderUtil.prepareScale(0.73f);
        Main.fontManager.drawStringWithShadow(setting.getName(), new Vector2D(x / 0.73f, (y + (height / 2f) - Main.fontManager.getFontHeight() / 2.0f) / 0.73f), new Color(col[0], col[1], col[2], 1.0f).getRGB());
        RenderUtil.releaseScale();
        for (int i = 20; i <= 27; i = i + 2) {
            final float val = ((27 - i) / 2f);
            RenderUtil.drawRoundedRect(x + width - 24, y + 5, x + width - 4 + val, y + height - 3 + val, 7, new Color(0, 0, 0, i));
        }
        RenderUtil.drawRoundedRect(x + width - 24, y + 5, x + width - 4, y + height - 3, 7, PanelScreen.thirdBackgroundColor);
        RenderUtil.drawRoundedRect(x + width - 23, y + 6, x + width - 5, y + height - 4, 6, new Color(bg[0], bg[1], bg[2], bg[3]));
        if (circleX < x || lastFrameX != x) {
            circleX = setting.GetSwitch() ? x + width - 11 : x + width - 27;
        }
        if (setting.GetSwitch()) {
            circleX = normalizeNumber(circleX, x + width - 11, PanelScreen.animationFactor() * 10);
        } else {
            circleX = normalizeNumber(circleX, x + width - 27, PanelScreen.animationFactor() * 10);
        }
        for (int i = 20; i <= 27; i = i + 2) {
            final float val = ((27 - i) / 2f);
            RenderUtil.drawRoundedRect(circleX - 0.5f - val, y + 3.5 - val, circleX + 9.5f + val, y + height - 1.5f + val, 9, new Color(0, 0, 0, i));
        }
        RenderUtil.drawRoundedRect(circleX - 0.5f, y + 3.5, circleX + 9.5f, y + height - 1.5f, 9, PanelScreen.thirdBackgroundColor);
        RenderUtil.drawRoundedRect(circleX, y + 4, circleX + 9, y + height - 2, 8, setting.GetSwitch() ? new Color(ec[0], ec[1], ec[2], 1.0f) : new Color(bg[0], bg[1], bg[2], 1.0f));
        lastFrameX = x;
    }

    @Override
    public void click(int mouseX, int mouseY, int state) {
        if (insideSwitch(mouseX, mouseY) && state == 0) {
            if (setting.getName().equals("Enabled")){
                if (!setting.GetSwitch()){
                    setting.getModule().enableModule();
                } else {
                    setting.getModule().disableModule();
                }
                return;
            }
            setting.setValue(!setting.GetSwitch());
        }
    }

    @Override
    public float getHeight() {
        return height;
    }

    protected boolean insideSwitch(int mouseX, int mouseY) {
        return mouseX > x + width - 24 && mouseX < x + width - 4 && mouseY > y + 5 && mouseY < y + height - 3;
    }

    protected void setEcColor(final float animationFactor) {
        final Color color = PanelScreen.PANEL.color.GetColor();
        if (setting.GetSwitch()) {
            ec[0] = normalizeNumber(ec[0], color.getRed() / 255.0f, animationFactor);
            ec[1] = normalizeNumber(ec[1], color.getGreen() / 255.0f, animationFactor);
            ec[2] = normalizeNumber(ec[2], color.getBlue() / 255.0f, animationFactor);

        } else {
            ec[0] = normalizeNumber(ec[0], PanelScreen.secondBackgroundColor.getRed() / 255.0f, animationFactor);
            ec[1] = normalizeNumber(ec[1], PanelScreen.secondBackgroundColor.getGreen() / 255.0f, animationFactor);
            ec[2] = normalizeNumber(ec[2], PanelScreen.secondBackgroundColor.getBlue() / 255.0f, animationFactor);
        }
    }

    protected void setBackgroundColor(final int mouseX, final int mouseY, final float animationFactor) {
        final Color color = PanelScreen.PANEL.color.GetColor();
        if (inside(mouseX, mouseY) || setting.GetSwitch()) {
            bg[0] = normalizeNumber(bg[0], color.getRed() / 255.0f, animationFactor);
            bg[1] = normalizeNumber(bg[1], color.getGreen() / 255.0f, animationFactor);
            bg[2] = normalizeNumber(bg[2], color.getBlue() / 255.0f, animationFactor);
            bg[3] = normalizeNumber(bg[3], 0.2f, animationFactor);

        } else {
            bg[0] = normalizeNumber(bg[0], PanelScreen.secondBackgroundColor.getRed() / 255.0f, animationFactor);
            bg[1] = normalizeNumber(bg[1], PanelScreen.secondBackgroundColor.getGreen() / 255.0f, animationFactor);
            bg[2] = normalizeNumber(bg[2], PanelScreen.secondBackgroundColor.getBlue() / 255.0f, animationFactor);
            bg[3] = normalizeNumber(bg[3], 1.0f, animationFactor);
        }
    }
}
