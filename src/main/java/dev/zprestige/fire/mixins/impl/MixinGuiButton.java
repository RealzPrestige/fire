package dev.zprestige.fire.mixins.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.ClickGui;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(value = GuiButton.class)
public abstract class MixinGuiButton {
    @Shadow
    public int x;
    @Shadow
    public int y;
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    public boolean visible;
    @Shadow
    public String displayString;
    @Shadow
    protected boolean hovered;

    @Shadow
    protected abstract void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY);

    @Inject(method = {"drawButton"}, at = {@At("HEAD")}, cancellable = true)
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float ignoredPartialTicks, final CallbackInfo callbackInfo) {
        if (visible) {
            hovered = (mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height);
            RenderUtil.drawRect(new Vector2D(x, y), new Vector2D(x + width, y + height), ClickGui.Instance.backgroundColor.GetColor().getRGB());
            RenderUtil.drawRect(new Vector2D(x + 1, y + 1), new Vector2D(x + width - 1, y + 2), ClickGui.Instance.color.GetColor().getRGB());
            RenderUtil.drawOutline(x, y, x + width, y + height, new Color(0, 0, 0, 50), 1.0f);
            Main.fontManager.drawStringWithShadow(displayString, new Vector2D(x + width / 2f - Main.fontManager.getStringWidth(displayString) / 2f, y + height / 2f - Main.fontManager.getFontHeight() / 2f), -1);
            if (hovered) {
                RenderUtil.drawRect(new Vector2D(x, y), new Vector2D(x + width, y + height), new Color(0, 0, 0, 30).getRGB());
            }
            mouseDragged(mc, mouseX, mouseY);
        }
        callbackInfo.cancel();
    }
}