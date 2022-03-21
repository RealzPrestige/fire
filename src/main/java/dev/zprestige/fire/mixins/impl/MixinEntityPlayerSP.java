package dev.zprestige.fire.mixins.impl;

import com.mojang.authlib.GameProfile;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.impl.MoveEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayerSP.class)
public class MixinEntityPlayerSP extends AbstractClientPlayer {

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    /**
     * Thanks mmmax for helping me back in client 2.0.0 with this :) (still use it)
     **/
    @SuppressWarnings("NullableProblems")
    public void move(MoverType type, double x, double y, double z) {
        final MoveEvent event = new MoveEvent(type, x, y, z);
        Main.eventBus.post(event);
        super.move(type, event.getMotionX(), event.getMotionY(), event.getMotionZ());
    }

    @Inject(method = "move", at = @At(value = "HEAD"), cancellable = true)
    public void move(MoverType type, double x, double y, double z, CallbackInfo callbackInfo) {
        final MoveEvent event = new MoveEvent(type, x, y, z);
        Main.eventBus.post(event);
        if (event.getMotionX() != x || event.getMotionY() != y || event.getMotionZ() != z) {
            super.move(type, event.getMotionX(), event.getMotionY(), event.getMotionZ());
            callbackInfo.cancel();
        }
    }
}