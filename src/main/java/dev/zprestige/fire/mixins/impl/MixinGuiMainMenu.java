package dev.zprestige.fire.mixins.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.ClickGui;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = GuiMainMenu.class)
public class MixinGuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    @ModifyArg(method = "initGui", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2))
    private Object moveLanguageButton(Object buttonIn) {
        final GuiButton guiButton = (GuiButton) buttonIn;
        // fuck u ugly bitch
        guiButton.x += 9999;
        return guiButton;
    }

    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.image(new ResourceLocation("textures/images/background.png"), 0, 0, width, height);
        final String name = Main.name + " ";
        Main.fontManager.drawStringWithShadow(name, new Vector2D(2, height - 10), ClickGui.Instance.color.GetColor().getRGB());
        Main.fontManager.drawStringWithShadow(Main.version, new Vector2D(2 + Main.fontManager.getStringWidth(name), height - 10), -1);
        RenderUtil.image(new ResourceLocation("textures/images/fire_512.png"), width / 2 - 30, height / 2 - 150, 60, 60);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}