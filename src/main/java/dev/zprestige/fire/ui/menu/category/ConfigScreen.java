package dev.zprestige.fire.ui.menu.category;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.ClickGui;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Timer;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigScreen extends AbstractCategory {
    protected final ArrayList<String> selectedList = new ArrayList<>();
    protected final ArrayList<LoadableConfig> loadableConfigs = new ArrayList<>();
    protected final Timer timer = new Timer();
    protected boolean typing, typingName, preserveKeybinds, clickingTick;
    protected String searchingString = "", savingString = "";
    protected LoadableConfig openedConfig = null;
    protected float openedConfigScrolling = 0.0f, lastOpenedComponentY = 0.0f, firstOpenedComponentY = 0.0f;

    public ConfigScreen(Vector2D position, Vector2D size) {
        super(null, position, size);
        reloadConfigs();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        RenderUtil.drawRect(new Vector2D(position.getX(), position.getY() - 2), new Vector2D(position.getX() + size.getX(), position.getY() + size.getY()), ClickGui.Instance.backgroundColor.GetColor().getRGB());
        RenderUtil.drawOutline(position.getX(), position.getY() - 2, position.getX() + size.getX(), position.getY() + size.getY(), new Color(0, 0, 0, 50), 1.0f);
        RenderUtil.drawRect(new Vector2D(position.getX() + 2, position.getY()), new Vector2D(position.getX() + size.getX() - 2, position.getY() + 1), ClickGui.Instance.color.GetColor().getRGB());
        RenderUtil.drawOutline(position.getX() + 2, position.getY() + 2, position.getX() + size.getX() / 2 - 2, position.getY() + 17, new Color(0, 0, 0, 50), 1.0f);
        if (searchingString.equals("")) {
            Main.fontManager.drawStringWithShadow("Search" + (typing ? getTypingIcon() : ""), new Vector2D(position.getX() + 4, position.getY() + 7.5f), Color.GRAY.getRGB());
        } else {
            Main.fontManager.drawStringWithShadow(searchingString + (typing ? getTypingIcon() : ""), new Vector2D(position.getX() + 4, position.getY() + 7.5f), -1);
        }
        RenderUtil.drawOutline(position.getX() + size.getX() / 2 + 2, position.getY() + 2, position.getX() + size.getX() - 2, position.getY() + 17, new Color(0, 0, 0, 50), 1.0f);
        RenderUtil.drawOutline(position.getX() + 2, position.getY() + 19, position.getX() + size.getX() - 2, position.getY() + size.getY() - 2, new Color(0, 0, 0, 50), 1.0f);
        RenderUtil.drawOutline(position.getX() + 4, position.getY() + 21, position.getX() + size.getX() / 2 - 2, position.getY() + size.getY() - 4, new Color(0, 0, 0, 50), 1.0f);
        RenderUtil.drawOutline(position.getX() + size.getX() / 2 + 2, position.getY() + 21, position.getX() + size.getX() - 4, position.getY() + size.getY() - 4, new Color(0, 0, 0, 50), 1.0f);

        scroll(mouseX, mouseY);
        float deltaY = position.getY() + 23;
        for (LoadableConfig loadableConfig : loadableConfigs) {
            if (loadableConfig.folder.toLowerCase().contains(searchingString.toLowerCase())) {
                loadableConfig.setPosition(new Vector2D(position.getX() + 6, deltaY));
                loadableConfig.render(mouseX, mouseY);
                deltaY += 17;
            }
        }
        if (openedConfig != null) {
            RenderUtil.prepareScissor((int) (position.getX()), (int) (position.getY() + 22), (int) (size.getX()), (int) size.getY() - 25);
            Main.fontManager.drawStringWithShadow(openedConfig.folder, new Vector2D(position.getX() + size.getX() / 2 + 4, position.getY() + 23 + openedConfigScrolling), (selectedList.contains(openedConfig.folder) || Main.configManager.isConfigTheSame(openedConfig.folder)) ? ClickGui.Instance.color.GetColor().getRGB() : -1);
            final float lelx = position.getX() + size.getX() / 2 + 4;
            if (clickingTick && mouseX > lelx && mouseX < lelx + Main.fontManager.getStringWidth(openedConfig.folder) && mouseY > position.getY() + 23 + openedConfigScrolling && mouseY < position.getY() + 23 + openedConfigScrolling + Main.fontManager.getFontHeight() + 3) {
                Main.configManager.load(openedConfig.folder, preserveKeybinds);
                selectedList.add(openedConfig.folder);
            }
            float deltaY2 = position.getY() + 23 + Main.fontManager.getFontHeight() + 3 + openedConfigScrolling;
            firstOpenedComponentY = deltaY2;
            final float mainFolder = deltaY2;
            for (File file : openedConfig.getCategoryFolders()) {
                if (file.getName().equals("Hud")){
                    continue;
                }
                Main.fontManager.drawStringWithShadow(file.getName(), new Vector2D(position.getX() + size.getX() / 2 + 8, deltaY2), (selectedList.contains(file.getName()) || Main.configManager.isCategoryTheSame(openedConfig.folder, Main.configManager.getCategoryByString(file.getName()))) ? ClickGui.Instance.color.GetColor().getRGB() : -1);
                final float textXrn = position.getX() + size.getX() / 2 + 8;
                if (clickingTick && mouseX > textXrn && mouseX < textXrn + Main.fontManager.getStringWidth(file.getName()) && mouseY > deltaY2 && mouseY < deltaY2 + Main.fontManager.getFontHeight() + 3) {
                    Main.configManager.loadSingleCategory(file, preserveKeybinds);
                    selectedList.add(file.getName());
                }
                deltaY2 += Main.fontManager.getFontHeight() + 3;
                final float currentY = deltaY2;
                for (File file1 : filesInFolder(file)) {
                    final float txtSize = position.getX() + size.getX() / 2 + 12;
                    Main.fontManager.drawStringWithShadow(file1.getName(), new Vector2D(position.getX() + size.getX() / 2 + 12, deltaY2), (selectedList.contains(file1.getName()) || Main.configManager.isConfigTheSame(openedConfig.folder, Main.configManager.getModuleByString(file1.getName()))) ? ClickGui.Instance.color.GetColor().getRGB() : -1);
                    if (clickingTick && mouseX > txtSize && mouseX < txtSize + Main.fontManager.getStringWidth(file1.getName()) && mouseY > deltaY2 && mouseY < deltaY2 + Main.fontManager.getFontHeight() + 3) {
                        Main.configManager.loadSingleModule(file1, preserveKeybinds);
                        selectedList.add(file1.getName());
                    }
                    deltaY2 += Main.fontManager.getFontHeight() + 3;
                    lastOpenedComponentY = deltaY2;
                }
                RenderUtil.drawRect(new Vector2D(position.getX() + size.getX() / 2 + 8, currentY), new Vector2D(position.getX() + size.getX() / 2 + 9, deltaY2 - 3), new Color(0, 0, 0, 50).getRGB());
                lastOpenedComponentY = deltaY2;
            }
            RenderUtil.drawRect(new Vector2D(position.getX() + size.getX() / 2 + 4, mainFolder), new Vector2D(position.getX() + size.getX() / 2 + 5, deltaY2 - 3), new Color(0, 0, 0, 50).getRGB());
            RenderUtil.releaseScissor();
        }
        RenderUtil.drawOutline(position.getX() + size.getX() / 2 + 13 + Main.fontManager.getStringWidth("Preserve"), position.getY() + 4, position.getX() + size.getX() - 70, position.getY() + 15, new Color(0, 0, 0, 50), 1.0f);
        if (insideCreate(mouseX, mouseY)) {
            RenderUtil.drawRect(new Vector2D(position.getX() + size.getX() - 36, position.getY() + 4), new Vector2D(position.getX() + size.getX() - 6, position.getY() + 15), new Color(0, 0, 0, 30).getRGB());
        }
        RenderUtil.drawOutline(position.getX() + size.getX() - 68, position.getY() + 4, position.getX() + size.getX() - 38, position.getY() + 15, new Color(0, 0, 0, 50), 1.0f);
        if (insideDelete(mouseX, mouseY)) {
            RenderUtil.drawRect(new Vector2D(position.getX() + size.getX() - 68, position.getY() + 4), new Vector2D(position.getX() + size.getX() - 38, position.getY() + 15), new Color(0, 0, 0, 30).getRGB());
        }
        RenderUtil.drawOutline(position.getX() + size.getX() - 36, position.getY() + 4, position.getX() + size.getX() - 6, position.getY() + 15, new Color(0, 0, 0, 50), 1.0f);
        RenderUtil.prepareScale(0.8);
        Main.fontManager.drawStringWithShadow("Delete", new Vector2D((position.getX() + size.getX() - 53 - Main.fontManager.getStringWidth("Delete") / 2f) / 0.8f, (position.getY() + 9.5f - Main.fontManager.getFontHeight() / 2f) / 0.8f), -1);
        Main.fontManager.drawStringWithShadow("Create", new Vector2D((position.getX() + size.getX() - 19 - Main.fontManager.getStringWidth("Create") / 2f) / 0.8f, (position.getY() + 9.5f - Main.fontManager.getFontHeight() / 2f) / 0.8f), -1);
        RenderUtil.releaseScale();
        RenderUtil.prepareScale(0.9);
        if (savingString.equals("")) {
            Main.fontManager.drawStringWithShadow("Config name" + (typingName ? getTypingIcon() : ""), new Vector2D((position.getX() + size.getX() / 2 + 15 + Main.fontManager.getStringWidth("Preserve")) / 0.9f, (position.getY() + 9.5f - Main.fontManager.getFontHeight() / 2f) / 0.9f), Color.GRAY.getRGB());
        } else {
            Main.fontManager.drawStringWithShadow(savingString + (typingName ? getTypingIcon() : ""), new Vector2D((position.getX() + size.getX() / 2 + 15 + Main.fontManager.getStringWidth("Preserve")) / 0.9f, (position.getY() + 9.5f - Main.fontManager.getFontHeight() / 2f) / 0.9f), -1);
        }
        RenderUtil.releaseScale();
        RenderUtil.drawOutline(position.getX() + size.getX() / 2 + 4, position.getY() + 4, position.getX() + size.getX() / 2 + 15, position.getY() + 15, new Color(0, 0, 0, 50), 1.0f);
        if (insidePreserveKeybinds(mouseX, mouseY)) {
            RenderUtil.drawRect(new Vector2D(position.getX() + size.getX() / 2 + 4, position.getY() + 4), new Vector2D(position.getX() + size.getX() / 2 + 15, position.getY() + 15), new Color(0, 0, 0, 30).getRGB());
        }
        RenderUtil.prepareScale(0.7);
        Main.fontManager.drawStringWithShadow("Preserve", new Vector2D((position.getX() + size.getX() / 2 + 17) / 0.7f, (position.getY() + 6.0f) / 0.7f), -1);
        Main.fontManager.drawStringWithShadow("Keybinds", new Vector2D((position.getX() + size.getX() / 2 + 17) / 0.7f, (position.getY() + 6.0f + Main.fontManager.getFontHeight()) / 0.7f), -1);
        RenderUtil.releaseScale();
        if (preserveKeybinds) {
            RenderUtil.drawCheckMark(position.getX() + size.getX() / 2 + 4, position.getY() + 3, 15, Color.WHITE.getRGB());
        }
        if (clickingTick) {
            clickingTick = false;
        }
    }

    protected void createConfig(String folder) {
        Main.configManager.save(folder);
        reloadConfigs();
    }

    protected void scroll(int mouseX, int mouseY) {
        if (openedConfig != null && insideOpenedConfig(mouseX, mouseY)) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0 && firstOpenedComponentY < position.getY() + size.getY() - 4 - Main.fontManager.getFontHeight() / 2) {
                openedConfigScrolling += 5;
            }
            if (wheel > 0 && lastOpenedComponentY > position.getY() + 23 + Main.fontManager.getFontHeight() + Main.fontManager.getFontHeight()) {
                openedConfigScrolling -= 5;
            }
        }
    }

    protected boolean insidePreserveKeybinds(int mouseX, int mouseY) {
        return mouseX > position.getX() + size.getX() / 2 + 4 && mouseX < position.getX() + size.getX() / 2 + 15 && mouseY > position.getY() + 4 && mouseY < position.getY() + 15;
    }

    protected boolean insideOpenedConfig(int mouseX, int mouseY) {
        return mouseX > position.getX() + size.getX() / 2 + 2 && mouseX < position.getX() + size.getX() - 4 && mouseY > position.getY() + 21 && mouseY < position.getY() + size.getY() - 4;
    }

    protected boolean insideCreate(int mouseX, int mouseY) {
        return mouseX > position.getX() + size.getX() - 36 && mouseX < position.getX() + size.getX() - 6 && mouseY > position.getY() + 4 && mouseY < position.getY() + 15;
    }

    protected boolean insideDelete(int mouseX, int mouseY) {
        return mouseX > position.getX() + size.getX() - 70 && mouseX < position.getX() + size.getX() - 38 && mouseY > position.getY() + 4 && mouseY < position.getY() + 15;
    }

    protected boolean insideCreateText(int mouseX, int mouseY) {
        return mouseX > position.getX() + size.getX() / 2 + 13 + Main.fontManager.getStringWidth("Preserve") && mouseX < position.getX() + size.getX() - 70 && mouseY > position.getY() + 4 && mouseY < position.getY() + 15;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void click(int mouseX, int mouseY, int state) {
        if (state == 0) {
            clickingTick = true;
            if (insideSearch(mouseX, mouseY)) {
                typing = !typing;
            }
            if (insidePreserveKeybinds(mouseX, mouseY)) {
                preserveKeybinds = !preserveKeybinds;
            }
            if (insideCreateText(mouseX, mouseY)) {
                typingName = !typingName;
            }
            if (insideCreate(mouseX, mouseY) && !savingString.equals("")) {
                createConfig(savingString);
            }
            if (insideDelete(mouseX, mouseY) && !savingString.equals("")) {
                final File file = new File(Main.configManager.getConfigFolder() + Main.configManager.getSeparator() + savingString);
                if (file.exists()) {
                    try {
                        filesInFolder(file).forEach(file1 -> {
                            filesInFolder(file1).forEach(File::delete);
                            file1.delete();
                        });
                        if (file.delete()) {
                            reloadConfigs();
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        loadableConfigs.forEach(loadableConfig -> loadableConfig.click(mouseX, mouseY, state));
    }

    @Override
    public void release(int mouseX, int mouseY, int state) {
    }

    @Override
    public void type(char typedChar, int keyCode) {
        if (typing) {
            searchingString = getTypingText(searchingString, typedChar, keyCode);
        }
        if (typingName) {
            savingString = getTypingText(savingString, typedChar, keyCode);
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
                typing = false;
                typingName = false;
                break;
            default:
                if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    newString = newString + typedChar;
                    break;
                }
        }
        return newString;
    }

    protected boolean insideSearch(int mouseX, int mouseY) {
        return mouseX > position.getX() + 2 && mouseX < position.getX() + size.getX() / 2 - 2 && mouseY > position.getY() + 2 && mouseY < position.getY() + 17;
    }

    protected void reloadConfigs() {
        loadableConfigs.clear();
        openedConfig = null;
        selectedList.clear();
        float deltaY = position.getY() + 22;
        for (File file : filesInFolder(new File(Main.fileManager.getDirectory() + File.separator + "Configs"))) {
            if (file.getName().equals("ActiveConfig.txt") || file.getName().equals("Friends.txt")){
                continue;
            }
            loadableConfigs.add(new LoadableConfig(file.getName(), new Vector2D(position.getX() + 6, deltaY), new Vector2D(size.getX() / 2 - 10, 15)));
            deltaY += 16;
        }
    }

    public String getTypingIcon() {
        if (timer.getTime(1000)) {
            timer.syncTime();
            return "";
        }
        if (timer.getTime(500)) {
            return "_";
        }
        return "";
    }

    protected ArrayList<File> filesInFolder(File file) {
        try {
            return Arrays.stream(Objects.requireNonNull(file.list())).map(file1 -> new File(file + File.separator + file1)).collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception ignored){
            return new ArrayList<>();
        }
    }

    protected class LoadableConfig {
        protected final String folder;
        protected final File file;
        protected Vector2D position, size;

        public LoadableConfig(String folder, Vector2D position, Vector2D size) {
            this.folder = folder;
            this.file = new File(Main.fileManager.getDirectory() + File.separator + "Configs" + File.separator + folder);
            this.position = position;
            this.size = size;
        }

        protected Vector2D[] getOpenBounds() {
            return new Vector2D[]{new Vector2D(position.getX() + size.getX() - 40, position.getY() + 2), new Vector2D(position.getX() + size.getX() - 2, position.getY() + size.getY() - 2)};
        }

        public void render(int mouseX, int mouseY) {
            RenderUtil.drawOutline(position.getX(), position.getY(), position.getX() + size.getX(), position.getY() + size.getY(), new Color(0, 0, 0, 50), 1.0f);
            final String active = Main.configManager.getActiveConfig();
            Main.fontManager.drawStringWithShadow(folder, new Vector2D(position.getX() + 2, position.getY() + size.getY() / 2), active != null && active.equals(folder) ? ClickGui.Instance.color.GetColor().getRGB() : -1);
            try {
                RenderUtil.prepareScale(0.6);
                Main.fontManager.drawStringWithShadow(new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss").format(Files.getLastModifiedTime(Paths.get(file.toURI())).toMillis()), new Vector2D((position.getX() + 4 + Main.fontManager.getStringWidth(folder)) / 0.6f, (position.getY() + size.getY() - Main.fontManager.getFontHeight()) / 0.6f), Color.GRAY.getRGB());
                RenderUtil.releaseScale();
            } catch (IOException ignored) {
            }
            final Vector2D[] vector2DS = getOpenBounds();
            RenderUtil.drawOutline(vector2DS[0].getX(), vector2DS[0].getY(), vector2DS[1].getX(), vector2DS[1].getY(), new Color(0, 0, 0, 50), 1.0f);
            if (insideOpenBounds(mouseX, mouseY)) {
                RenderUtil.drawRect(vector2DS[0], vector2DS[1], new Color(0, 0, 0, 30).getRGB());
            }
            RenderUtil.prepareScale(0.8);
            Main.fontManager.drawStringWithShadow("Open", new Vector2D((vector2DS[0].getX() + 21 - Main.fontManager.getStringWidth("Open") / 2) / 0.8f, (position.getY() + 2 + (size.getY() - 2) / 2 - Main.fontManager.getFontHeight() / 2) / 0.8f), -1);
            RenderUtil.releaseScale();
        }

        public void click(int mouseX, int mouseY, int state) {
            if (state == 0) {
                if (insideOpenBounds(mouseX, mouseY) && (openedConfig == null || !openedConfig.equals(this))) {
                    openedConfig = this;
                    openedConfigScrolling = 0.0f;
                    selectedList.clear();
                }
            }
        }

        protected boolean insideOpenBounds(int mouseX, int mouseY) {
            final Vector2D[] vector2DS = getOpenBounds();
            return mouseX > vector2DS[0].getX() && mouseX < vector2DS[1].getX() && mouseY > vector2DS[0].getY() && mouseY < vector2DS[1].getY();
        }

        public ArrayList<File> getCategoryFolders() {
            return filesInFolder(file);
        }

        public void setPosition(Vector2D position) {
            this.position = position;
        }
    }
}
