package dev.zprestige.fire.ui.hudeditor.components.impl.fps;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.client.clickgui.ClickGui;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import dev.zprestige.fire.util.impl.Timer;
import net.minecraft.client.Minecraft;

import java.util.Arrays;

public class Fps extends HudComponent {
    protected int[] avarageFrames = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    protected final Timer timer = new Timer();

    public Fps() {
        super("Fps", 0, 20, Main.fontManager.getStringWidth("Fps 100"), Main.fontManager.getFontHeight());
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
            final float totalWidth = Main.fontManager.getStringWidth(text + fps);
            Main.fontManager.drawStringWithShadow(text + ChatFormatting.WHITE + fps, x, y, ((ClickGui) Main.moduleManager.getModuleByClass(ClickGui.class)).color.GetColor().getRGB());
            setWidth(totalWidth);
        } catch (Exception ignored) {
        }
    }
}