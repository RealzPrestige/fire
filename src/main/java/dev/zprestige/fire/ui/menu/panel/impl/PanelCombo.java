package dev.zprestige.fire.ui.menu.panel.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.settings.Setting;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.ui.menu.panel.PanelSetting;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;

import java.awt.*;
import java.util.stream.IntStream;

public class PanelCombo extends PanelSetting {
    protected final ComboBox setting;

    public PanelCombo(final ComboBox setting, final float x, final float y, final float width, final float height) {
        super(setting, x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
        final float y = this.y - secondY;
        RenderUtil.prepareScale(0.73f);
        Main.fontManager.drawStringWithShadow(setting.getName(), new Vector2D(x / 0.73f, (y + (height / 2f) - Main.fontManager.getFontHeight() / 2.0f) / 0.73f), new Color(col[0], col[1], col[2], 1.0f).getRGB());
        Main.fontManager.drawStringWithShadow(setting.GetCombo(), new Vector2D((x + width - Main.fontManager.getStringWidth(setting.GetCombo()) * 0.73f) / 0.73f, (y + (height / 2f) - Main.fontManager.getFontHeight() / 2.0f) / 0.73f), new Color(col[0], col[1], col[2], 0.5f).getRGB());
        RenderUtil.releaseScale();
    }

    @Override
    public void click(int mouseX, int mouseY, int state) {
        if (inside(mouseX, mouseY)) {
            final int max = setting.getValues().length;
            final int index = getIndex();
            switch (state) {
                case 0:
                    setting.setValue(setting.getValues()[index + 1 >= max ? 0 : index + 1]);
                    break;
                case 1:
                    setting.setValue(setting.getValues()[index - 1 < 0 ? max - 1 : index - 1]);
                    break;
            }
        }
    }

    @Override
    public float getHeight() {
        return height;
    }

    protected int getIndex() {
        return IntStream.range(0, setting.getValues().length).filter(i -> setting.getValues()[i].equals(setting.GetCombo())).findFirst().orElse(-1);
    }
}
