package dev.zprestige.fire.mixins.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.impl.PacketEvent;
import dev.zprestige.fire.module.visual.RotationRender;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetworkManager.class)
public class MixinNetworkManager {
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void onSendPacket(final Packet<?> packet, final CallbackInfo callbackInfo) {
        PacketEvent.PacketSendEvent event = new PacketEvent.PacketSendEvent(packet);
        Main.eventBus.post(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        } else if (event.getPacket() instanceof CPacketPlayer.Rotation || event.getPacket() instanceof CPacketPlayer.PositionRotation) {
            RotationRender.yaw = (((CPacketPlayer) event.getPacket()).getYaw(0));
            RotationRender.pitch = (((CPacketPlayer) event.getPacket()).getPitch(0));

        }
    }

    @Inject(method = "channelRead0*", at = @At("HEAD"), cancellable = true)
    public void onPacketReceive(final ChannelHandlerContext channelHandlerContext, final Packet<?> packet, final CallbackInfo callbackInfo) {
        PacketEvent.PacketReceiveEvent event = new PacketEvent.PacketReceiveEvent(packet);
        Main.eventBus.post(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }
}
