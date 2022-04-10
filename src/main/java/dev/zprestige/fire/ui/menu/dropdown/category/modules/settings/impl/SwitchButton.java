package dev.zprestige.fire.ui.menu.dropdown.category.modules.settings.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.ClickGui;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.ui.menu.dropdown.category.modules.settings.AbstractSetting;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;

import java.awt.*;

public class SwitchButton extends AbstractSetting {
    protected final Switch setting;

    public SwitchButton(Switch setting, Vector2D size) {
        super(setting, size);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (setting.GetSwitch()) {
            RenderUtil.drawRect(position, new Vector2D(position.getX() + size.getX(), position.getY() + size.getY()), ClickGui.Instance.color.GetColor().getRGB());
        }
        RenderUtil.drawOutline(position.getX(), position.getY(), position.getX() + size.getX(), position.getY() + size.getY(), new Color(0, 0, 0, 30), 1.0f);
        Main.fontManager.drawStringWithShadow(setting.getName(), new Vector2D(position.getX() + 2, position.getY() + size.getY() / 2 - Main.fontManager.getFontHeight() / 2), -1);
        if (inside(mouseX, mouseY)) {
            RenderUtil.drawRect(position, new Vector2D(position.getX() + size.getX(), position.getY() + size.getY()), new Color(0, 0, 0, 50).getRGB());
        }
    }

    @Override
    public void click(int mouseX, int mouseY, int state) {
        if (inside(mouseX, mouseY) && state == 0) {
            setting.setValue(!setting.GetSwitch());
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

