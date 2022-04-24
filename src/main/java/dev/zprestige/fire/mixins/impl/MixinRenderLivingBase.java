package dev.zprestige.fire.mixins.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.impl.RenderLivingBaseEvent;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLivingBase.class)
public class MixinRenderLivingBase {

    @Shadow
    protected ModelBase mainModel;

    @Inject(method = "renderModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"), cancellable = true)
    public void doRenderPre(final EntityLivingBase entitylivingBase, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final CallbackInfo callbackInfo) {
        final RenderLivingBaseEvent event = new RenderLivingBaseEvent(mainModel, entitylivingBase, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        Main.eventBus.invokeEvent(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }
}