package dev.zprestige.fire.ui.hudeditor.components.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.clickgui.ClickGui;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.world.WorldType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Coords extends HudComponent {
    protected boolean aligned;

    public Coords() {
        super("Coords", 0, 0, 0, 0);
    }

    @Override
    public void render() {
        if (!aligned && !loaded) {
            final ScaledResolution scaledResolution = new ScaledResolution(mc);
            setPosition(0, scaledResolution.getScaledHeight() - Main.fontManager.getFontHeight() * 2);
            aligned = true;
        }
        final int color = ((ClickGui) Main.moduleManager.getModuleByClass(ClickGui.class)).color.GetColor().getRGB();
        final boolean nether = mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell");
        final float multiplication = nether ? 8.0f : 0.125f;
        final String string = ChatFormatting.WHITE + "XYZ " + ChatFormatting.RESET + roundCoordinate(mc.player.posX) + ChatFormatting.WHITE + ", " + ChatFormatting.RESET + roundCoordinate(mc.player.posY) + ChatFormatting.WHITE + ", " + ChatFormatting.RESET + roundCoordinate(mc.player.posZ) + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + roundCoordinate(mc.player.posX* multiplication) + ChatFormatting.WHITE + "," + ChatFormatting.RESET + roundCoordinate(mc.player.posZ* multiplication) + ChatFormatting.WHITE + "]";
        Main.fontManager.drawStringWithShadow(string, x, y + Main.fontManager.getFontHeight(), color);
        setHeight(Main.fontManager.getFontHeight() * 2);
        setWidth(Main.fontManager.getStringWidth(string));
    }

    protected final float roundCoordinate(final double i) {
        return BigDecimal.valueOf(i).setScale(1, RoundingMode.FLOOR).floatValue();
    }
}
