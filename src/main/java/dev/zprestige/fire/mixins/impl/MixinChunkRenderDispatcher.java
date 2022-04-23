package dev.zprestige.fire.mixins.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.visual.viewtweaks.ViewTweaks;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChunkRenderDispatcher.class)
public class MixinChunkRenderDispatcher {
    @Inject(method = "getNextChunkUpdate", at = @At("HEAD"))
    protected void limitChunkUpdates(final CallbackInfoReturnable<ChunkCompileTaskGenerator> cir) throws InterruptedException {
        final ViewTweaks viewTweaks = (ViewTweaks) Main.moduleManager.getModuleByClass(ViewTweaks.class);
        if (viewTweaks.isEnabled() && !Main.mc.isSingleplayer()){
            Thread.sleep((long) viewTweaks.chunkLoadDelay.GetSlider());
        }
    }
}