package dev.zprestige.fire.ui.menu.panel.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.settings.Setting;
import dev.zprestige.fire.settings.impl.Key;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.ui.menu.panel.PanelScreen;
import dev.zprestige.fire.ui.menu.panel.PanelSetting;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Timer;
import dev.zprestige.fire.util.impl.Vector2D;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class PanelKey extends PanelSetting {
    protected final Timer timer = new Timer();
    protected final Key setting;
    protected boolean typing;

    public PanelKey(final Key setting, final float x, final float y, final float width, final float height) {
        super(setting, x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        final float y = this.y - secondY;
        for (int i = 20; i <= 27; i = i + 2) {
            final float val = ((27 - i) / 2f);
            RenderUtil.drawRoundedRect(x + width - 38, y + 2, x + width - 4 + val, y + height - 2 + val, 7, new Color(0, 0, 0, i));
        }
        RenderUtil.drawRoundedRect(x + width - 38, y + 2, x + width - 4, y + height - 2, 7, PanelScreen.thirdBackgroundColor);
        RenderUtil.drawRoundedRect(x + width - 37, y + 3, x + width - 5, y + height - 3, 6, PanelScreen.secondBackgroundColor);
        RenderUtil.prepareScale(0.73f);
        Main.fontManager.drawStringWithShadow(setting.getName(), new Vector2D(x / 0.73f, (y + (height / 2f) - Main.fontManager.getFontHeight() / 2.0f) / 0.73f), new Color(col[0], col[1], col[2], 1.0f).getRGB());
        if (setting.isHold()) {
            Main.fontManager.drawStringWithShadow("(hold)", new Vector2D((x + Main.fontManager.getStringWidth(setting.getName()) * 0.73f) / 0.73f, (y + (height / 2f) - Main.fontManager.getFontHeight() / 2.0f) / 0.73f), new Color(col[0], col[1], col[2], 0.3f).getRGB());
        }
        final String string = typing ? getDots() : setting.getValue() == -1 ? "NONE" : Keyboard.getKeyName(setting.getValue());
        Main.fontManager.drawStringWithShadow(string, new Vector2D((x + width - 21 - (typing ? Main.fontManager.getStringWidth("...") * 0.73f : Main.fontManager.getStringWidth(string) * 0.73f) / 2.0f) / 0.73f, (y + (height / 2f) - Main.fontManager.getFontHeight() / 2.0f) / 0.73f), new Color(col[0], col[1], col[2], 1.0f).getRGB());
        RenderUtil.releaseScale();
    }

    @Override
    public void click(int mouseX, int mouseY, int state) {
        if (insideBox(mouseX, mouseY)) {
            switch (state) {
                case 0:
                    typing = !typing;
                    break;
                case 1:
                    setting.setHold(!setting.isHold());
                    break;
            }
        }
    }

    @Override
    public void type(char typedChar, int keyCode) {
        if (typing) {
            setting.setValue(keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_ESCAPE ? -1 : keyCode);
            typing = !typing;
        }
    }

    protected String getDots() {
        if (timer.getTime(1500)) {
            timer.syncTime();
        }
        if (timer.getTime(1000)) {
            return "...";
        }
        if (timer.getTime(500)) {
            return "..";
        }
        return ".";
    }

    protected boolean insideBox(int mouseX, int mouseY) {
        return mouseX > x + width - 38 && mouseX < x + width - 4 && mouseY > y + 2 && mouseY < y + height - 2;
    }

    @Override
    public float getHeight() {
        return height;
    }
}
