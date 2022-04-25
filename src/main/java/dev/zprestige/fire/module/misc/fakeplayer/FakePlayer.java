package dev.zprestige.fire.module.misc.fakeplayer;

import com.mojang.authlib.GameProfile;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Switch;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.ArrayList;
import java.util.UUID;

@Descriptor(description = "Spawns a fake entity for e.g testing")
public class FakePlayer extends Module {
    protected final Switch start = Menu.Switch("Start", false);
    protected final Switch record = Menu.Switch("Record", false);
    protected final ArrayList<Location> recording = new ArrayList<>();
    protected final int id = 438297483;
    protected EntityOtherPlayerMP fakePlayer;
    protected boolean recorded;
    protected int index, running;
    protected float prevPosX = 0, prevPosY = 0, prevPosZ = 0, prevRotationYaw = 0, prevRotationYawHead = 0, prevRotationPitch = 0;
    public FakePlayer() {
        eventListeners = new EventListener[]{
                new TickListener(this)
        };
    }
    @Override
    public void onEnable() {
        if (mc.world != null) {
            fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("12cbdfad-33b7-4c07-aeac-01766e609482"), "zPrestige_"));
            fakePlayer.copyLocationAndAnglesFrom(mc.player);
            fakePlayer.inventory.copyInventory(mc.player.inventory);
            fakePlayer.setHealth(36);
            mc.world.addEntityToWorld(id, fakePlayer);
            prevPosX = (float) fakePlayer.posX;
            prevPosY = (float) fakePlayer.posY;
            prevPosZ = (float) fakePlayer.posZ;
            prevRotationYaw = fakePlayer.rotationYaw;
            prevRotationYawHead = fakePlayer.rotationYawHead;
            prevRotationPitch = fakePlayer.rotationPitch;
        }
    }

    @Override
    public void onDisable() {
        if (fakePlayer != null) {
            mc.world.removeEntityFromWorld(id);
        }
        index = 0;
        running = 0;
    }

    public static class Location {
        protected final float x, y, z, rotationYaw, rotationYawHead, rotationPitch;
        protected final int index;

        public Location(final float x, final float y, final float z, final float rotationYaw, final float rotationYawHead, final float rotationPitch, final int index) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.rotationYaw = rotationYaw;
            this.rotationYawHead = rotationYawHead;
            this.rotationPitch = rotationPitch;
            this.index = index;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }

        public float getRotationYaw() {
            return rotationYaw;
        }

        public float getRotationYawHead() {
            return rotationYawHead;
        }

        public float getRotationPitch() {
            return rotationPitch;
        }
    }
}