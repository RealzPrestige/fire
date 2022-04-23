package dev.zprestige.fire.ui.hudeditor.components.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.clickgui.ClickGui;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import dev.zprestige.fire.util.impl.Vector2D;

public class Watermark extends HudComponent {

    public Watermark() {
        super("Watermark", new Vector2D(0, 0), new Vector2D(Main.fontManager.getStringWidth(Main.name + " " + Main.version), Main.fontManager.getFontHeight()));
    }

    @Override
    public void render() {
        final String name = Main.name+ " ";
        final String version = Main.version;
        Main.fontManager.drawStringWithShadow(name, position, ClickGui.Instance.color.GetColor().getRGB());
        Main.fontManager.drawStringWithShadow(version, new Vector2D(position.getX() + Main.fontManager.getStringWidth(name), position.getY()), -1);
    }
}
