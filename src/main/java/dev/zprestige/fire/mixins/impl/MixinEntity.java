package dev.zprestige.fire.mixins.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.impl.TurnEvent;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    public void onTurn(final float yaw, final float pitch, final CallbackInfo callbackInfo) {
        final TurnEvent event = new TurnEvent(yaw, pitch);
        Main.eventBus.invokeEvent(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }
}
