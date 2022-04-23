package dev.zprestige.fire.ui.menu.panel.panels;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.ui.menu.panel.PanelScreen;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.stream.IntStream;

public class PanelDrawable {
    protected float[] col = new float[]{1.0f, 1.0f, 1.0f};
    protected float x, y, width, height;
    protected Color color;
    protected final String string;

    public PanelDrawable(final String string, final float width, final float height) {
        this.string = string;
        this.width = width;
        this.height = height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public void render(int mouseX, int mouseY) {
        hoverColor(mouseX, mouseY, PanelScreen.animationFactor());
        color = new Color(fixColor(col[0]), fixColor(col[1]), fixColor(col[2]), 1.0f);
        RenderUtil.image(new ResourceLocation("textures/images/" + string.toLowerCase() + ".png"), (int) (x + 5.0f), (int) y + 3, 9, 9, color);
        Main.fontManager.drawStringWithShadow(string, new Vector2D(x + 20, y + (height / 2f) - (Main.fontManager.getFontHeight() / 2f)), color.getRGB());
    }

    protected float fixColor(final float i) {
        return Math.max(0.0f, Math.min(1.0f, i));
    }

    public void click(int mouseX, int mouseY, int state) {
        if (inside(mouseX, mouseY)) {
            PanelScreen.activeModule = null;
            PanelScreen.activeCategory = null;
            PanelScreen.panelDrawable = this;
        }
    }

    public void type(char typedChar, int keyCode) {
    }

    public void release(int state) {
    }

    protected void hoverColor(final int mouseX, final int mouseY, final float animationFactor) {
        if (PanelScreen.panelDrawable != null && PanelScreen.panelDrawable.equals(this)) {
            final Color color = PanelScreen.PANEL.color.GetColor();
            if (inside(mouseX, mouseY)) {
                col[0] = normalizeNumber(col[0], color.getRed() / (255.0f * 2f), animationFactor);
                col[1] = normalizeNumber(col[1], color.getGreen() / (255.0f * 2f), animationFactor);
                col[2] = normalizeNumber(col[2], color.getBlue() / (255.0f * 2f), animationFactor);
            } else {
                col[0] = normalizeNumber(col[0], color.getRed() / 255.0f, animationFactor);
                col[1] = normalizeNumber(col[1], color.getGreen() / 255.0f, animationFactor);
                col[2] = normalizeNumber(col[2], color.getBlue() / 255.0f, animationFactor);
            }
        } else {
            if (inside(mouseX, mouseY)) {
                IntStream.range(0, 3).forEach(i -> col[i] = normalizeNumber(col[i], 0.5f, animationFactor * 10));
            } else {
                IntStream.range(0, 3).forEach(i -> col[i] = normalizeNumber(col[i], 1.0f, animationFactor * 10));
            }
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

    protected boolean inside(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    protected boolean selected() {
        return PanelScreen.panelDrawable != null && PanelScreen.panelDrawable.equals(this);
    }
}
