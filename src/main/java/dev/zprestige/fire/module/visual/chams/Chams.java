package dev.zprestige.fire.module.visual.chams;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

@Descriptor(description = "Draws chams on entities")
public class Chams extends Module {
    public final Switch fill = Menu.Switch("Fill", false);
    public final ColorBox fillColor = Menu.Color("Fill Color", Color.WHITE).visibility(z -> fill.GetSwitch());
    public final Switch outline = Menu.Switch("Outline", false);
    public final ColorBox outlineColor = Menu.Color("Outline Color", Color.WHITE).visibility(z -> outline.GetSwitch());
    public final Slider outlineWidth = Menu.Slider("Outline Width", 1.0f, 0.1f, 5.0f).visibility(z -> outline.GetSwitch());

    public final Switch popChams = Menu.Switch("PopChams", false).panel("PopChams");
    public final Switch popSelf = Menu.Switch("Self", false).panel("PopChams").visibility(z -> popChams.GetSwitch());
    public final Switch popAnimateVertical = Menu.Switch("Animate Vertical", false).panel("PopChams").visibility(z -> popChams.GetSwitch());
    public final Slider popVerticalAnimationSpeed = Menu.Slider("Vertical Animation Speed", 2.5f, -5.0f, 5.0f).panel("PopChams").visibility(z -> popChams.GetSwitch() && popAnimateVertical.GetSwitch());
    public final Slider popAnimationSpeed = Menu.Slider("Animation Speed", 2.5f, 0.1f, 5.0f).panel("PopChams").visibility(z -> popChams.GetSwitch());
    public final Slider popStartAlpha = Menu.Slider("Start Alpha", 120.0f, 0.1f, 255.0f).panel("PopChams").visibility(z -> popChams.GetSwitch());
    public final Switch popFill = Menu.Switch("Fill", false).panel("PopChams").visibility(z -> popChams.GetSwitch());
    public final ColorBox popFillColor = Menu.Color("Fill Color", Color.WHITE).panel("PopChams").visibility(z -> popChams.GetSwitch() && popFill.GetSwitch());
    public final Switch popOutline = Menu.Switch("Outline", false).panel("PopChams").visibility(z -> popChams.GetSwitch());
    public final ColorBox popOutlineColor = Menu.Color("Outline Color", Color.WHITE).panel("PopChams").visibility(z -> popChams.GetSwitch() && popOutline.GetSwitch());
    public final Slider popOutlineWidth = Menu.Slider("Outline Width", 1.0f, 0.1f, 5.0f).panel("PopChams").visibility(z -> popChams.GetSwitch() && popOutline.GetSwitch());
    protected final ArrayList<PopEntity> popEntities = new ArrayList<>();
    protected final int factor = 770, factorAlpha = 1;

    public Chams() {
        eventListeners = new EventListener[]{
                new Frame3DListener(this),
                new RenderLivingBaseListener(this),
                new RenderCrystalListener(this),
                new TotemPopListener(this)
        };
    }

    protected void prepareFill(final Color color) {
        glPushMatrix();
        glEnable(GL_ALPHA_TEST);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);
        GlStateManager.tryBlendFuncSeparate(factor, factor + factorAlpha, factorAlpha, 0);
        glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }

    protected void releaseFill() {
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_ALPHA_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_LIGHTING);
        glPopMatrix();
        glEnable(GL_BLEND);
    }

    protected void prepareOutline(final Color color, final float outlineWidth) {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glLineWidth(outlineWidth);
        glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);

    }

    protected void releaseOutline() {
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }

    protected void addEntity(final EntityPlayer entity) {
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
        popEntities.add(new PopEntity(playerMP, popStartAlpha.GetSlider()));
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
            setAlpha(getAlpha() - (getAlpha() / (fps / popAnimationSpeed.GetSlider())));
        }


        public Entity getEntity() {
            return entity;
        }
    }
}
