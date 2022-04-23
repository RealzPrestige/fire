package dev.zprestige.fire.mixins.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.impl.BlockInteractEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerControllerMP.class)
public class MixinPlayerControllerMP {
    @Inject(method = "clickBlock", at = @At("HEAD"), cancellable = true)
    public void onClickBlock(final BlockPos pos, final EnumFacing facing, final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final BlockInteractEvent.ClickBlock event = new BlockInteractEvent.ClickBlock(pos, facing);
        Main.eventBus.invokeEvent(event);
    }

    @Inject(method = "onPlayerDamageBlock", at = @At("HEAD"), cancellable = true)
    public void onPlayerDamageBlock(final BlockPos pos, final EnumFacing facing, final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final BlockInteractEvent.DamageBlock event = new BlockInteractEvent.DamageBlock(pos, facing);
        Main.eventBus.invokeEvent(event);
    }
}