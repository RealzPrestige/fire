package dev.zprestige.fire.ui.hudeditor.components.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.clickgui.ClickGui;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import dev.zprestige.fire.util.impl.Vector2D;

import java.util.Objects;

public class Ping extends HudComponent {

    public Ping() {
        super("Ping", new Vector2D(0, 10), new Vector2D(Main.fontManager.getStringWidth("Ping 00ms"), Main.fontManager.getFontHeight()));
    }

    @Override
    public void render() {
        try {
            final String ping = "Ping ";
            final String ms = Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.getConnection().getGameProfile().getId()).getResponseTime() + "ms";
            final float pingWidth = Main.fontManager.getStringWidth(ping);
            final float totalWidth = Main.fontManager.getStringWidth(ping + ms);
            Main.fontManager.drawStringWithShadow(ping, position, ClickGui.Instance.color.GetColor().getRGB());
            Main.fontManager.drawStringWithShadow(ms, new Vector2D(position.getX() + pingWidth, position.getY()), -1);
            setWidth(totalWidth);
        } catch (Exception ignored) {
        }
    }
}
