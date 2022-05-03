package dev.zprestige.fire.ui.hudeditor.components.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.clickgui.ClickGui;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerList extends HudComponent {

    public PlayerList() {
        super("PlayerList", 0, 60, Main.fontManager.getStringWidth("ExampleName 20hp 50m"), Main.fontManager.getFontHeight());
    }

    @Override
    public void render() {
        float longestName = 0.0f, deltaY = 0.0f;
        for (final EntityPlayer entityPlayer : mc.world.playerEntities){
            if (entityPlayer.equals(mc.player)){
                continue;
            }
            final String name = entityPlayer.getName();
            final float health = (float) Math.ceil(entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()), dist = (float) Math.ceil(Math.sqrt(mc.player.getDistanceSq(entityPlayer))), length = Main.fontManager.getStringWidth(name);
            Main.fontManager.drawStringWithShadow(name + " " + getFormattingByHp(health) + health + ChatFormatting.WHITE +  "hp " + getFormattingByDistance(dist) + dist + ChatFormatting.WHITE + "m", x, y + deltaY, ((ClickGui) Main.moduleManager.getModuleByClass(ClickGui.class)).color.GetColor().getRGB());
            if (length > longestName){
                longestName = length;
            }
            deltaY += Main.fontManager.getFontHeight() + 3.0f;
        }
        setWidth(longestName);
        setHeight(deltaY + Main.fontManager.getFontHeight() + 3.0f);
    }

    protected ChatFormatting getFormattingByHp(final float hp){
        if (hp >= 30){
            return ChatFormatting.GREEN;
        }
        if (hp >= 20){
            return ChatFormatting.GOLD;
        }
        if (hp >= 10){
            return ChatFormatting.RED;
        }
        return ChatFormatting.DARK_RED;
    }

    protected ChatFormatting getFormattingByDistance(final float dist){
        if (dist >= 50){
            return ChatFormatting.GREEN;
        }
        if (dist >= 30){
            return ChatFormatting.GOLD;
        }
        if (dist >= 15){
            return ChatFormatting.RED;
        }
        return ChatFormatting.DARK_RED;
    }
}