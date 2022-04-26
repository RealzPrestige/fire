package dev.zprestige.fire.module.visual.nametags;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Slider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import java.awt.*;

@Descriptor(description = "Renders info above the heads of players")
public class NameTags extends Module {
    public final Slider scale = Menu.Slider("Scale", 1.5f, 0.1f, 10.0f);
    protected final float magicNumber = 7.083333333333333f, secondMagicNumber = 1.02f;

    public NameTags() {
        eventListeners = new EventListener[]{
                new Frame3DListener(this),
                new RenderEntityNameListener(this)
        };
    }

    protected float redByPercentage(final double percentage) {
        return (float) (255.0f - (percentage * 2.5f));
    }

    protected float greenByPercentage(final double percentage) {
        return (float) (percentage * 2.5f);
    }

    protected float getPercentage(final ItemStack stack) {
        final float durability = stack.getMaxDamage() - stack.getItemDamage();
        return (durability / stack.getMaxDamage()) * 100.0f;
    }

    protected void drawItem(ItemStack itemStack, final float x, final float y){
        GlStateManager.pushMatrix();
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().zLevel = -150;
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int) x, (int) y);
        mc.getRenderItem().zLevel = 0;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    protected Color getLatencyColor(final float latency) {
        final float g = safety(((250 - latency) * secondMagicNumber) / 255.0f);
        final float r = safety((latency * secondMagicNumber) / 255.0f);
        return new Color(r, g, 0, 1.0f);
    }

    protected float safety(final float input) {
        return Math.min(1.0f, Math.max(0.0f, input));
    }

    public Color getHealthColor(final float health) {
        final float g = (health * magicNumber) / 255.0f;
        final float r = ((36 - health) * magicNumber) / 255.0f;
        return new Color(r, g, 0, 1.0f);
    }
}
