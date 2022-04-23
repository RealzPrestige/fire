package dev.zprestige.fire.ui.menu.panel;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.Category;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class PanelCategory {
    protected final ArrayList<PanelModule> panelModules = new ArrayList<>();
    protected final Category category;
    protected final float offset = 131.6666666666667f;
    protected float[] col;
    protected float x, y, width, height, alpha;

    public PanelCategory(final Category category, final float x, final float y, final float width, final float height) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setupPanelModules();
        col = new float[]{1.0f, 1.0f, 1.0f};
    }

    public void render(int mouseX, int mouseY) {
        final float animationFactor = PanelScreen.animationFactor();
        hoverColor(mouseX, mouseY, animationFactor);
        final Color color = new Color(Math.max(0.0f, Math.min(1.0f, col[0])), Math.max(0.0f, Math.min(1.0f, col[1])), Math.max(0.0f, Math.min(1.0f, col[2])), 1.0f);
        RenderUtil.image(new ResourceLocation("textures/images/" + category.toString() + ".png"), (int) (x + 5.0f), (int) y + 3, 9, 9, color);
        Main.fontManager.drawStringWithShadow(category.toString(), new Vector2D(x + 20, y + (height / 2f) - (Main.fontManager.getFontHeight() / 2f)), color.getRGB());
        alpha = normalizeNumber(alpha, PanelScreen.activeCategory != null && PanelScreen.activeCategory.equals(this) ? 1.0f : 0.0f, animationFactor * 10);
        final float startX = PanelScreen.x + PanelScreen.secondStart + 5;
        float deltaX = startX;
        float deltaY = PanelScreen.y + PanelScreen.secondStartY + 5;
        for (final PanelModule panelModule : panelModules) {
            if (PanelScreen.activeModule == null && !panelModule.module.getName().toLowerCase().contains(PanelScreen.searchingString.toLowerCase())) {
                continue;
            }
            panelModule.x = deltaX;
            panelModule.y = deltaY;
            deltaX += offset + 5;
            if (deltaX >= startX + (offset + 5) * 3) {
                deltaY += 35;
                deltaX = startX;
            }
            panelModule.render(mouseX, mouseY);
        }
    }

    public void click(int mouseX, int mouseY, int state) {
        if (state == 0 && inside(mouseX, mouseY)) {
            PanelScreen.activeModule = null;
            PanelScreen.panelDrawable = null;
            PanelScreen.activeCategory = this;
        }
        panelModules.forEach(panelModule -> panelModule.click(mouseX, mouseY, state));
    }

    public void release(int mouseX, int mouseY, int state) {
        panelModules.forEach(panelModule -> panelModule.release(mouseX, mouseY, state));
    }

    public void type(char typedChar, int keyCode) {
        panelModules.forEach(panelModule -> panelModule.type(typedChar, keyCode));
    }

    protected void setupPanelModules() {
        panelModules.clear();
        final float startX = PanelScreen.x + PanelScreen.secondStart + 5;
        float deltaX = startX;
        float deltaY = PanelScreen.y + PanelScreen.secondStartY + 5;
        for (Module module : Main.moduleManager.getModulesInCategory(category)) {
            panelModules.add(new PanelModule(this, module, deltaX, deltaY, offset, 30));
            deltaX += offset + 5;
            if (deltaX >= startX + (offset + 5) * 3) {
                deltaY += 35;
                deltaX = startX;
            }
        }
    }

    protected void hoverColor(final int mouseX, final int mouseY, final float animationFactor) {
        if (PanelScreen.activeCategory != null && PanelScreen.activeCategory.equals(this)) {
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

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
