package dev.zprestige.fire.ui.menu.dropdown.category.modules.settings.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.ui.menu.dropdown.category.modules.settings.AbstractSetting;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;

import java.awt.*;
import java.util.stream.IntStream;

public class ComboBoxButton extends AbstractSetting {
    protected final ComboBox setting;

    public ComboBoxButton(ComboBox setting, Vector2D size) {
        super(setting, size);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        RenderUtil.drawOutline(position.getX(), position.getY(), position.getX() + size.getX(), position.getY() + size.getY(), new Color(0, 0, 0, 30), 1.0f);
        final float y = position.getY() + size.getY() / 2 - Main.fontManager.getFontHeight() / 2;
        Main.fontManager.drawStringWithShadow(setting.getName(), new Vector2D(position.getX() + 2, y), -1);
        final String text = setting.GetCombo();
        Main.fontManager.drawStringWithShadow(text, new Vector2D(position.getX() + size.getX() - Main.fontManager.getStringWidth(text) - 2, y), Color.GRAY.getRGB());
    }

    public int getIndex() {
        return IntStream.range(0, setting.getValues().length).filter(i -> setting.getValues()[i].equals(setting.GetCombo())).findFirst().orElse(-1);
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
    public void release(int mouseX, int mouseY, int state) {

    }

    @Override
    public void type(char typedChar, int keyCode) {

    }

    @Override
    public float getHeight() {
        return size.getY();
    }
}
