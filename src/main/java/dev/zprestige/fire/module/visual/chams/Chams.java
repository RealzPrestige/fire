package dev.zprestige.fire.module.visual.chams;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

@Descriptor(description = "Draws chams on entities")
public class Chams extends Module {
    public final Switch fill = Menu.Switch("Fill", false);
    public final ColorBox fillColor = Menu.Color("Fill Color", Color.WHITE).visibility(z -> fill.GetSwitch());
    public final Switch outline = Menu.Switch("Outline", false);
    public final ColorBox outlineColor = Menu.Color("Outline Color", Color.WHITE).visibility(z -> outline.GetSwitch());
    public final Slider outlineWidth = Menu.Slider("Outline Width", 1.0f, 0.1f, 5.0f).visibility(z -> outline.GetSwitch());
    protected final int factor = 770, factorAlpha = 1;

    public Chams(){
        eventListeners = new EventListener[]{
                new RenderLivingBaseListener(this),
                new RenderCrystalListener(this)
        };
    }

    protected void prepareFill(){
        glPushMatrix();
        glEnable(GL_ALPHA_TEST);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);
        GlStateManager.tryBlendFuncSeparate(factor, factor + factorAlpha, factorAlpha, 0);
        glColor4f(fillColor.GetColor().getRed() / 255.0f, fillColor.GetColor().getGreen() / 255.0f, fillColor.GetColor().getBlue() / 255.0f, fillColor.GetColor().getAlpha() / 255.0f);
    }
    protected void releaseFill(){
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_ALPHA_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_LIGHTING);
        glPopMatrix();
        glEnable(GL_BLEND);
    }

    protected void prepareOutline(){
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glLineWidth(outlineWidth.GetSlider());
        glColor4f(outlineColor.GetColor().getRed() / 255.0f, outlineColor.GetColor().getGreen() / 255.0f, outlineColor.GetColor().getBlue() / 255.0f, outlineColor.GetColor().getAlpha() / 255.0f);

    }

    protected void releaseOutline(){
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }
}
