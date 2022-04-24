package dev.zprestige.fire.ui.hudeditor;


import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.clickgui.ClickGui;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import dev.zprestige.fire.util.impl.RenderUtil;

import java.awt.*;

public class HudComponentScreen {
    protected final HudComponent hudComponent;
    protected float x, y, width, height;

    public HudComponentScreen(HudComponent hudComponent, float x, float y, float width, float height) {
        this.hudComponent = hudComponent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void release(int button) {
        if (check()) {
            hudComponent.release(button);
        }
    }

    public void click(int mouseX, int mouseY, int button) {
        if (check()) {
            hudComponent.click(mouseX, mouseY, button);
        }
        if (inside(mouseX, mouseY) && button == 0) {
            hudComponent.setEnabled(!hudComponent.isEnabled());
        }
    }

    public void draw(int mouseX, int mouseY) {
        if (check()) {
            hudComponent.update(mouseX, mouseY);
        }
        if (hudComponent.isEnabled()) {
            RenderUtil.drawRect(x, y,x + width, y + height, ((ClickGui) Main.moduleManager.getModuleByClass(ClickGui.class)).color.GetColor().getRGB());
        }
        RenderUtil.drawOutline(x, y, x + width, y + height, new Color(0, 0, 0, 30), 1f);
        if (inside(mouseX, mouseY)) {
            RenderUtil.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 30).getRGB());
        }
        Main.fontManager.drawStringWithShadow(hudComponent.getName(),x + 2, y + height / 2f - Main.fontManager.getFontHeight() / 2f, -1);
    }

    protected boolean inside(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY >y && mouseY < y + height;
    }

    protected boolean check() {
        return hudComponent.isEnabled();
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
