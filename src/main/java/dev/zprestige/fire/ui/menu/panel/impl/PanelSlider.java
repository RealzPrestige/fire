package dev.zprestige.fire.ui.menu.panel.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.ui.menu.panel.PanelScreen;
import dev.zprestige.fire.ui.menu.panel.PanelSetting;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class PanelSlider extends PanelSetting {
    protected final Slider setting;
    float circleX, extension, lastFrameX;

    public PanelSlider(final Slider setting, final float x, final float y, final float width, final float height) {
        super(setting, x, y, width, height);
        this.setting = setting;
        final float width1 = this.width - Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f;
        circleX = x + (Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f) + (width1 - (Main.fontManager.getStringWidth(setting.getMax() + " ") * 0.73f)) * sliderWidthValue() - 1;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        final float y = this.y - secondY;
        super.render(mouseX, mouseY);
        dragSlider(mouseX, mouseY);
        final float width = this.width - Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f;
        RenderUtil.drawRoundedRect(x + (Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f), y + (height) / 2f - 2, x + (Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f) + (width - (Main.fontManager.getStringWidth(setting.getMax() + " ") * 0.73f)), y + (height) / 2f + 2, 5, PanelScreen.thirdBackgroundColor);
        RenderUtil.drawRoundedRect(x + (Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f) + 1, y + (height) / 2f - 1, x + (Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f) + (width - (Main.fontManager.getStringWidth(setting.getMax() + " ") * 0.73f)) - 1, y + (height) / 2f + 1, 3, PanelScreen.secondBackgroundColor);
        circleX = normalizeNumber(circleX, x + (Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f) + (width - (Main.fontManager.getStringWidth(setting.getMax() + " ") * 0.73f)) * sliderWidthValue() - 1, PanelScreen.animationFactor() * 100);
        if (lastFrameX != x){
            circleX = x + (Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f) + (width - (Main.fontManager.getStringWidth(setting.getMax() + " ") * 0.73f)) * sliderWidthValue() - 1;
        }
        RenderUtil.drawRoundedRect(x + (Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f) + 1, y + (height) / 2f - 1, circleX + 3, y + (height) / 2f + 1, 3, new Color(PanelScreen.PANEL.color.GetColor().getRed() / 255.0f, PanelScreen.PANEL.color.GetColor().getGreen() / 255.0f, PanelScreen.PANEL.color.GetColor().getBlue() / 255.0f, 0.15f));
        RenderUtil.drawRoundedRect(circleX - 3.5f, y + (height) / 2f - 3.5f, circleX + 3.5f, y + (height / 2f) + 3.5f, 6, PanelScreen.thirdBackgroundColor);
        RenderUtil.drawRoundedRect(circleX - 3, y + (height) / 2f - 3, circleX + 3, y + (height) / 2f + 3, 6, new Color(PanelScreen.PANEL.color.GetColor().getRed() / 255.0f, PanelScreen.PANEL.color.GetColor().getGreen() / 255.0f, PanelScreen.PANEL.color.GetColor().getBlue() / 255.0f, 0.5f));
        RenderUtil.prepareScale(0.73f);
        Main.fontManager.drawStringWithShadow(setting.GetSlider() + "", new Vector2D((circleX - (Main.fontManager.getStringWidth(setting.GetSlider() + "") / 2.0f) * 0.73f) / 0.73f, (y + (height / 2.0f) - (Main.fontManager.getFontHeight() / 0.73f)) / 0.73f), new Color(col[0], col[1], col[2], 1.0f).getRGB());
        Main.fontManager.drawStringWithShadow(setting.getName() + "   " + setting.getMin(), new Vector2D(x / 0.73f, (y + (height / 2f) - Main.fontManager.getFontHeight() / 2.0f) / 0.73f), new Color(col[0], col[1], col[2], 1.0f).getRGB());
        Main.fontManager.drawStringWithShadow(setting.getMax() + "", new Vector2D((x + this.width - (Main.fontManager.getStringWidth(setting.getMax() + "") * 0.73f)) / 0.73f, (y + (height / 2f) - Main.fontManager.getFontHeight() / 2.0f) / 0.73f), new Color(col[0], col[1], col[2], 1.0f).getRGB());
        RenderUtil.releaseScale();
        lastFrameX = x;
    }

    public void dragSlider(int mouseX, int mouseY) {
        if (isInsideExtended(mouseX, mouseY) && Mouse.isButtonDown(0)) {
            setSliderValue(mouseX);
            extension = 10;
        } else {
            extension = 0;
        }
    }

    public float sliderWidthValue() {
        return (setting.GetSlider() - setting.getMin()) / (setting.getMax() - setting.getMin());
    }

    protected void setSliderValue(int mouseX) {
        setting.setValue(setting.getMin());
        final float diff = Math.min(this.width - (Main.fontManager.getStringWidth(setting.getMax() + " ") * 0.73f) - (Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f), Math.max(0, mouseX - (x + (Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f))));
        final float min = setting.getMin();
        final float max = setting.getMax();
        final float val = roundNumber(diff / (this.width - (Main.fontManager.getStringWidth(setting.getMax() + " ") * 0.73f) - (Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f)) * (max - min) + min);
        setting.setValue(diff == 0 ? setting.getMin() : Math.min(val, setting.getMax()));

    }

    protected float roundNumber(double value) {
        BigDecimal decimal = BigDecimal.valueOf(value);
        decimal = decimal.setScale(1, RoundingMode.FLOOR);
        return decimal.floatValue();
    }


    protected boolean isInsideExtended(int mouseX, int mouseY) {
        return mouseX > x + (Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f) - extension && mouseX < x + (Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f) + ((width - Main.fontManager.getStringWidth(setting.getName() + "  " + setting.getMin() + " ") * 0.73f) - (Main.fontManager.getStringWidth(setting.getMax() + " ") * 0.73f)) + extension && mouseY > y && mouseY < y + height;
    }

    @Override
    public float getHeight() {
        return height;
    }
}
