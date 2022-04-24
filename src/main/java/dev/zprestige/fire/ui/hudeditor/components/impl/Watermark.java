package dev.zprestige.fire.ui.hudeditor.components.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.clickgui.ClickGui;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;

public class Watermark extends HudComponent {

    public Watermark() {
        super("Watermark", 0, 0, Main.fontManager.getStringWidth(Main.name + " " + Main.version), Main.fontManager.getFontHeight());
    }

    @Override
    public void render() {
        final String name = Main.name + " ";
        final String version = Main.version;
        Main.fontManager.drawStringWithShadow(name, x, y, ((ClickGui) Main.moduleManager.getModuleByClass(ClickGui.class)).color.GetColor().getRGB());
        Main.fontManager.drawStringWithShadow(version, x + Main.fontManager.getStringWidth(name), y, -1);
    }
}
