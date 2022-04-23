package dev.zprestige.fire.module.misc.fakeplayer;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;

public class TickListener extends EventListener<TickEvent, FakePlayer> {

    public TickListener(final FakePlayer fakePlayer) {
        super(TickEvent.class, fakePlayer);
    }

    @Override
    public void invoke(final Object object) {
        if (module.start.GetSwitch()) {
            if (module.record.GetSwitch()){
                Main.chatManager.sendMessage("Cant record and start at the same time, ending recording");
                module.start.setValue(false);
                return;
            }
            if (module.recording.isEmpty()) {
                Main.chatManager.sendMessage("No recording found, record first before starting.");
                module.start.setValue(false);
                return;
            }
            if (module.running == module.recording.size()){
                module.running = 0;
                return;
            }
            final FakePlayer.Location location = module.recording.get(module.running);
            module.fakePlayer.prevPosX = module.prevPosX;
            module.fakePlayer.prevPosY = module.prevPosY;
            module.fakePlayer.prevPosZ = module.prevPosZ;
            module.fakePlayer.prevRotationYaw = module.prevRotationYaw;
            module.fakePlayer.prevRotationYawHead = module.prevRotationYawHead;
            module.fakePlayer.prevRotationPitch = module.prevRotationPitch;
            module.fakePlayer.posX = location.getX();
            module.fakePlayer.posY = location.getY();
            module.fakePlayer.posZ = location.getZ();
            module.fakePlayer.rotationYaw = location.getRotationYaw();
            module.fakePlayer.rotationYawHead = location.getRotationYawHead();
            module.fakePlayer.rotationPitch = location.getRotationPitch();
            module.prevPosX = location.getX();
            module.prevPosY = location.getY();
            module.prevPosZ = location.getZ();
            module.prevRotationYaw = location.getRotationYaw();
            module.prevRotationYawHead = location.getRotationYawHead();
            module.prevRotationPitch = location.getRotationPitch();
            module.running++;
        }
        if (module.record.GetSwitch()) {
            if (!module.recorded) {
                module.index = 0;
                module.recorded = true;
                module.recording.clear();
            }
            module.recording.add(new FakePlayer.Location((float) mc.player.posX, (float) mc.player.posY, (float) mc.player.posZ, mc.player.rotationYaw, mc.player.rotationYawHead, mc.player.rotationPitch, module.index++));
        } else if (module.recorded) {
            module.index = 0;
            module.recorded = false;
        }
        if (module.fakePlayer != null && module.fakePlayer.getDistanceSq(mc.player) > 100000) {
            mc.world.removeEntityFromWorld(module.id);
            module.disableModule();
        }
    }
}
