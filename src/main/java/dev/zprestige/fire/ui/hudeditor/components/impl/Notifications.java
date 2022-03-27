package dev.zprestige.fire.ui.hudeditor.components.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.ClickGui;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import dev.zprestige.fire.util.impl.AnimationUtil;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class Notifications extends HudComponent {
    public final ArrayList<Notification> notifications = new ArrayList<>();

    public Notifications() {
        super("Notifications", new Vector2D(0, 200), new Vector2D(120, 30));
    }

    @Override
    public void render() {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        setPosition(new Vector2D(scaledResolution.getScaledWidth() - 120, position.getY()));
        float deltaY = position.getY();
        for (final Notification notification : new ArrayList<>(notifications)) {
            if (notification.finished || notification.pos.getX() < 600){
                notifications.remove(notification);
                continue;
            }
            notification.setY(deltaY);
            deltaY += notification.getHeight();
        }
        notifications.forEach(notification -> notification.update((int) (Minecraft.getDebugFPS() / 20.0f)));
        notifications.forEach(Notification::render);
    }

    protected static boolean isWordEnable(final String string) {
        return string.equals("On.");
    }

    protected static boolean isWordDisable(final String string) {
        return string.equals("Off.");
    }

    protected static boolean isWordSpecial(final String string) {
        return Main.moduleManager.getModules().stream().anyMatch(module -> module.getName().equals(string)) || string.equals("OnGround.") || string.equals("Strafe.");
    }

    protected static boolean isTotemPopNumber(final String string) {
        return IntStream.range(0, 100).anyMatch(i -> string.equals(i + ""));
    }


    public static class Notification {
        protected final ScaledResolution scaledResolution;
        protected final String text;
        protected Vector2D pos;
        protected float alpha, lineWidth;
        protected boolean end, finished;
        protected float height;

        public Notification(final String text, final ScaledResolution scaledResolution, final float y) {
            this.text = text;
            this.scaledResolution = scaledResolution;
            this.alpha = 0.0f;
            this.lineWidth = 118.0f;
            this.end = false;
            this.finished = false;
            this.pos = new Vector2D(scaledResolution.getScaledWidth(), y);
            this.height = 32.0f;
        }

        public void render() {
            RenderUtil.drawRect(pos, new Vector2D(pos.getX() + 120, pos.getY() + 30), new Color(ClickGui.Instance.backgroundColor.GetColor().getRed(), ClickGui.Instance.backgroundColor.GetColor().getGreen(), ClickGui.Instance.backgroundColor.GetColor().getBlue(), (int) Math.max(Math.min(255, alpha), 0)).getRGB());
            final String[] words = text.split(" ");
            float deltaX = 2.0f;
            float deltaY = 2.0f;
            for (String string : words) {
                if (deltaX + Main.fontManager.getStringWidth(string) >= 118) {
                    deltaY += Main.fontManager.getFontHeight() + 4;
                    deltaX = 2.0f;
                }
                Main.fontManager.drawStringWithShadow(string, new Vector2D(pos.getX() + deltaX, pos.getY() + deltaY), isWordEnable(string) ? Color.GREEN.getRGB() : isWordDisable(string) ? Color.RED.getRGB() : isTotemPopNumber(string) || isWordSpecial(string) ? ClickGui.Instance.color.GetColor().getRGB() : -1);
                deltaX += Main.fontManager.getStringWidth(string + " ");

            }
            RenderUtil.drawOutline(pos.getX(), pos.getY(), pos.getX() + 120, pos.getY() + 30, new Color(0, 0, 0, (int) Math.max(Math.min(255, alpha), 0) / 10), 1.0f);
            RenderUtil.drawRect(new Vector2D(pos.getX() + 1, pos.getY() + 28), new Vector2D(pos.getX() + lineWidth, pos.getY() + 29), ClickGui.Instance.color.GetColor().getRGB());
        }

        public void update(final int division) {
            if (!end) {
                boolean updating = false;
                if (alpha < ClickGui.Instance.backgroundColor.GetColor().getAlpha() - 2) {
                    alpha = AnimationUtil.increaseNumber(alpha, ClickGui.Instance.backgroundColor.GetColor().getAlpha(), (ClickGui.Instance.backgroundColor.GetColor().getAlpha() - alpha) / division);
                    updating = true;
                } else {
                    alpha = ClickGui.Instance.backgroundColor.GetColor().getAlpha();
                }
                if (pos.getX() > scaledResolution.getScaledWidth() - 121) {
                    pos = new Vector2D(pos.getX() - (pos.getX() - (scaledResolution.getScaledWidth() - 123)) / division, pos.getY());
                    updating = true;
                }
                if (!updating) {
                    end = true;
                }
            } else {
                if (lineWidth > 1.0f) {
                    lineWidth = AnimationUtil.decreaseNumber(lineWidth, 0.0f, lineWidth / division);
                } else {
                    boolean updating = false;
                    if (alpha > 1.0f) {
                        alpha = AnimationUtil.decreaseNumber(alpha, 0.0f, alpha / division);
                        updating = true;
                    }
                    if (pos.getX() < scaledResolution.getScaledWidth() - 2) {
                        pos = new Vector2D(pos.getX() + ((scaledResolution.getScaledWidth() - pos.getX()) / division), pos.getY());
                        updating = true;
                    }
                    if (!updating) {
                        height = AnimationUtil.decreaseNumber(height, 0.0f, height / division);
                        if (height < 1.0f){
                            finished = true;
                        }
                    }
                }
            }
        }

        public void setY(final float y) {
            pos = new Vector2D(pos.getX(), y);
        }

        public float getHeight() {
            return height;
        }
    }
}
