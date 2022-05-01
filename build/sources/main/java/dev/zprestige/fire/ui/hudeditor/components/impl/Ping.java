package dev.zprestige.fire.ui.hudeditor.components.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.clickgui.ClickGui;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;

import java.util.Objects;

public class Ping extends HudComponent {

    public Ping() {
        super("Ping", 0, 10, Main.fontManager.getStringWidth("Ping 00ms"), Main.fontManager.getFontHeight());
    }

    @Override
    public void render() {
        try {
            final String ping = "Ping ";
            final String ms = Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.getConnection().getGameProfile().getId()).getResponseTime() + "ms";
            final float pingWidth = Main.fontManager.getStringWidth(ping);
            final float totalWidth = Main.fontManager.getStringWidth(ping + ms);
            Main.fontManager.drawStringWithShadow(ping, x, y, ((ClickGui) Main.moduleManager.getModuleByClass(ClickGui.class)).color.GetColor().getRGB());
            Main.fontManager.drawStringWithShadow(ms, x + pingWidth, y, -1);
            setWidth(totalWidth);
        } catch (Exception ignored) {
        }
    }
}
