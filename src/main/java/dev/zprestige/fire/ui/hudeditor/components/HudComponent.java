package dev.zprestige.fire.ui.hudeditor.components;


import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.clickgui.ClickGui;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class HudComponent {
    protected final Minecraft mc = Main.mc;
    protected String name;
    protected boolean enabled, dragging;
    protected float x, y, width, height;
    protected float dragX, dragY;

    public HudComponent(String name, float x, float y, float width, float height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.enabled = false;
    }

    protected void drag(int mouseX, int mouseY) {
        x = dragX + mouseX;
        y = dragY + mouseY;
    }

    public void release(int button) {
        if (button == 0) {
            dragging = false;
        }
    }

    public void click(int mouseX, int mouseY, int button) {
        if (inside(mouseX, mouseY) && button == 0) {
            dragX = x - mouseX;
            dragY = y - mouseY;
            dragging = true;
        }
    }

    public void update(int mouseX, int mouseY) {
        if (dragging) {
            drag(mouseX, mouseY);
        }
        if (inside(mouseX, mouseY)) {
            RenderUtil.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 30).getRGB());
        }
        RenderUtil.drawOutline(x, y, x + width, y + height, ((ClickGui) Main.moduleManager.getModuleByClass(ClickGui.class)).color.GetColor(), 1.0f);
    }

    public void render() {
    }

    protected boolean inside(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY >y && mouseY < y + height;
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
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
