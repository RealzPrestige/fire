package dev.zprestige.fire.module.visual.chams

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.ColorBox
import dev.zprestige.fire.settings.impl.Slider
import dev.zprestige.fire.settings.impl.Switch
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.opengl.GL11
import java.awt.Color

@Descriptor(description = "Draws chams on entities")
class Chams : Module() {
    val players: Switch = Menu.Switch("Players", false)
    val crystals: Switch = Menu.Switch("Crystals", false)
    val fill: Switch = Menu.Switch("Fill", false)
    val fillColor: ColorBox = Menu.Color("Fill Color", Color.WHITE).visibility { z: Color? -> fill.GetSwitch() }
    val outline: Switch = Menu.Switch("Outline", false)
    val outlineColor: ColorBox = Menu.Color("Outline Color", Color.WHITE).visibility { z: Color? -> outline.GetSwitch() }
    val outlineWidth: Slider = Menu.Slider("Outline Width", 1.0f, 0.1f, 5.0f).visibility { z: Float? -> outline.GetSwitch() }
    val popChams: Switch = Menu.Switch("PopChams", false).panel("PopChams")
    val popSelf: Switch = Menu.Switch("Self", false).panel("PopChams").visibility { z: Boolean? -> popChams.GetSwitch() }
    val popAnimateVertical: Switch =
        Menu.Switch("Animate Vertical", false).panel("PopChams").visibility { z: Boolean? -> popChams.GetSwitch() }
    val popVerticalAnimationSpeed: Slider = Menu.Slider("Vertical Animation Speed", 2.5f, -5.0f, 5.0f).panel("PopChams")
        .visibility { popChams.GetSwitch() && popAnimateVertical.GetSwitch() }
    val popAnimationSpeed: Slider = Menu.Slider("Animation Speed", 2.5f, 0.1f, 5.0f).panel("PopChams")
        .visibility { z: Float? -> popChams.GetSwitch() }
    val popStartAlpha: Slider = Menu.Slider("Start Alpha", 120.0f, 0.1f, 255.0f).panel("PopChams")
        .visibility { z: Float? -> popChams.GetSwitch() }
    val popFill = Menu.Switch("Pop Fill", false).panel("PopChams").visibility { z: Boolean? -> popChams.GetSwitch() }
    val popFillColor: ColorBox = Menu.Color("Pop Fill Color", Color.WHITE).panel("PopChams")
        .visibility { z: Color? -> popChams.GetSwitch() && popFill.GetSwitch() }
    val popOutline: Switch =
        Menu.Switch("Pop Outline", false).panel("PopChams").visibility { z: Boolean? -> popChams.GetSwitch() }
    val popOutlineColor: ColorBox = Menu.Color("Pop Outline Color", Color.WHITE).panel("PopChams")
        .visibility { z: Color? -> popChams.GetSwitch() && popOutline.GetSwitch() }
    val popOutlineWidth: Slider = Menu.Slider("Pop Outline Width", 1.0f, 0.1f, 5.0f).panel("PopChams")
        .visibility { z: Float? -> popChams.GetSwitch() && popOutline.GetSwitch() }
    val popEntities = ArrayList<PopEntity>()
    val factor = 770
    val factorAlpha = 1

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            Frame3DListener(this),
            RenderLivingBaseListener(this),
            RenderCrystalListener(this),
            TotemPopListener(this)
        )
    }

    fun prepareFill(color: Color) {
        GL11.glPushMatrix()
        GL11.glEnable(GL11.GL_ALPHA_TEST)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_LIGHTING)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GlStateManager.tryBlendFuncSeparate(factor, factor + factorAlpha, factorAlpha, 0)
        GL11.glColor4f(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, color.alpha / 255.0f)
    }

    fun releaseFill() {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glDisable(GL11.GL_ALPHA_TEST)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glEnable(GL11.GL_LIGHTING)
        GL11.glPopMatrix()
        GL11.glEnable(GL11.GL_BLEND)
    }

    fun prepareOutline(color: Color, outlineWidth: Float) {
        GL11.glPushMatrix()
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_LIGHTING)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST)
        GL11.glLineWidth(outlineWidth)
        GL11.glColor4f(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, color.alpha / 255.0f)
    }

    fun releaseOutline() {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glEnable(GL11.GL_LIGHTING)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glPopMatrix()
    }

    fun addEntity(entity: EntityPlayer?) {
        val playerMP = EntityOtherPlayerMP(mc.world, mc.player.gameProfile)
        playerMP.copyLocationAndAnglesFrom(entity!!)
        playerMP.prevRotationYaw = playerMP.rotationYaw
        playerMP.prevRotationYawHead = playerMP.rotationYawHead
        playerMP.prevRotationPitch = playerMP.rotationPitch
        playerMP.entityId = 696969696
        popEntities.add(PopEntity(playerMP, popStartAlpha.GetSlider()))
    }

    inner class PopEntity(val entity: Entity, var alpha: Float) {

        fun updateAlpha(fps: Int) {
            alpha = alpha - alpha / (fps / popAnimationSpeed.GetSlider())
        }
    }
}