package dev.zprestige.fire.ui.hudeditor.components.impl.fps;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.client.clickgui.ClickGui;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import dev.zprestige.fire.util.impl.Timer;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.client.Minecraft;

import java.util.Arrays;

public class Fps extends HudComponent {
    protected int[] avarageFrames = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    protected final Timer timer = new Timer();

    public Fps() {
        super("Fps", new Vector2D(0, 20), new Vector2D(Main.fontManager.getStringWidth("Fps 100"), Main.fontManager.getFontHeight()));
        Main.eventBus.registerListeners(new EventListener[]{
                new TickListener(this)
        });
    }


    protected int average() {
        return Arrays.stream(avarageFrames).sum() / 10;
    }

    @Override
    public void render() {
        try {
            final String text = "Fps ";
            final String fps = Minecraft.getDebugFPS() + " (Average " + average() + ")";
            final float textWidth = Main.fontManager.getStringWidth(text);
            final float totalWidth = Main.fontManager.getStringWidth(text + fps);
            Main.fontManager.drawStringWithShadow(text, position, ClickGui.Instance.color.GetColor().getRGB());
            Main.fontManager.drawStringWithShadow(fps, new Vector2D(position.getX() + textWidth, position.getY()), -1);
            setWidth(totalWidth);
        } catch (Exception ignored) {
        }
    }
}