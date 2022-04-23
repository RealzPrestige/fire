package dev.zprestige.fire.ui.hudeditor;


import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.clickgui.ClickGui;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;

import java.awt.*;

public class HudComponentScreen {
    protected final HudComponent hudComponent;
    protected Vector2D position, size;

    public HudComponentScreen(HudComponent hudComponent, Vector2D position, Vector2D size) {
        this.hudComponent = hudComponent;
        this.position = position;
        this.size = size;
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
            RenderUtil.drawRect(position, new Vector2D(position.getX() + size.getX(), position.getY() + size.getY()), ClickGui.Instance.color.GetColor().getRGB());
        }
        RenderUtil.drawOutline(position.getX(), position.getY(), position.getX() + size.getX(), position.getY() + size.getY(), new Color(0, 0, 0, 30), 1f);
        if (inside(mouseX, mouseY)) {
            RenderUtil.drawRect(position, new Vector2D(position.getX() + size.getX(), position.getY() + size.getY()), new Color(0, 0, 0, 30).getRGB());
        }
        Main.fontManager.drawStringWithShadow(hudComponent.getName(), new Vector2D(position.getX() + 2, position.getY() + size.getY() / 2f - Main.fontManager.getFontHeight() / 2f), -1);
    }

    protected boolean inside(int mouseX, int mouseY) {
        return mouseX > position.getX() && mouseX < position.getX() + size.getX() && mouseY > position.getY() && mouseY < position.getY() + size.getY();
    }

    protected boolean check() {
        return hudComponent.isEnabled();
    }

    public void setX(float x) {
        this.position = new Vector2D(x, position.getY());
    }

    public void setY(float y) {
        this.position = new Vector2D(position.getX(), y);
    }
}
