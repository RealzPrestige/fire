package dev.zprestige.fire.ui.hudeditor.components;


import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.ClickGui;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class HudComponent {
    protected final Minecraft mc = Main.mc;
    protected String name;
    protected boolean enabled, dragging;
    protected Vector2D position, size;
    protected float dragX, dragY;

    public HudComponent(String name, Vector2D position, Vector2D size) {
        this.name = name;
        this.position = position;
        this.size = size;
        this.enabled = false;
    }

    protected void drag(int mouseX, int mouseY) {
        position = new Vector2D(dragX + mouseX, dragY + mouseY);
    }

    public void release(int button) {
        if (button == 0) {
            dragging = false;
        }
    }

    public void click(int mouseX, int mouseY, int button) {
        if (inside(mouseX, mouseY) && button == 0) {
            dragX = position.getX() - mouseX;
            dragY = position.getY() - mouseY;
            dragging = true;
        }
    }

    public void update(int mouseX, int mouseY) {
        if (dragging) {
            drag(mouseX, mouseY);
        }
        if (inside(mouseX, mouseY)) {
            RenderUtil.drawRect(position, new Vector2D(position.getX() + size.getX(), position.getY() + size.getY()), new Color(0, 0, 0, 30).getRGB());
        }
        RenderUtil.drawOutline(position.getX(), position.getY(), position.getX() + size.getX(), position.getY() + size.getY(), ClickGui.Instance.color.GetColor(), 1.0f);
    }

    public void render() {
    }

    protected boolean inside(int mouseX, int mouseY) {
        return mouseX > position.getX() && mouseX < position.getX() + size.getX() && mouseY > position.getY() && mouseY < position.getY() + size.getY();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setWidth(float width) {
        this.size = new Vector2D(width, size.getY());
    }

    public void setHeight(float height) {
        this.size = new Vector2D(size.getX(), height);
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }
}
