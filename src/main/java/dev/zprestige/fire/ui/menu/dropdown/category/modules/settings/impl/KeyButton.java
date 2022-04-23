package dev.zprestige.fire.ui.menu.dropdown.category.modules.settings.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.settings.impl.Key;
import dev.zprestige.fire.ui.menu.dropdown.category.modules.settings.AbstractSetting;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Timer;
import dev.zprestige.fire.util.impl.Vector2D;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class KeyButton extends AbstractSetting {
    protected final Key setting;
    protected boolean typing;
    protected final Timer timer = new Timer();

    public KeyButton(Key setting, Vector2D size) {
        super(setting, size);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        RenderUtil.drawOutline(position.getX(), position.getY(), position.getX() + size.getX(), position.getY() + size.getY(), new Color(0, 0, 0, 30), 1.0f);
        final float y = position.getY() + size.getY() / 2 - Main.fontManager.getFontHeight() / 2;
        final String text = typing ? " " + getTypingIcon() : setting.GetKey() == -1 ? "None" : Keyboard.getKeyName(setting.GetKey());
        Main.fontManager.drawStringWithShadow(setting.getName(), new Vector2D(position.getX() + 2, y), -1);
        if (setting.isHold()) {
            RenderUtil.prepareScale(0.6);
            Main.fontManager.drawStringWithShadow("(hold)", new Vector2D((position.getX() + Main.fontManager.getStringWidth(setting.getName()) + 4) / 0.6f, (position.getY() + size.getY() / 2) / 0.6f), Color.GRAY.getRGB());
            RenderUtil.releaseScale();
        }
        Main.fontManager.drawStringWithShadow(text, new Vector2D(position.getX() + size.getX() - Main.fontManager.getStringWidth(text) - 2, y), Color.GRAY.getRGB());
        if (inside(mouseX, mouseY)) {
            RenderUtil.drawRect(position, new Vector2D(position.getX() + size.getX(), position.getY() + size.getY()), new Color(0, 0, 0, 50).getRGB());
        }
    }

    @Override
    public void click(int mouseX, int mouseY, int state) {
        if (inside(mouseX, mouseY) && state == 0) {
            typing = !typing;
        }
        if (inside(mouseX, mouseY) && state == 1) {
            setting.setHold(!setting.isHold());
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int state) {

    }

    @Override
    public void type(char typedChar, int keyCode) {
        if (typing) {
            setting.setValue(keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_ESCAPE ? -1 : keyCode);
            typing = !typing;
        }
    }

    @Override
    public float getHeight() {
        return size.getY();
    }

    public String getTypingIcon() {
        if (timer.getTime(1000)) {
            timer.syncTime();
            return "";
        }
        if (timer.getTime(500)) {
            return "_";
        }
        return "";
    }
}
