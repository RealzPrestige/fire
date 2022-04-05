package dev.zprestige.fire.module.visual;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.DeathEvent;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.events.impl.TotemPopEvent;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

public class PopChams extends Module {
    public final Switch  animateVertical = Menu.Switch("Animate Vertical", false);
    public final Slider verticalAnimationSpeed = Menu.Slider("Vertical Animation Speed",2.5f, -5.0f, 5.0f).visibility(z -> animateVertical.GetSwitch());
    public final Slider animationSpeed = Menu.Slider("Animation Speed", 2.5f, 0.1f, 5.0f);
    public final Slider startAlpha = Menu.Slider("Start Alpha", 120.0f, 0.1f, 255.0f);
    public final Switch solid = Menu.Switch("Solid Color", true);
    public final ColorBox solidColor = Menu.Color("Solid Color", Color.WHITE).visibility(z -> solid.GetSwitch());
    public final Switch wireframe = Menu.Switch("Wireframe", true);
    public final ColorBox wireframeColor = Menu.Color("Wireframe Color", Color.WHITE).visibility(z -> wireframe.GetSwitch());
    protected final ArrayList<PopEntity> popEntities = new ArrayList<>();

    @RegisterListener
    public void onFrame3D(final FrameEvent.FrameEvent3D event) {
        final float partialTicks = event.getPartialTicks();
        final int fps = Minecraft.getDebugFPS();
        for (final PopEntity popEntity : popEntities) {
            final float alpha = popEntity.getAlpha();
            final Entity entity = popEntity.getEntity();
            if (animateVertical.GetSwitch()){
                entity.posY += verticalAnimationSpeed.GetSlider() / 100.0f;
            }
            if (solid.GetSwitch()) {
                prepareSolid(alpha);
                renderEntity(entity, partialTicks);
                releaseSolid();
            }
            if (wireframe.GetSwitch()) {
                prepareWireframe(alpha);
                renderEntity(entity, partialTicks);
                releaseWireframe();
            }
            popEntity.updateAlpha(fps);
            if (popEntity.getAlpha() < 30) {
                popEntities.remove(popEntity);
                break;
            }
        }
    }

    @RegisterListener
    public void onTotemPop(final TotemPopEvent event) {
        addEntity(event.getEntityPlayer());
    }

    protected void addEntity(final EntityPlayer entity){
        final EntityOtherPlayerMP playerMP = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        playerMP.copyLocationAndAnglesFrom(entity);
        playerMP.rotationYawHead = entity.rotationYawHead;
        playerMP.prevRotationYawHead = entity.rotationYawHead;
        playerMP.rotationYaw = entity.rotationYaw;
        playerMP.prevRotationYaw = entity.rotationYaw;
        playerMP.rotationPitch = entity.rotationPitch;
        playerMP.prevRotationPitch = entity.rotationPitch;
        playerMP.cameraYaw = entity.rotationYaw;
        playerMP.cameraPitch = entity.rotationPitch;
        playerMP.limbSwing = entity.limbSwing;
        playerMP.limbSwingAmount = entity.limbSwingAmount;
        playerMP.prevLimbSwingAmount = entity.limbSwingAmount;
        popEntities.add(new PopEntity(playerMP, startAlpha.GetSlider()));
    }

    protected void prepareSolid(final float alpha) {
        GL11.glPushMatrix();
        GL11.glDepthRange(0.01, 1.0f);
        GL11.glPushAttrib(GL11.GL_ALL_CLIENT_ATTRIB_BITS);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(1f);
        GL11.glColor4f(solidColor.GetColor().getRed() / 255f, solidColor.GetColor().getGreen() / 255f, solidColor.GetColor().getBlue() / 255f, alpha / 255f);

    }

    protected void releaseSolid() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glPopAttrib();
        GL11.glDepthRange(0.0, 1.0f);
        GL11.glPopMatrix();
    }

    protected void prepareWireframe(final float alpha) {
        GlStateManager.pushMatrix();
        GL11.glPushAttrib(1048575);
        GL11.glPolygonMode(1032, 6913);
        glDisable(3553);
        glDisable(2896);
        glDisable(2929);
        glEnable(2848);
        glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(wireframeColor.GetColor().getRed() / 255f, wireframeColor.GetColor().getGreen() / 255f, wireframeColor.GetColor().getBlue() / 255f, alpha / 255f);
    }

    protected void releaseWireframe() {
        GL11.glLineWidth(1f);
        glEnable(2896);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    protected void renderEntity(final Entity entity, final float partialTicks) {
        if (entity.ticksExisted == 0) {
            entity.lastTickPosX = entity.posX;
            entity.lastTickPosY = entity.posY;
            entity.lastTickPosZ = entity.posZ;
        }
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
        final float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        final float l = 65536.0f;
        final int i = entity.getBrightnessForRender();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i % l, i / l);
        mc.getRenderManager().renderEntity(entity, x - mc.getRenderManager().viewerPosX, y - mc.getRenderManager().viewerPosY, z - mc.getRenderManager().viewerPosZ, yaw, partialTicks, false);
    }

    protected class PopEntity {
        protected final Entity entity;
        protected float alpha;

        public PopEntity(final Entity entity, final float startAlpha) {
            this.entity = entity;
            this.alpha = startAlpha;
        }

        public float getAlpha() {
            return alpha;
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        public void updateAlpha(final int fps) {
            setAlpha(getAlpha() - (getAlpha() / (fps / animationSpeed.GetSlider())));
        }


        public Entity getEntity() {
            return entity;
        }
    }
}
