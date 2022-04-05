package dev.zprestige.fire.ui.hudeditor.components.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class Armor extends HudComponent {

    public Armor() {
        super("Armor", new Vector2D(0, 40), new Vector2D(0, 0));
    }

    @Override
    public void render() {
        final int[] armorOffset = {0};
        mc.player.inventory.armorInventory.stream().filter(itemStack -> !itemStack.isEmpty()).forEach(itemStack -> {
            RenderUtil.prepareScale(0.7f);
            final double percent = Math.floor(getPercentage(itemStack));
            final String string = (percent + "%").replace(".0", "");
            Main.fontManager.drawStringWithShadow(string, new Vector2D(((position.getX() + armorOffset[0]) + (12 - (Main.fontManager.getStringWidth(string) / 2f))) / 0.7f, (position.getY() - 2) / 0.7f), new Color(redByPercentage(percent) / 255.0f, greenByPercentage(percent) / 255.0f, 0.0f).getRGB());
            RenderUtil.releaseScale();
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().zLevel = 200;
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int) (position.getX() + armorOffset[0]), (int) (position.getY() + Main.fontManager.getFontHeight()));
            mc.getRenderItem().zLevel = 0;
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
            armorOffset[0] += 16;
        });
        setHeight(Main.fontManager.getFontHeight() + 16);
        setWidth(armorOffset[0]);
    }

    protected float getPercentage(final ItemStack stack) {
        final float durability = stack.getMaxDamage() - stack.getItemDamage();
        return (durability / stack.getMaxDamage()) * 100.0f;
    }

    protected float redByPercentage(final double percentage) {
        return (float) (255.0f - (percentage * 2.5f));
    }

    protected float greenByPercentage(final double percentage) {
        return (float) (percentage * 2.5f);
    }

}
