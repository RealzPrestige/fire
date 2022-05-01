package dev.zprestige.fire.mixins.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.impl.RenderEntityNameEvent;
import dev.zprestige.fire.module.visual.rotationrender.RotationRender;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer {
    private float renderPitch, renderYaw, renderHeadYaw, prevRenderHeadYaw, lastRenderHeadYaw = 0, prevRenderPitch, lastRenderPitch = 0;

    @Inject(method = {"renderEntityName*"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void renderEntityName(final AbstractClientPlayer entity, final double x, final double y, final double z, final String name, final double distanceSq, final CallbackInfo callbackInfo) {
        final RenderEntityNameEvent event = new RenderEntityNameEvent();
        Main.eventBus.invokeEvent(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }
    @Inject(method = "doRender*", at = @At("HEAD"))
    public void rotateBegin(final AbstractClientPlayer entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo callbackInfo) {
        if (Main.listener.checkNull() && Main.moduleManager.getModuleByClass(RotationRender.class).isEnabled() && entity.equals(Main.mc.player)) {
            prevRenderHeadYaw = entity.prevRotationYawHead;
            prevRenderPitch = entity.prevRotationPitch;
            renderPitch = entity.rotationPitch;
            renderYaw = entity.rotationYaw;
            renderHeadYaw = entity.rotationYawHead;
            entity.rotationPitch = RotationRender.pitch;
            entity.prevRotationPitch = lastRenderPitch;
            entity.rotationYaw = RotationRender.yaw;
            entity.rotationYawHead = RotationRender.yaw;
            entity.prevRotationYawHead = lastRenderHeadYaw;
        }
    }

    @Inject(method = "doRender*", at = @At("RETURN"))
    public void rotateEnd(final AbstractClientPlayer entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo callbackInfo) {
        if (Main.listener.checkNull() && Main.moduleManager.getModuleByClass(RotationRender.class).isEnabled() && entity.equals(Main.mc.player)) {
            lastRenderHeadYaw = entity.rotationYawHead;
            lastRenderPitch = entity.rotationPitch;
            entity.rotationPitch = renderPitch;
            entity.rotationYaw = renderYaw;
            entity.rotationYawHead = renderHeadYaw;
            entity.prevRotationYawHead = prevRenderHeadYaw;
            entity.prevRotationPitch = prevRenderPitch;
        }
    }
}