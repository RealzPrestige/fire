package dev.zprestige.fire.module.misc;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.manager.PlayerManager;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.EntityUtil;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class RunDetect extends Module {
    public final Slider radius = Menu.Slider("Radius", 3.0f, 0.1f, 15.0f);
    protected final ArrayList<EntityPlayer> potentialRunnersList = new ArrayList<>(), swordedPotentialRunnersList = new ArrayList<>(), finalRunningPlayers = new ArrayList<>();

    @RegisterListener
    public void onTick(final TickEvent event) {
        mc.world.playerEntities.stream().filter(player -> !player.equals(mc.player) && !potentialRunnersList.contains(player) && mc.player.getDistanceSq(EntityUtil.getPlayerPos(player)) < (radius.GetSlider() * radius.GetSlider())).forEach(potentialRunnersList::add);
        potentialRunnersList.stream().filter(entityPlayer -> !swordedPotentialRunnersList.contains(entityPlayer) && entityPlayer.getHeldItemMainhand().getItem().equals(Items.DIAMOND_SWORD)).forEach(swordedPotentialRunnersList::add);
        swordedPotentialRunnersList.stream().filter(entityPlayer -> entityPlayer.getHeldItemMainhand().getItem().equals(Items.GOLDEN_APPLE) && !finalRunningPlayers.contains(entityPlayer)).forEach(finalRunningPlayers::add);
        potentialRunnersList.stream().filter(entityPlayer -> mc.player.getDistanceSq(EntityUtil.getPlayerPos(entityPlayer)) > (radius.GetSlider() * radius.GetSlider())).findFirst().ifPresent(potentialRunnersList::remove);
        swordedPotentialRunnersList.stream().filter(entityPlayer -> mc.player.getDistanceSq(EntityUtil.getPlayerPos(entityPlayer)) > (radius.GetSlider() * radius.GetSlider())).findFirst().ifPresent(potentialRunnersList::remove);
    }

    @RegisterListener
    public void onFrame3D(final FrameEvent.FrameEvent3D event) {
        final int fps = Minecraft.getDebugFPS();
        final float factor = fps / 100000.0f;
        final String string = "Potentially running.";
        final float width = Main.fontManager.getStringWidth(string);
        for (final EntityPlayer entityPlayer : new ArrayList<>(finalRunningPlayers)) {
            final Vec3d interpolatedPos = RenderUtil.interpolateEntity(entityPlayer);
            RenderUtil.prepare3D(interpolatedPos.x, interpolatedPos.y + 0.75, interpolatedPos.z, 5);
            Main.fontManager.drawStringWithShadow(string, new Vector2D(-(width / 2f), 0), -1);
            RenderUtil.release3D();
            if (!BlockUtil.isPlayerSafe(new PlayerManager.Player(entityPlayer)) || !entityPlayer.getHeldItemMainhand().getItem().equals(Items.GOLDEN_APPLE) || mc.player.getDistanceSq(EntityUtil.getPlayerPos(entityPlayer)) > radius.GetSlider() * radius.GetSlider()) {
                remove(entityPlayer);
            }
        }
    }

    protected void remove(final EntityPlayer entityPlayer) {
        potentialRunnersList.remove(entityPlayer);
        swordedPotentialRunnersList.remove(entityPlayer);
        finalRunningPlayers.remove(entityPlayer);
    }

}