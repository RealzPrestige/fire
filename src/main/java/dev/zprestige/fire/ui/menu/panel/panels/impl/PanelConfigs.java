package dev.zprestige.fire.ui.menu.panel.panels.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.panel.Panel;
import dev.zprestige.fire.ui.menu.panel.PanelScreen;
import dev.zprestige.fire.ui.menu.panel.panels.PanelDrawable;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PanelConfigs extends PanelDrawable {
    protected final ArrayList<Config> configs = new ArrayList<>();
    protected final Timer deletingTimer = new Timer(), backspaceTimer = new Timer();
    protected boolean clickingFrame;
    protected float width, height, alpha, leftScroll, rightScroll;
    protected Clickable create;
    protected boolean searching, preserveKeybinds;
    protected String searchString = "";
    protected Config activeConfig;

    public PanelConfigs(final float x, final float y, final float width, final float height) {
        super("Configs", width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        create = new Clickable("Create", PanelScreen.x + PanelScreen.secondStart + 4, PanelScreen.y + PanelScreen.secondStartY + 4, 25, 17) {
            @Override
            public void action() {
                if (!searchString.equals("")) {
                    Main.configManager.save(searchString);
                    loadFiles();
                }
            }
        };
        loadFiles();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
        if (selected()) {
            int mouse = Mouse.getDWheel();
            if (mouse < 0) {
                if (insideLeft(mouseX, mouseY)) {
                    leftScroll -= 10;
                }
                if (insideRight(mouseX, mouseY)) {
                    rightScroll -= 10;
                }
            } else if (mouse > 0) {
                if (insideLeft(mouseX, mouseY)) {
                    leftScroll += 10;
                }
                if (insideRight(mouseX, mouseY)) {
                    rightScroll += 10;
                }
            }
            for (int i = 20; i <= 28; i = i + 2) {
                final float val = ((28 - i) / 2f);
                RenderUtil.drawRoundedRect(PanelScreen.x + PanelScreen.secondStart + 2, PanelScreen.y + PanelScreen.secondStartY + 2, PanelScreen.x + PanelScreen.secondStart - 2 + PanelScreen.secondWidth - PanelScreen.secondEndY + val, PanelScreen.y + PanelScreen.secondStartY + 23 + val, 10, new Color(0, 0, 0, i));
            }
            RenderUtil.drawRoundedRect(PanelScreen.x + PanelScreen.secondStart + 2, PanelScreen.y + PanelScreen.secondStartY + 2, PanelScreen.x + PanelScreen.secondStart - 2 + PanelScreen.secondWidth - PanelScreen.secondEndY, PanelScreen.y + PanelScreen.secondStartY + 23, 10, PanelScreen.thirdBackgroundColor);
            RenderUtil.drawRoundedRect(PanelScreen.x + PanelScreen.secondStart + 3, PanelScreen.y + PanelScreen.secondStartY + 3, PanelScreen.x + PanelScreen.secondStart - 3 + PanelScreen.secondWidth - PanelScreen.secondEndY, PanelScreen.y + PanelScreen.secondStartY + 22, 10, PanelScreen.secondBackgroundColor);
            final float center = PanelScreen.x + PanelScreen.secondStart + (PanelScreen.secondWidth - PanelScreen.secondEndY) / 2.0f;
            RenderUtil.drawRoundedRect(center - 0.5f, PanelScreen.y + PanelScreen.secondStartY + 25, center + 0.5f, PanelScreen.y + PanelScreen.height - PanelScreen.secondEndY - 2.0f, 1, PanelScreen.thirdBackgroundColor);
            RenderUtil.prepareScissor(0, (int) (PanelScreen.y + PanelScreen.secondStartY + 25), 1000, (int) (PanelScreen.height - PanelScreen.secondStartY - 26 - PanelScreen.secondEndY));
            float deltaY = PanelScreen.y + PanelScreen.secondStartY + 25 + leftScroll;
            for (Config config : new ArrayList<>(configs)) {
                config.x = PanelScreen.x + PanelScreen.secondStart + 5;
                config.y = deltaY;
                config.render(mouseX, mouseY, clickingFrame);
                deltaY += 25;
            }
            RenderUtil.releaseScissor();
            for (int i = 20; i <= 28; i = i + 2) {
                final float val = ((28 - i) / 2f);
                RenderUtil.drawRoundedRect(PanelScreen.x + PanelScreen.secondStart + 31, PanelScreen.y + PanelScreen.secondStartY + 4, PanelScreen.x + PanelScreen.secondStart + 131 + val, PanelScreen.y + PanelScreen.secondStartY + 21 + val, 10, new Color(0, 0, 0, i));
            }
            RenderUtil.drawRoundedRect(PanelScreen.x + PanelScreen.secondStart + 31, PanelScreen.y + PanelScreen.secondStartY + 4, PanelScreen.x + PanelScreen.secondStart + 131, PanelScreen.y + PanelScreen.secondStartY + 21, 10, PanelScreen.thirdBackgroundColor);
            RenderUtil.drawRoundedRect(PanelScreen.x + PanelScreen.secondStart + 32, PanelScreen.y + PanelScreen.secondStartY + 5, PanelScreen.x + PanelScreen.secondStart + 130, PanelScreen.y + PanelScreen.secondStartY + 20, 10, PanelScreen.secondBackgroundColor);
            if (!Keyboard.isKeyDown(Keyboard.KEY_BACK)) {
                backspaceTimer.syncTime();
            }
            if (searching && Keyboard.isKeyDown(Keyboard.KEY_BACK) && deletingTimer.getTime(100000 / backspaceTimer.nanoTime())) {
                if (searchString.length() > 0) {
                    searchString = searchString.substring(0, searchString.length() - 1);
                }
                deletingTimer.syncTime();
            }
            RenderUtil.prepareScale(0.65f);
            RenderUtil.prepareScissor((int) (PanelScreen.x + PanelScreen.secondStart + 30), (int) (PanelScreen.y + PanelScreen.secondStartY + 5), 99, 15);
            if (!searchString.equals("")) {
                Main.fontManager.drawStringWithShadow(searchString + (searching ? getTypingIcon() : ""), new Vector2D((PanelScreen.x + PanelScreen.secondStart + 33) / 0.65f, (PanelScreen.y + PanelScreen.secondStartY + 11.5f - (Main.fontManager.getFontHeight() * 0.65f) / 2.0f) / 0.65f), new Color(1.0f, 1.0f, 1.0f, 1.0f).getRGB());
            } else {
                Main.fontManager.drawStringWithShadow("Config" + (searching ? getTypingIcon() : getDots()), new Vector2D((PanelScreen.x + PanelScreen.secondStart + 33) / 0.65f, (PanelScreen.y + PanelScreen.secondStartY + 11.5f - (Main.fontManager.getFontHeight() * 0.65f) / 2.0f) / 0.65f), new Color(1.0f, 1.0f, 1.0f, 0.6f).getRGB());
            }
            RenderUtil.releaseScissor();
            RenderUtil.releaseScale();
            for (int i = 20; i <= 28; i = i + 2) {
                final float val = ((28 - i) / 2f);
                RenderUtil.drawRoundedRect(PanelScreen.x + PanelScreen.secondStart + PanelScreen.secondWidth - PanelScreen.secondEndY - 19, PanelScreen.y + PanelScreen.secondStartY + 5, PanelScreen.x + PanelScreen.secondStart + PanelScreen.secondWidth - PanelScreen.secondEndY - 4 + val, PanelScreen.y + PanelScreen.secondStartY + 20 + val, 9, new Color(0, 0, 0, i));
            }
            RenderUtil.drawRoundedRect(PanelScreen.x + PanelScreen.secondStart + PanelScreen.secondWidth - PanelScreen.secondEndY - 19, PanelScreen.y + PanelScreen.secondStartY + 5, PanelScreen.x + PanelScreen.secondStart + PanelScreen.secondWidth - PanelScreen.secondEndY - 4, PanelScreen.y + PanelScreen.secondStartY + 20, 9, PanelScreen.thirdBackgroundColor);
            RenderUtil.drawRoundedRect(PanelScreen.x + PanelScreen.secondStart + PanelScreen.secondWidth - PanelScreen.secondEndY - 18, PanelScreen.y + PanelScreen.secondStartY + 6, PanelScreen.x + PanelScreen.secondStart + PanelScreen.secondWidth - PanelScreen.secondEndY - 5, PanelScreen.y + PanelScreen.secondStartY + 19, 9, PanelScreen.secondBackgroundColor);
            if (preserveKeybinds) {
                RenderUtil.drawCheckMark(PanelScreen.x + PanelScreen.secondStart + PanelScreen.secondWidth - PanelScreen.secondEndY - 17, PanelScreen.y + PanelScreen.secondStartY + 6, 15, Color.WHITE.getRGB());
            }
            RenderUtil.prepareScale(0.63f);
            Main.fontManager.drawStringWithShadow("Preserve", new Vector2D((PanelScreen.x + PanelScreen.secondStart + PanelScreen.secondWidth - PanelScreen.secondEndY - 20 - Main.fontManager.getStringWidth("Preserve") * 0.63f) / 0.63f, (PanelScreen.y + PanelScreen.secondStartY + 9) / 0.63f), -1);
            Main.fontManager.drawStringWithShadow("Keybinds", new Vector2D((PanelScreen.x + PanelScreen.secondStart + PanelScreen.secondWidth - PanelScreen.secondEndY - 20 - Main.fontManager.getStringWidth("Keybinds") * 0.63f) / 0.63f, (PanelScreen.y + PanelScreen.secondStartY + 11 + (Main.fontManager.getFontHeight() * 0.63f)) / 0.63f), -1);
            RenderUtil.releaseScale();
            create.setX(PanelScreen.x + PanelScreen.secondStart + 4);
            create.setY(PanelScreen.y + PanelScreen.secondStartY + 4);
            if (clickingFrame) {
                create.click(mouseX, mouseY, 0);
            }
            create.render(mouseX, mouseY);
            if (activeConfig != null) {
                RenderUtil.prepareScissor(0, (int) (PanelScreen.y + PanelScreen.secondStartY + 25), 1000, (int) (PanelScreen.height - PanelScreen.secondStartY - 26 - PanelScreen.secondEndY));
                float delta1 = PanelScreen.y + PanelScreen.secondStartY + 26 + rightScroll;
                final ClickableText configName = new ClickableText(activeConfig.folder, center + 5, delta1) {
                    @Override
                    protected void action() {
                        Main.configManager.load(activeConfig.folder, preserveKeybinds);
                        final Panel panel = (Panel) Main.moduleManager.getModuleByClass(Panel.class);
                        if (!panel.isEnabled()) {
                            panel.enableModule();
                        }
                    }
                };
                if (clickingFrame) {
                    configName.click(mouseX, mouseY, 1.0f);
                }
                configName.render(mouseX, mouseY, 1.0f);
                delta1 += Main.fontManager.getFontHeight() + 3;
                for (final File file : filesInFolder(new File(Main.configManager.getConfigFolder() + File.separator + activeConfig.folder))) {
                    final ClickableText configCategory = new ClickableText(file.getName(), (center + 7) / 0.93f, delta1 / 0.93f) {
                        @Override
                        protected void action() {
                            Main.configManager.loadSingleCategory(file, preserveKeybinds);
                            final Panel panel = (Panel) Main.moduleManager.getModuleByClass(Panel.class);
                            if (!panel.isEnabled()) {
                                panel.enableModule();
                            }
                        }
                    };
                    if (clickingFrame) {
                        configCategory.click(mouseX, mouseY, 0.93f);
                    }
                    RenderUtil.prepareScale(0.93f);
                    configCategory.render(mouseX, mouseY, 0.93f);
                    RenderUtil.releaseScale();
                    delta1 += Main.fontManager.getFontHeight() * 0.93f + 3;
                    for (final File file1 : filesInFolder(file)) {
                        final ClickableText configModule = new ClickableText(file1.getName().replace(".txt", ""), (center + 9) / 0.83f, delta1 / 0.83f) {
                            @Override
                            protected void action() {
                                Main.configManager.loadSingleModule(file1, preserveKeybinds);
                                final Panel panel = (Panel) Main.moduleManager.getModuleByClass(Panel.class);
                                if (!panel.isEnabled()) {
                                    panel.enableModule();
                                }
                            }
                        };
                        if (clickingFrame) {
                            configModule.click(mouseX, mouseY, 0.83f);
                        }
                        RenderUtil.prepareScale(0.83f);
                        configModule.render(mouseX, mouseY, 0.83f);
                        RenderUtil.releaseScale();
                        delta1 += Main.fontManager.getFontHeight() * 0.83f + 3;
                    }
                }
                RenderUtil.releaseScissor();
            }
            clickingFrame = false;
        }
    }

    @Override
    public void click(int mouseX, int mouseY, int state) {
        super.click(mouseX, mouseY, state);
        if (state == 0) {
            if (insideSearch(mouseX, mouseY)) {
                searching = !searching;
            }
            if (insidePreserve(mouseX, mouseY)) {
                preserveKeybinds = !preserveKeybinds;
            }
            clickingFrame = true;
        }
    }

    @Override
    public void type(char typedChar, int keyCode) {
        if (searching) {
            searchString = getTypingText(searchString, typedChar, keyCode);
        }
    }

    protected boolean insideLeft(int mouseX, int mouseY) {
        return mouseX > PanelScreen.x + PanelScreen.secondStart && mouseX < PanelScreen.x + PanelScreen.secondStart + (PanelScreen.secondWidth - PanelScreen.secondEndY) / 2.0f && mouseY > PanelScreen.y + PanelScreen.secondStartY && mouseY < PanelScreen.y + PanelScreen.height - PanelScreen.secondEndY;
    }

    protected boolean insideRight(int mouseX, int mouseY) {
        return mouseX > PanelScreen.x + PanelScreen.secondStart + (PanelScreen.secondWidth - PanelScreen.secondEndY) / 2.0f && mouseX < PanelScreen.x + PanelScreen.width - PanelScreen.secondEndY && mouseY > PanelScreen.y + PanelScreen.secondStartY && mouseY < PanelScreen.y + PanelScreen.height - PanelScreen.secondEndY;
    }

    protected boolean insidePreserve(int mouseX, int mouseY) {
        return mouseX > PanelScreen.x + PanelScreen.secondStart + PanelScreen.secondWidth - PanelScreen.secondEndY - 18 && mouseX < PanelScreen.x + PanelScreen.secondStart + PanelScreen.secondWidth - PanelScreen.secondEndY - 5 && mouseY > PanelScreen.y + PanelScreen.secondStartY + 6 && mouseY < PanelScreen.y + PanelScreen.secondStartY + 19;
    }

    protected String getTypingText(String string, char typedChar, int keyCode) {
        String newString = string;
        switch (keyCode) {
            case 14:
                if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                    newString = "";
                }
                if (newString.length() > 0) {
                    newString = newString.substring(0, newString.length() - 1);
                    deletingTimer.syncTime();
                }
                break;
            case 27:
            case 28:
                searching = false;
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
        return mouseX > PanelScreen.x + PanelScreen.secondStart + 31 && mouseX < PanelScreen.x + PanelScreen.secondStart + 131 && mouseY > PanelScreen.y + PanelScreen.secondStartY + 4 && mouseY < PanelScreen.y + PanelScreen.secondStartY + 21;
    }

    protected final Timer typeTimer = new Timer();

    public String getTypingIcon() {
        if (typeTimer.getTime(1000)) {
            typeTimer.syncTime();
            return "";
        }
        if (typeTimer.getTime(500)) {
            return "_";
        }
        return "";
    }

    protected final Timer timer = new Timer();

    protected String getDots() {
        if (timer.getTime(1500)) {
            timer.syncTime();
        }
        if (timer.getTime(1000)) {
            return "...";
        }
        if (timer.getTime(500)) {
            return "..";
        }
        return ".";
    }


    protected void loadFiles() {
        configs.clear();
        final float center = (PanelScreen.secondWidth - PanelScreen.secondEndY) / 2.0f;
        float deltaY = PanelScreen.y + PanelScreen.secondStartY + 25;
        for (File file : filesInFolder(new File(Main.fileManager.getDirectory() + File.separator + "Configs"))) {
            if (!file.getName().equals("ActiveConfig.txt") && !file.getName().equals("Friends.txt") && !file.getName().equals("Prefix.txt")) {
                configs.add(new Config(file.getName(), PanelScreen.x + PanelScreen.secondStart + 5, deltaY, center - 10, 20));
                deltaY += 25;
            }
        }
    }

    protected ArrayList<File> filesInFolder(File file) {
        try {
            return Arrays.stream(Objects.requireNonNull(file.list())).map(file1 -> new File(file + File.separator + file1)).collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
    }

    protected static class ClickableText {
        protected final String string;
        protected float x, y;

        public ClickableText(final String string, final float x, final float y) {
            this.string = string;
            this.x = x;
            this.y = y;
        }

        public void render(int mouseX, int mouseY, float scale) {
            Main.fontManager.drawStringWithShadow(string, new Vector2D(x, y), inside(mouseX, mouseY, scale) ? new Color(1.0f, 1.0f, 1.0f, 0.6f).getRGB() : Color.WHITE.getRGB());
        }

        protected void action() {
        }

        public void click(int mouseX, int mouseY, float scale) {
            if (inside(mouseX, mouseY, scale)) {
                action();
            }
        }

        protected boolean inside(int mouseX, int mouseY, float scale) {
            return mouseX > x * scale && mouseX < (x + Main.fontManager.getStringWidth(string)) * scale && mouseY > y * scale && mouseY < (y + Main.fontManager.getFontHeight() / scale) * scale;
        }

        public void setX(float x) {
            this.x = x;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected class Config {
        protected final String folder;
        protected final File file;
        protected float x, y, width, height;
        protected final Clickable save, delete, open;

        public Config(final String folder, final float x, final float y, final float width, final float height) {
            this.folder = folder;
            this.file = new File(Main.fileManager.getDirectory() + File.separator + "Configs" + File.separator + folder);
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            delete = new Clickable("Delete", x + width - 28, y + 4, 25, height - 8) {
                @Override
                public void action() {
                    if (file.exists()) {
                        try {
                            filesInFolder(file).forEach(file1 -> {
                                filesInFolder(file1).forEach(File::delete);
                                file1.delete();
                            });
                            if (file.delete()) {
                                loadFiles();
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            };
            save = new Clickable("Save", x + width - 56, y + 4, 25, height - 8) {
                @Override
                public void action() {
                    Main.configManager.save(folder);
                    loadFiles();
                }
            };
            open = new Clickable("Open", x + width - 84, y + 4, 25, height - 8);
        }

        public void render(int mouseX, int mouseY, boolean clickingFrame) {
            for (int i = 20; i <= 28; i = i + 2) {
                final float val = ((28 - i) / 2f);
                RenderUtil.drawRoundedRect(x, y, x + width + val, y + height + val, 10, new Color(0, 0, 0, i));
            }
            RenderUtil.drawRoundedRect(x, y, x + width, y + height, 10, PanelScreen.thirdBackgroundColor);
            RenderUtil.drawRoundedRect(x + 1, y + 1, x + width - 1, y + height - 1, 10, PanelScreen.secondBackgroundColor);
            if (inside(mouseX, mouseY)) {
                RenderUtil.drawRoundedRect(x + 1, y + 1, x + width - 1, y + height - 1, 10, new Color(0, 0, 0, 30));
            }
            Main.fontManager.drawStringWithShadow(folder, new Vector2D(x + 2, y + 3), Main.configManager.getActiveConfig().replace("\"", "").equals(folder) ? PanelScreen.PANEL.color.GetColor().getRGB() : -1);
            RenderUtil.prepareScale(0.63f);
            try {
                final String string = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss").format(Files.getLastModifiedTime(Paths.get(file.toURI())).toMillis());
                Main.fontManager.drawStringWithShadow(string, new Vector2D((x + 2) / 0.63f, (y + 3 + Main.fontManager.getFontHeight() / 0.63f) / 0.63f), new Color(1.0f, 1.0f, 1.0f, 0.6f).getRGB());
            } catch (IOException ignored) {
            }
            RenderUtil.releaseScale();
            delete.setX(x + width - 28);
            delete.setY(y + 4);
            save.setX(x + width - 56);
            save.setY(y + 4);
            open.setX(x + width - 84);
            open.setY(y + 4);
            if (clickingFrame) {
                delete.click(mouseX, mouseY, 0);
                save.click(mouseX, mouseY, 0);
                if (open.inside(mouseX, mouseY) && (activeConfig == null || !activeConfig.equals(this))) {
                    activeConfig = this;
                }
            }
            open.render(mouseX, mouseY);
            delete.render(mouseX, mouseY);
            save.render(mouseX, mouseY);
        }

        protected boolean inside(int mouseX, int mouseY) {
            return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
        }
    }

    public class Clickable {
        protected String displayString;
        protected float x, y, width, height;
        protected float[] col = new float[]{1, 1, 1};

        public Clickable(final String displayString, final float x, final float y, final float width, final float height) {
            this.displayString = displayString;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public void action() {
        }

        public void click(final int mouseX, final int mouseY, final int state) {
            if (inside(mouseX, mouseY) && state == 0) {
                action();
            }
        }

        public void render(final int mouseX, final int mouseY) {
            setupCol(mouseX, mouseY);
            for (int i = 20; i <= 28; i = i + 2) {
                final float val = ((28 - i) / 2f);
                RenderUtil.drawRoundedRect(x, y, x + width + val, y + height + val, 7, new Color(0, 0, 0, i));
            }
            RenderUtil.drawRoundedRect(x, y, x + width, y + height, 8, PanelScreen.thirdBackgroundColor);
            RenderUtil.drawRoundedRect(x + 1, y + 1, x + width - 1, y + height - 1, 8, PanelScreen.secondBackgroundColor);
            RenderUtil.prepareScale(0.65f);
            Main.fontManager.drawStringWithShadow(displayString, new Vector2D((x + (width / 2f) - (Main.fontManager.getStringWidth(displayString) * 0.65f) / 2.0f) / 0.65f, (y + 7.5f - Main.fontManager.getFontHeight() / 2.0f) / 0.65f), new Color(col[0], col[1], col[2], 1.0f).getRGB());
            RenderUtil.releaseScale();
        }

        protected boolean inside(final int mouseX, final int mouseY) {
            return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
        }

        protected void setupCol(final int mouseX, final int mouseY) {
            if (inside(mouseX, mouseY)) {
                IntStream.range(0, 3).forEach(i -> col[i] = normalizeNumber(col[i], 0.6f, PanelScreen.animationFactor() * 10));
            } else {
                IntStream.range(0, 3).forEach(i -> col[i] = normalizeNumber(col[i], 1.0f, PanelScreen.animationFactor() * 10));
            }
        }

        public void setY(float y) {
            this.y = y;
        }

        public void setX(float x) {
            this.x = x;
        }
    }
}
