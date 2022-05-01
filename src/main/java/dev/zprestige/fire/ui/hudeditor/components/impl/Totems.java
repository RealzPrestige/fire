package dev.zprestige.fire.ui.hudeditor.components.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class Totems extends HudComponent {

    public Totems() {
        super("Totems", 0, 50, 16, 16);
    }

    @Override
    public void render() {
        final int totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem().equals(Items.TOTEM_OF_UNDYING))).mapToInt(ItemStack::getCount).sum() + (mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING) ? 1 : 0);
        if (totems > 0) {
            RenderUtil.prepareScale(0.8f);
            final String totemString = totems + "";
            Main.fontManager.drawStringWithShadow(totemString,(x + (9 - (Main.fontManager.getStringWidth(totemString) / 2f))) / 0.8f, (y - 2) / 0.8f, new Color(red(totems) / 255.0f, green(totems) / 255.0f, 0.0f).getRGB());
            RenderUtil.releaseScale();
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().zLevel = 200;
            mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.TOTEM_OF_UNDYING), (int) x, (int) (y + Main.fontManager.getFontHeight()));
            mc.getRenderItem().zLevel = 0;
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
            setHeight(16);
            setWidth(16);
        }
    }

    protected float green(final int tots) {
        return Math.min(255.0f, (tots * 10) * 2.5f);
    }

    protected float red(final int tots) {
        return 255.0f - Math.min(255.0f, (tots * 10) * 2.5f);
    }
}
