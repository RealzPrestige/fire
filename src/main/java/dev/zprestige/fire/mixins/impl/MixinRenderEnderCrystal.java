package dev.zprestige.fire.mixins.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.impl.RenderCrystalEvent;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = RenderEnderCrystal.class, priority = 1001)
public class MixinRenderEnderCrystal {

    @Final
    @Shadow
    private final ModelBase modelEnderCrystalNoBase = new ModelEnderCrystal(0.0F, false);

    @Redirect(method = "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void doRender(final ModelBase modelBase, Entity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        final RenderCrystalEvent renderCrystalEvent = new RenderCrystalEvent(modelBase, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Main.eventBus.post(renderCrystalEvent);
        if (!renderCrystalEvent.isCancelled()) {
            modelEnderCrystalNoBase.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
}