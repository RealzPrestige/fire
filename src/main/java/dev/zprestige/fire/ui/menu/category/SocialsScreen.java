package dev.zprestige.fire.ui.menu.category;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.ClickGui;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Timer;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

public class SocialsScreen extends AbstractCategory {
    protected final Timer timer = new Timer();
    protected boolean typingAll, typingFriends, clickingTick;
    protected float allScrolling = 0.0f, friendScrolling = 0.0f, minAllY = 0.0f, maxAllY = 0.0f, minFriendY = 0.0f, maxFriendY = 0.0f;
    protected String allString = "", friendsString = "";

    public SocialsScreen(Vector2D position, Vector2D size) {
        super(null, position, size);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        int wheel = Mouse.getDWheel();
        if (wheel < 0){
            if (insideAllBox(mouseX, mouseY)&& minAllY + 37.5 < position.getY() + size.getY() - 4) {
                allScrolling += 12.5;
            }
            if (insideFriendsBox(mouseX, mouseY) && minFriendY + 37.5 < position.getY() + size.getY() - 4){
                friendScrolling += 12.5f;
            }
        }
        if (wheel > 0){
            if (insideAllBox(mouseX, mouseY) && maxAllY - 25 > position.getY() + 23) {
                allScrolling -= 12.5;
            }
            if (insideFriendsBox(mouseX, mouseY) && maxFriendY - 25 > position.getY() + 23){
                friendScrolling -= 12.5f;
            }
        }
        RenderUtil.drawRect(new Vector2D(position.getX(), position.getY() - 2), new Vector2D(position.getX() + size.getX(), position.getY() + size.getY()), ClickGui.Instance.backgroundColor.GetColor().getRGB());
        RenderUtil.drawOutline(position.getX(), position.getY() - 2, position.getX() + size.getX(), position.getY() + size.getY(), new Color(0, 0, 0, 50), 1.0f);
        RenderUtil.drawRect(new Vector2D(position.getX() + 2, position.getY()), new Vector2D(position.getX() + size.getX() - 2, position.getY() + 1), ClickGui.Instance.color.GetColor().getRGB());
        RenderUtil.drawOutline(position.getX() + 2, position.getY() + 2, position.getX() + size.getX() / 2 - 2, position.getY() + 17, new Color(0, 0, 0, 50), 1.0f);

        RenderUtil.drawOutline(position.getX() + size.getX() / 2 + 2, position.getY() + 2, position.getX() + size.getX() - 2, position.getY() + 17, new Color(0, 0, 0, 50), 1.0f);
        RenderUtil.drawOutline(position.getX() + 2, position.getY() + 19, position.getX() + size.getX() - 2, position.getY() + size.getY() - 2, new Color(0, 0, 0, 50), 1.0f);
        RenderUtil.drawOutline(position.getX() + 4, position.getY() + 21, position.getX() + size.getX() / 2 - 2, position.getY() + size.getY() - 4, new Color(0, 0, 0, 50), 1.0f);
        RenderUtil.drawOutline(position.getX() + size.getX() / 2 + 2, position.getY() + 21, position.getX() + size.getX() - 4, position.getY() + size.getY() - 4, new Color(0, 0, 0, 50), 1.0f);

        if (allString.equals("")) {
            Main.fontManager.drawStringWithShadow("Search" + (typingAll ? getTypingIcon() : ""), new Vector2D(position.getX() + 4, position.getY() + 7.5f), Color.GRAY.getRGB());
        } else {
            Main.fontManager.drawStringWithShadow(allString + (typingAll ? getTypingIcon() : ""), new Vector2D(position.getX() + 4, position.getY() + 7.5f), -1);
        }

        if (friendsString.equals("")) {
            Main.fontManager.drawStringWithShadow("Search" + (typingFriends ? getTypingIcon() : ""), new Vector2D(position.getX() + size.getX() / 2 + 4, position.getY() + 7.5f), Color.GRAY.getRGB());
        } else {
            Main.fontManager.drawStringWithShadow(friendsString + (typingFriends ? getTypingIcon() : ""), new Vector2D(position.getX() + size.getX() / 2 + 4, position.getY() + 7.5f), -1);
        }
        final ArrayList<GuiPlayerComponent> allPlayers = new ArrayList<>();
        final ArrayList<GuiPlayerComponent> friendPlayers = new ArrayList<>();
        float allY = position.getY() + 23 + (allString.equals("") ? allScrolling : 0 );
        float friendY = position.getY() + 23 + (friendsString.equals("") ? friendScrolling : 0);
        minAllY = allY;
        minFriendY = friendY;
        for (NetworkPlayerInfo networkPlayerInfo : Main.mc.player.connection.getPlayerInfoMap()) {
            final String name = networkPlayerInfo.getGameProfile().getName();
            if (Main.friendManager.isFriend(name)) {
                if (name.toLowerCase().contains(friendsString.toLowerCase())) {
                    friendPlayers.add(new GuiPlayerComponent(networkPlayerInfo, new Vector2D(position.getX() + size.getX() / 2 + 4, friendY), new Vector2D(position.getX() + size.getX() - 6, friendY + 25), true));
                    friendY += 26;
                }
            } else if (name.toLowerCase().contains(allString.toLowerCase())) {
                allPlayers.add(new GuiPlayerComponent(networkPlayerInfo, new Vector2D(position.getX() + 6, allY), new Vector2D(position.getX() + size.getX() / 2 - 4, allY + 25), false));
                allY += 26;
            }
        }
        maxAllY = allY;
        maxFriendY = friendY;
        RenderUtil.prepareScissor((int) (position.getX() + 4), (int) (position.getY() + 23), (int) (size.getX() - 8), (int) (size.getY() - 25));
        allPlayers.stream().filter(guiPlayerComponent -> guiPlayerComponent.size.getY() > position.getY() && guiPlayerComponent.position.getY() < position.getY() + size.getY()).forEach(guiPlayerComponent -> guiPlayerComponent.render(mouseX, mouseY));
        friendPlayers.stream().filter(guiPlayerComponent -> guiPlayerComponent.size.getY() > position.getY() && guiPlayerComponent.position.getY() < position.getY() + size.getY()).forEach(guiPlayerComponent -> guiPlayerComponent.render(mouseX, mouseY));
       RenderUtil.releaseScissor();
        if (clickingTick) {
            allPlayers.stream().filter(guiPlayerComponent -> guiPlayerComponent.size.getY() > position.getY() + 23 && guiPlayerComponent.position.getY() < position.getY() + size.getY()).forEach(guiPlayerComponent -> guiPlayerComponent.click(mouseX, mouseY));
            friendPlayers.stream().filter(guiPlayerComponent -> guiPlayerComponent.size.getY() > position.getY() + 23 && guiPlayerComponent.position.getY() < position.getY() + size.getY()).forEach(guiPlayerComponent -> guiPlayerComponent.click(mouseX, mouseY));
            clickingTick = false;
        }
    }

    protected boolean insideAllSearch(int mouseX, int mouseY) {
        return mouseX > position.getX() + 2 && mouseX < position.getX() + size.getX() / 2 - 2 && mouseY > position.getY() + 2 && mouseY < position.getY() + 17;
    }

    protected boolean insideFriendsSearch(int mouseX, int mouseY) {
        return mouseX > position.getX() + size.getX() / 2 + 2 && mouseX < position.getX() + size.getX() - 2 && mouseY > position.getY() + 2 && mouseY < position.getY() + 17;
    }

    protected boolean insideAllBox(int mouseX, int mouseY) {
        return mouseX > position.getX() + 2 && mouseX < position.getX() + size.getX() / 2 - 2 && mouseY > position.getY() + 21 && mouseY < position.getY() + size.getY() - 4;
    }

    protected boolean insideFriendsBox(int mouseX, int mouseY) {
        return mouseX > position.getX() + size.getX() / 2 + 2 && mouseX < position.getX() + size.getX() - 2 && mouseY > position.getY() + 21 && mouseY < position.getY() + size.getY() - 4;
    }
    @Override
    public void click(int mouseX, int mouseY, int state) {
        if (state == 0) {
            if (insideAllSearch(mouseX, mouseY)) {
                typingAll = !typingAll;
            }
            if (insideFriendsSearch(mouseX, mouseY)) {
                typingFriends = !typingFriends;
            }
            clickingTick = true;
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int state) {

    }

    @Override
    public void type(char typedChar, int keyCode) {
        if (typingAll) {
            allString = getTypingText(allString, typedChar, keyCode);
        }
        if (typingFriends) {
            friendsString = getTypingText(friendsString, typedChar, keyCode);
        }
    }

    protected String getTypingText(String string, char typedChar, int keyCode) {
        String newString = string;
        switch (keyCode) {
            case 14:
                if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                    newString = "";
                    break;
                }
                if (newString.length() > 0) {
                    newString = newString.substring(0, newString.length() - 1);
                    break;
                }
            case 27:
            case 28:
                typingAll = false;
                typingFriends = false;
                break;
            default:
                if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    newString = newString + typedChar;
                    break;
                }
        }
        return newString;
    }

    protected String getTypingIcon() {
        if (timer.getTime(1000)) {
            timer.syncTime();
            return "";
        }
        if (timer.getTime(500)) {
            return "_";
        }
        return "";
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
            RenderUtil.drawOutline(position.getX(), position.getY(), size.getX(), size.getY(), new Color(0, 0, 0, 50), 1.0f);
            renderPlayerHead(networkPlayerInfo, (int) (position.getX() + 2.0f), (int) (position.getY() + 2.0f));
            if (inside(mouseX, mouseY)) {
                RenderUtil.drawRect(position, size, new Color(0, 0, 0, 30).getRGB());
            }
            Main.fontManager.drawStringWithShadow(networkPlayerInfo.getGameProfile().getName(), new Vector2D(position.getX() + 25.0f, position.getY() + 4.0f), friend ? Color.CYAN.getRGB() : -1);
            RenderUtil.prepareScale(0.8);
            Main.fontManager.drawStringWithShadow(networkPlayerInfo.getResponseTime() + "ms", new Vector2D((position.getX() + 25.0f) / 0.8f, (position.getY() + 6.0f + Main.fontManager.getFontHeight()) / 0.8f), Color.GRAY.getRGB());
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
