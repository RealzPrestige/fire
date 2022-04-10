package dev.zprestige.fire.ui.menu.panel;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.settings.Setting;
import dev.zprestige.fire.settings.impl.*;
import dev.zprestige.fire.ui.menu.panel.impl.*;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;

import java.awt.*;
import java.util.ArrayList;

public class PanelInner {
    protected final PanelModule panelModule;
    protected final ArrayList<PanelSetting> panelSettings = new ArrayList<>();
    protected String name;
    protected boolean adjustedX;
    protected float x, y, width, height, scroll;

    public PanelInner(final PanelModule panelModule, final String name, final float x, final float y, final float width, final float height) {
        this.panelModule = panelModule;
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        for (final Setting<?> setting : panelModule.module.getSettings()) {
            if (setting.getPanel().equals(name)) {
                if (setting instanceof Switch) {
                    panelSettings.add(new PanelSwitch((Switch) setting, x, y, width - 6, 15));
                }
                if (setting instanceof Slider) {
                    panelSettings.add(new PanelSlider((Slider) setting, x, y, width - 6, 15));
                }
                if (setting instanceof Key) {
                    panelSettings.add(new PanelKey((Key) setting, x, y, width - 6, 15));
                }
                if (setting instanceof ComboBox) {
                    panelSettings.add(new PanelCombo((ComboBox) setting, x, y, width - 6, 15));
                }
                if (setting instanceof ColorBox) {
                    panelSettings.add(new PanelColor((ColorBox) setting, x, y, width - 6, 15));
                }
            }
        }
        this.adjustedX = false;
    }

    public void render(final int mouseX, final int mouseY) {
        float y = this.y + (10 - panelModule.a * 10) + scroll;
        for (int i = 20; i <= 28; i = i + 2) {
            final float val = ((28 - i) / 2f);
            RenderUtil.drawRoundedRect(x, y, x + width + val, y + height + val, 10, new Color(0, 0, 0, i));
        }
        RenderUtil.drawRoundedRect(x, y, x + width, y + height, 10, PanelScreen.thirdBackgroundColor);
        RenderUtil.drawRoundedRect(x + 1, y + 1, x + width - 1, y + height - 1, 10, PanelScreen.secondBackgroundColor);
        Main.fontManager.drawStringWithShadow(name, new Vector2D(x + (width / 2f) - (Main.fontManager.getStringWidth(name) / 2.0f), y + 9.0f - (Main.fontManager.getFontHeight() / 2.0f)), -1);
        RenderUtil.drawRoundedRect(x + 3, y + 16, x + width - 3, y + 17, 2, PanelScreen.thirdBackgroundColor);
        final float minY = PanelScreen.y + PanelScreen.secondStartY;
        final float maxY = PanelScreen.y + PanelScreen.secondStartY + PanelScreen.height - PanelScreen.secondStartY - PanelScreen.secondEndY - 2.0f;
        float deltaY = y + 18;
        for (final PanelSetting panelSetting : panelSettings) {
            if (!PanelScreen.searchingString.equals("") && !panelSetting.setting.getName().toLowerCase().contains(PanelScreen.searchingString)) {
                continue;
            }
            boolean scissored = false;
            panelSetting.setY(deltaY);
            panelSetting.setX(x + 3);
            if (panelSetting.y < PanelScreen.y + PanelScreen.height - PanelScreen.secondEndY && panelSetting.y + panelSetting.height > PanelScreen.y + PanelScreen.secondStartY) {
                if ((panelSetting.isVisible() && deltaY + panelSetting.getHeight() - panelSetting.secondY < maxY && deltaY > minY) || (!panelSetting.isVisible() && (deltaY - panelSetting.secondY < maxY && deltaY > minY))) {
                    RenderUtil.prepareScissor((int) panelSetting.x - 1, (int) deltaY, (int) panelSetting.width + 1, (int) (deltaY + panelSetting.getHeight()));
                    scissored = true;
                }
                panelSetting.render(mouseX, mouseY);
                if (scissored) {
                    RenderUtil.releaseScissor();
                }
            }
            deltaY += panelSetting.getHeight() - panelSetting.secondY;
        }
        this.height = deltaY - y + 3;
    }

    public void click(final int mouseX, final int mouseY, final int state) {
        panelSettings.stream().filter(PanelSetting::isVisible).forEach(panelSetting -> panelSetting.click(mouseX, mouseY, state));
    }

    public void release(final int mouseX, final int mouseY, final int state) {
        panelSettings.stream().filter(PanelSetting::isVisible).forEach(panelSetting -> panelSetting.release(mouseX, mouseY, state));
    }

    public void type(char typedChar, int keyCode) {
        panelSettings.stream().filter(PanelSetting::isVisible).forEach(panelSetting -> panelSetting.type(typedChar, keyCode));
    }

}
