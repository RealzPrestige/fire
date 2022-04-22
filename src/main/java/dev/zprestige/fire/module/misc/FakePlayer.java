package dev.zprestige.fire.module.misc;

import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

@Descriptor(description = "Spawns a fake entity for e.g testing")
public class FakePlayer extends Module {
    protected final int id = 438297483;
    protected EntityOtherPlayerMP fakePlayer;

    protected UUID findUUIDByName() {
        try {
            final URLConnection request = new URL("https://api.mojang.com/users/profiles/minecraft/" + "FakePlayer").openConnection();
            request.connect();
            // what in the stegosauruses
            final String id = java.util.UUID.fromString(new JsonParser().parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject().get("id").getAsString().replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5")).toString();
            return UUID.fromString(id);
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void onEnable() {
        if (mc.world != null) {
            fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(findUUIDByName(), "FakePlayer"));
            fakePlayer.copyLocationAndAnglesFrom(mc.player);
            fakePlayer.inventory = mc.player.inventory;
            fakePlayer.setHealth(36);
            mc.world.addEntityToWorld(id, fakePlayer);
        }
    }

    @RegisterListener
    public void onTick(final TickEvent event) {
        if (fakePlayer != null && fakePlayer.getDistanceSq(mc.player) > 100000) {
            mc.world.removeEntityFromWorld(id);
            disableModule();
        }
    }

    @Override
    public void onDisable() {
        if (fakePlayer != null) {
            mc.world.removeEntityFromWorld(id);
        }
    }
}