package dev.zprestige.fire.module.misc.rundetect

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent
import dev.zprestige.fire.util.impl.EntityUtil
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items

class TickListener(runDetect: RunDetect) : EventListener<TickEvent, RunDetect>(
    TickEvent::class.java, runDetect
) {
    override fun invoke(e: Any) {
        mc.world.playerEntities.stream().filter { player: EntityPlayer ->
            player != mc.player && !module.potentialRunnersList.contains(player) && mc.player.getDistanceSq(
                EntityUtil.getPlayerPos(player)
            ) < module.radius.GetSlider() * module.radius.GetSlider()
        }.forEach { entityPlayer: EntityPlayer? -> module.potentialRunnersList.add(entityPlayer!!) }
        module.potentialRunnersList.stream().filter { entityPlayer: EntityPlayer ->
            !module.swordedPotentialRunnersList.contains(entityPlayer) && entityPlayer.heldItemMainhand
                .getItem() == Items.DIAMOND_SWORD
        }.forEach { entityPlayer: EntityPlayer? -> module.swordedPotentialRunnersList.add(entityPlayer!!) }
        module.swordedPotentialRunnersList.stream().filter { entityPlayer: EntityPlayer ->
            entityPlayer.heldItemMainhand
                .getItem() == Items.GOLDEN_APPLE && !module.finalRunningPlayers.contains(entityPlayer)
        }.forEach { entityPlayer: EntityPlayer? -> module.finalRunningPlayers.add(entityPlayer!!) }
        module.potentialRunnersList.stream().filter { entityPlayer: EntityPlayer? ->
            mc.player.getDistanceSq(EntityUtil.getPlayerPos(entityPlayer)) > module.radius.GetSlider() * module.radius.GetSlider()
        }.findFirst().ifPresent { o: EntityPlayer? -> module.potentialRunnersList.remove(o) }
        module.swordedPotentialRunnersList.stream().filter { entityPlayer: EntityPlayer? ->
            mc.player.getDistanceSq(EntityUtil.getPlayerPos(entityPlayer)) > module.radius.GetSlider() * module.radius.GetSlider()
        }.findFirst().ifPresent { o: EntityPlayer? -> module.potentialRunnersList.remove(o) }
    }
}
