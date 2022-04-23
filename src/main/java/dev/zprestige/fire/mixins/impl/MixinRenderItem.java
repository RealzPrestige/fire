package dev.zprestige.fire.mixins.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.impl.RenderItemEvent;
import dev.zprestige.fire.module.visual.viewmodel.ViewModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(value = RenderItem.class)
public class MixinRenderItem {
    protected final Minecraft mc = Main.mc;

    @Shadow
    private void renderModel(final IBakedModel iBakedModel, final int color, final ItemStack stack) {
    }

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V", at = @At("HEAD"))
    public void renderItem(final ItemStack itemStack, final EntityLivingBase entityLivingBase, final ItemCameraTransforms.TransformType transformType, final boolean leftHanded, final CallbackInfo callbackInfo) {
        if (transformType.equals(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) || transformType.equals(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)) {
            if (transformType.equals(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)) {
                Main.newBus.invokeEvent(new RenderItemEvent.Offhand(itemStack, entityLivingBase));
            } else {
                Main.newBus.invokeEvent(new RenderItemEvent.MainHand(itemStack, entityLivingBase));
            }
        }
    }

    @Redirect(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V"))
    public void renderModelColor(final RenderItem renderItem, final IBakedModel iBakedModel, final ItemStack itemStack) {
        final ViewModel viewModel = (ViewModel) Main.moduleManager.getModuleByClass(ViewModel.class);
        renderModel(iBakedModel, viewModel.isEnabled() ? viewModel.color.GetColor().getRGB() : new Color(1.0f, 1.0f, 1.0f, 1.0f).getRGB(), itemStack);
    }
}