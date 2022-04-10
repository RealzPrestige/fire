package dev.zprestige.fire.ui.menu.panel.panels.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.ui.menu.panel.PanelScreen;
import dev.zprestige.fire.ui.menu.panel.panels.PanelDrawable;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.ArrayList;

public class PanelSocials extends PanelDrawable {
    protected boolean clickingFrame;

    public PanelSocials(final float x, final float y, final float width, final float height) {
        super("Socials", width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
        if (selected()) {
            final ArrayList<GuiPlayerComponent> allPlayers = new ArrayList<>(), friendPlayers = new ArrayList<>();
            final float center = PanelScreen.x + PanelScreen.secondStart + (PanelScreen.secondWidth - PanelScreen.secondEndY) / 2.0f;
            float allY = PanelScreen.y + PanelScreen.secondStartY + 2;
            float friendY = PanelScreen.y + PanelScreen.secondStartY + 2;
            RenderUtil.drawRoundedRect(center - 0.5f, allY, center + 0.5f, PanelScreen.y + PanelScreen.height - PanelScreen.secondEndY - 2.0f, 1, PanelScreen.thirdBackgroundColor);
            for (NetworkPlayerInfo networkPlayerInfo : Main.mc.player.connection.getPlayerInfoMap()) {
                final String name = networkPlayerInfo.getGameProfile().getName();
                if (PanelScreen.searchingString.equals("") || name.toLowerCase().contains(PanelScreen.searchingString.toLowerCase())) {
                    if (Main.friendManager.isFriend(name)) {
                        friendPlayers.add(new GuiPlayerComponent(networkPlayerInfo, new Vector2D(center + 5, friendY), new Vector2D(center - 5 + (PanelScreen.secondWidth - PanelScreen.secondEndY) / 2.0f, friendY + 25), true));
                        friendY += 30;
                    } else {
                        allPlayers.add(new GuiPlayerComponent(networkPlayerInfo, new Vector2D(PanelScreen.x + PanelScreen.secondStart + 5, allY), new Vector2D(center - 5, allY + 25), false));
                        allY += 30;
                    }
                }
            }
            RenderUtil.prepareScissor((int) PanelScreen.x, (int) (PanelScreen.y + PanelScreen.secondStartY), (int) PanelScreen.width, (int) (PanelScreen.height - PanelScreen.secondEndY));
            if (clickingFrame) {
                allPlayers.stream().filter(player -> player.position.getY() + player.size.getY() < PanelScreen.y + PanelScreen.height - PanelScreen.secondEndY).forEach(player -> player.click(mouseX, mouseY));
                friendPlayers.stream().filter(friendPlayer -> friendPlayer.position.getY() + friendPlayer.size.getY() < PanelScreen.y + PanelScreen.height - PanelScreen.secondEndY).forEach(friendPlayer -> friendPlayer.click(mouseX, mouseY));
                clickingFrame = false;
            }
            allPlayers.stream().filter(player -> player.position.getY() + player.size.getY() < PanelScreen.y + PanelScreen.height - PanelScreen.secondEndY).forEach(player -> player.render(mouseX, mouseY));
            friendPlayers.stream().filter(friend -> friend.position.getY() + friend.size.getY() < PanelScreen.y + PanelScreen.height - PanelScreen.secondEndY).forEach(friendPlayer -> friendPlayer.render(mouseX, mouseY));
            RenderUtil.releaseScissor();
        }
    }

    @Override
    public void click(int mouseX, int mouseY, int state) {
        super.click(mouseX, mouseY, state);
        if (selected() && state == 0) {
            clickingFrame = true;
        }
    }

    protected void renderPlayerHead(NetworkPlayerInfo networkPlayerInfo, int x, int y) {
        GlStateManager.pushAttrib();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Main.mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
        Gui.drawScaledCustomSizeModalRect(x, y, 8.0F, (float) 8, 8, 8, 21, 21, 64.0F, 64.0F);
        Gui.drawScaledCustomSizeModalRect(x, y, 40.0F, (float) 8, 8, 8, 21, 21, 64.0F, 64.0F);
        GlStateManager.disableBlend();
        GlStateManager.popAttrib();
    }

    protected class GuiPlayerComponent {
        protected final NetworkPlayerInfo networkPlayerInfo;
        protected Vector2D position, size;
        protected boolean friend;

        public GuiPlayerComponent(NetworkPlayerInfo networkPlayerInfo, Vector2D position, Vector2D size, boolean friend) {
            this.networkPlayerInfo = networkPlayerInfo;
            this.position = position;
            this.size = size;
            this.friend = friend;
        }

        public void render(int mouseX, int mouseY) {
            for (int i = 20; i <= 28; i = i + 2) {
                final float val = ((28 - i) / 2f);
                RenderUtil.drawRoundedRect(position.getX(), position.getY(), size.getX() + val, size.getY() + val, 10, new Color(0, 0, 0, i));
            }
            RenderUtil.drawRoundedRect(position.getX(), position.getY(), size.getX(), size.getY(), 10, PanelScreen.thirdBackgroundColor);
            RenderUtil.drawRoundedRect(position.getX() + 1, position.getY() + 1, size.getX() - 1, size.getY() - 1, 10, PanelScreen.secondBackgroundColor);
            renderPlayerHead(networkPlayerInfo, (int) (position.getX() + 5.0f), (int) (position.getY() + 2.0f));
            if (inside(mouseX, mouseY)) {
                RenderUtil.drawRoundedRect(position.getX() + 1, position.getY() + 1, size.getX() - 1, size.getY() - 1, 10, new Color(0, 0, 0, 30));
            }
            Main.fontManager.drawStringWithShadow(networkPlayerInfo.getGameProfile().getName(), new Vector2D(position.getX() + 30.0f, position.getY() + 4.0f), friend ? Color.CYAN.getRGB() : -1);
            RenderUtil.prepareScale(0.8);
            Main.fontManager.drawStringWithShadow(networkPlayerInfo.getResponseTime() + "ms", new Vector2D((position.getX() + 30.0f) / 0.8f, (position.getY() + 6.0f + Main.fontManager.getFontHeight()) / 0.8f), Color.GRAY.getRGB());
            RenderUtil.releaseScale();
        }

        public void click(int mouseX, int mouseY) {
            if (inside(mouseX, mouseY)) {
                final String name = networkPlayerInfo.getGameProfile().getName();
                if (friend) {
                    Main.friendManager.removeFriend(name);
                } else {
                    Main.friendManager.addFriend(name);
                }

            }
        }

        protected boolean inside(int mouseX, int mouseY) {
            return mouseX > position.getX() && mouseX < size.getX() && mouseY > position.getY() && mouseY < size.getY();
        }
    }
}
