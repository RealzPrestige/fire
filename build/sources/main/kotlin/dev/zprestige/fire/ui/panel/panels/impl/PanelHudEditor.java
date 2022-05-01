package dev.zprestige.fire.ui.panel.panels.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.ui.panel.PanelScreen;
import dev.zprestige.fire.ui.panel.panels.PanelDrawable;
import dev.zprestige.fire.util.impl.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class PanelHudEditor extends PanelDrawable {
    protected final ArrayList<HudComponent> hudComponents = new ArrayList<>();
    protected final float offset = 131.6666666666667f, componentHeight = 30.0f, delta = 35.0f;
    protected boolean clickingFrame, releasingFrame;

    public PanelHudEditor(final float x, final float y, final float width, final float height) {
        super("HudEditor", width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setupPanelModules();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
        if (selected()) {
            final float startX = PanelScreen.x + PanelScreen.secondStart + 5;
            float deltaX = startX;
            float deltaY = PanelScreen.y + PanelScreen.secondStartY + 5;
            for (final HudComponent hudComponent : hudComponents) {
                hudComponent.x = deltaX;
                hudComponent.y = deltaY;
                deltaX += offset + 5;
                if (deltaX >= startX + (offset * 3)) {
                    deltaY += delta;
                    deltaX = startX;
                }
            }
            hudComponents.forEach(hudComponent -> hudComponent.render(mouseX, mouseY, clickingFrame, releasingFrame));
            if (clickingFrame) {
                hudComponents.forEach(hudComponent -> hudComponent.click(mouseX, mouseY));
            }
            clickingFrame = false;
            releasingFrame = false;
        }
    }

    @Override
    public void click(int mouseX, int mouseY, int state) {
        super.click(mouseX, mouseY, state);
        if (selected() && state == 0) {
            clickingFrame = true;
        }
    }

    @Override
    public void release(int state) {
        if (selected() && state == 0) {
            releasingFrame = true;
        }
    }

    protected void setupPanelModules() {
        hudComponents.clear();
        final float startX = PanelScreen.x + PanelScreen.secondStart + 5;
        float deltaX = startX;
        float deltaY = PanelScreen.y + PanelScreen.secondStartY + 5;
        for (final dev.zprestige.fire.ui.hudeditor.components.HudComponent hudComponent : Main.hudManager.getHudComponents()) {
            hudComponents.add(new HudComponent(hudComponent, deltaX, deltaY));
            deltaX += offset + 5;
            if (deltaX >= startX + (offset * 3)) {
                deltaY += delta;
                deltaX = startX;
            }
        }
    }

    protected class HudComponent {
        protected float[] col = new float[]{1.0f, 1.0f, 1.0f};
        protected final dev.zprestige.fire.ui.hudeditor.components.HudComponent hudComponent;
        protected float x, y;

        public HudComponent(final dev.zprestige.fire.ui.hudeditor.components.HudComponent hudComponent, final float x, final float y) {
            this.hudComponent = hudComponent;
            this.x = x;
            this.y = y;
        }

        public void render(int mouseX, int mouseY, boolean clickingFrame, boolean releaseFrame) {
            for (int i = 20; i <= 28; i = i + 2) {
                final float val = ((28 - i) / 2f);
                RenderUtil.drawRoundedRect(x, y, x + offset + val, y + componentHeight + val, 10, new Color(0, 0, 0, i));
            }
            RenderUtil.drawRoundedRect(x, y, x + offset, y + componentHeight, 10, PanelScreen.thirdBackgroundColor);
            RenderUtil.drawRoundedRect(x + 1, y + 1, x + offset - 1, y + componentHeight - 1, 10, PanelScreen.secondBackgroundColor);
            Main.fontManager.drawStringWithShadow(hudComponent.getName(), x + 4, y + 2 + Main.fontManager.getFontHeight() / 2.0f, new Color(col[0], col[1], col[2], 1.0f).getRGB());
            RenderUtil.prepareScale(0.75f);
            final String description = "Draws the " + hudComponent.getName() + " component on the screen";
            float delta = 0.0f;
            float deltaY1 = 0.0f;
            for (String string : description.split(" ")) {
                final float width = Main.fontManager.getStringWidth(string + " ");
                if (delta + width > offset) {
                    delta = 0.0f;
                    deltaY1 += Main.fontManager.getFontHeight() + 0.75f;
                }
                Main.fontManager.drawStringWithShadow(string, (x + 4 + delta) / 0.75f, (y + 5 + Main.fontManager.getFontHeight() * 1.5f + deltaY1) / 0.75f, new Color(1.0f, 1.0f, 1.0f, inside(mouseX, mouseY) ? 0.4f : 0.5f).getRGB());
                delta += width * 0.75f;
            }
            RenderUtil.releaseScale();
            hoverColor(mouseX, mouseY, PanelScreen.animationFactor() * 10);
            if (hudComponent.isEnabled()) {
                hudComponent.update(mouseX, mouseY);
            }
            if (clickingFrame) {
                hudComponent.click(mouseX, mouseY, 0);
            }
            if (releaseFrame) {
                hudComponent.release(0);
            }
        }

        public void click(int mouseX, int mouseY) {
            if (inside(mouseX, mouseY)) {
                hudComponent.setEnabled(!hudComponent.isEnabled());
            }
        }

        protected boolean inside(int mouseX, int mouseY) {
            return mouseX > x && mouseX < x + offset && mouseY > y && mouseY < y + componentHeight;
        }

        protected void hoverColor(final int mouseX, final int mouseY, final float animationFactor) {
            if (hudComponent.isEnabled()) {
                final Color color = PanelScreen.PANEL.color.GetColor();
                if (inside(mouseX, mouseY)) {
                    col[0] = normalizeNumber(col[0], color.getRed() / (255.0f * 1.25f), animationFactor);
                    col[1] = normalizeNumber(col[1], color.getGreen() / (255.0f * 1.25f), animationFactor);
                    col[2] = normalizeNumber(col[2], color.getBlue() / (255.0f * 1.25f), animationFactor);

                } else {
                    col[0] = normalizeNumber(col[0], color.getRed() / 255.0f, animationFactor);
                    col[1] = normalizeNumber(col[1], color.getGreen() / 255.0f, animationFactor);
                    col[2] = normalizeNumber(col[2], color.getBlue() / 255.0f, animationFactor);
                }
            } else {
                if (inside(mouseX, mouseY)) {
                    IntStream.range(0, 3).forEach(i -> col[i] = normalizeNumber(col[i], 0.6f, animationFactor));
                } else {
                    IntStream.range(0, 3).forEach(i -> col[i] = normalizeNumber(col[i], 1.0f, animationFactor));
                }
            }
        }
    }
}
