package dev.zprestige.fire.mixins.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.impl.BlockInteractEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {PlayerControllerMP.class})
public class MixinPlayerControllerMP {
    @Inject(method = {"clickBlock"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void onClickBlock(BlockPos pos, EnumFacing facing, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final BlockInteractEvent.ClickBlock event = new BlockInteractEvent.ClickBlock(pos, facing);
        Main.eventBus.post(event);
    }

    @Inject(method = {"onPlayerDamageBlock"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void onPlayerDamageBlock(BlockPos pos, EnumFacing facing, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final BlockInteractEvent.DamageBlock event = new BlockInteractEvent.DamageBlock(pos, facing);
        Main.eventBus.post(event);
    }
}