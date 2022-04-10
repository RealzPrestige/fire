package dev.zprestige.fire.ui.menu.panel;

import dev.zprestige.fire.settings.Setting;
import dev.zprestige.fire.settings.impl.Switch;

import java.util.stream.IntStream;

public class PanelSetting {
    protected Setting<?> setting;
    protected float x, y, width, height, secondY;
    protected float[] col = new float[]{0.9f, 0.9f, 0.9f};

    public PanelSetting(final Setting<?> setting, final float x, final float y, final float width, final float height) {
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.secondY = setting.isVisible() ? 0.0f : height;
    }

    public void render(final int mouseX, final int mouseY) {
        secondY = normalizeNumber(secondY, setting.isVisible() ? 0 : height, PanelScreen.animationFactor() * 10);
        hoverColor(mouseX, mouseY, PanelScreen.animationFactor() * 10);
    }

    public void click(final int mouseX, final int mouseY, final int state) {

    }

    public void release(final int mouseX, final int mouseY, final int state) {

    }

    public void type(char typedChar, int keyCode) {

    }

    protected boolean inside(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getHeight() {
        return secondY;
    }

    public Setting<?> getSetting() {
        return setting;
    }

    public boolean enabledSwitch() {
        return setting instanceof Switch && ((Switch) setting).GetSwitch();
    }

    public boolean isVisible(){
        return setting.isVisible();
    }

    protected void hoverColor(final int mouseX, final int mouseY, final float animationFactor) {
        if (inside(mouseX, mouseY)) {
            IntStream.range(0, 3).forEach(i -> col[i] = normalizeNumber(col[i], 0.6f, animationFactor));
        } else {
            IntStream.range(0, 3).forEach(i -> col[i] = normalizeNumber(col[i], enabledSwitch() ? 1.0f : 0.7f, animationFactor));
        }
    }

    protected float normalizeNumber(final float input, final float target, final float factor) {
        if (input > target) {
            return input - ((input - target) * factor) / 10;
        }
        if (input < target) {
            return input + ((target - input) * factor) / 10;
        }
        return input;
    }
}
