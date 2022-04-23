package dev.zprestige.fire.module.misc.rundetect;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.manager.playermanager.PlayerManager;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.FrameEvent;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.EntityUtil;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, RunDetect> {

    public Frame3DListener(final RunDetect runDetect){
        super(FrameEvent.FrameEvent3D.class, runDetect);
    }

    @Override
    public void invoke(final Object object){
        final String string = "Potentially running.";
        final float width = Main.fontManager.getStringWidth(string);
        for (final EntityPlayer entityPlayer : new ArrayList<>(module.finalRunningPlayers)) {
            final Vec3d interpolatedPos = RenderUtil.interpolateEntity(entityPlayer);
            RenderUtil.prepare3D(interpolatedPos.x, interpolatedPos.y + 0.75, interpolatedPos.z, 5);
            Main.fontManager.drawStringWithShadow(string, new Vector2D(-(width / 2f), 0), -1);
            RenderUtil.release3D();
            if (!BlockUtil.isPlayerSafe(new PlayerManager.Player(entityPlayer)) || !entityPlayer.getHeldItemMainhand().getItem().equals(Items.GOLDEN_APPLE) || mc.player.getDistanceSq(EntityUtil.getPlayerPos(entityPlayer)) > module.radius.GetSlider() * module.radius.GetSlider()) {
                module.remove(entityPlayer);
            }
        }
    }
}
