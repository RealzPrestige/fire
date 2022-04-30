package dev.zprestige.fire.module.misc.rundetect

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.FrameEvent.FrameEvent3D
import dev.zprestige.fire.manager.playermanager.PlayerManager
import dev.zprestige.fire.util.impl.BlockUtil
import dev.zprestige.fire.util.impl.EntityUtil
import dev.zprestige.fire.util.impl.RenderUtil
import net.minecraft.init.Items

class Frame3DListener(runDetect: RunDetect) : EventListener<FrameEvent3D, RunDetect>(FrameEvent3D::class.java,
    runDetect
) {

    override fun invoke(e: Any) {
        val string = "Potentially running."
        val width = Main.fontManager.getStringWidth(string)
        for (entityPlayer in ArrayList(module.finalRunningPlayers)) {
            val interpolatedPos = RenderUtil.interpolateEntity(entityPlayer)
            RenderUtil.prepare3D(interpolatedPos.x, interpolatedPos.y + 0.75, interpolatedPos.z, 0.0005)
            Main.fontManager.drawStringWithShadow(string, -(width / 2f), 0f, -1)
            RenderUtil.release3D()
            if (!BlockUtil.isPlayerSafe(PlayerManager.Player(entityPlayer)) || entityPlayer.heldItemMainhand.getItem() != Items.GOLDEN_APPLE || mc.player.getDistanceSq(
                    EntityUtil.getPlayerPos(entityPlayer)
                ) > module.radius.GetSlider() * module.radius.GetSlider()
            ) {
                module.remove(entityPlayer)
            }
        }
    }
}
