package dev.zprestige.fire.manager.configmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.Category;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.Setting;
import dev.zprestige.fire.settings.impl.*;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class ConfigManager {
    protected final File configFolder = new File(Main.fileManager.getDirectory() + File.separator + "Configs");
    protected final String separator = File.separator;

    public ConfigManager() {
        loadActiveConfig();
        loadSavedFriends();
        loadPrefix();
    }

    public void savePrefix() {
        final File file = Main.fileManager.registerFileAndCreate(configFolder + separator + "Prefix.txt");
        final BufferedWriter bufferedWriter = Main.fileManager.createBufferedWriter(file);
        Main.fileManager.writeLine(bufferedWriter, "\"" + Main.commandManager.getPrefix() + "\"");
        Main.fileManager.closeBufferedWriter(bufferedWriter);
    }

    public void loadPrefix() {
        final File file = Main.fileManager.registerFileAndCreate(configFolder + separator + "Prefix.txt");
        if (!file.exists()) {
            return;
        }
        final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(file);
        try {
            final String line = bufferedReader.readLine();
            if (line != null && !line.equalsIgnoreCase("")) {
                Main.commandManager.setPrefix(line.replace("\"", ""));
            }
        } catch (IOException ignored) {
        }
        Main.fileManager.closeBufferedReader(bufferedReader);
    }

    public void loadActiveConfig() {
        final String active = getActiveConfig();
        if (active == null || active.equals("")) {
            return;
        }
        load(active.replace("\"", ""), false);
    }

    public String getActiveConfig() {
        final File file = new File(configFolder + separator + "ActiveConfig.txt");
        if (!file.exists()) {
            return "";
        }
        final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(file);
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            return "";
        }
    }

    public void loadSavedFriends() {
        final File file = Main.fileManager.registerFileAndCreate(configFolder + separator + "Friends.txt");
        if (!file.exists()) {
            return;
        }
        final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(file);
        bufferedReader.lines().forEach(line -> Main.friendManager.addFriend(line.replace("\"", "")));
        Main.fileManager.closeBufferedReader(bufferedReader);
    }

    protected void saveActiveConfig(final String folder) {
        final File file = Main.fileManager.registerFileAndCreate(configFolder + separator + "ActiveConfig.txt");
        final BufferedWriter bufferedWriter = Main.fileManager.createBufferedWriter(file);
        Main.fileManager.writeLine(bufferedWriter, "\"" + folder + "\"");
        Main.fileManager.closeBufferedWriter(bufferedWriter);
    }

    public void load(final String folder, boolean preserveKeybinds) {
        final File file = new File(configFolder + separator + folder);
        for (Module module : Main.moduleManager.getModules()) {
            final File path = new File(file + separator + module.getCategory().toString());
            final File moduleFile = new File(path + separator + module.getName() + ".txt");
            if (moduleFile.exists()) {
                final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(moduleFile);
                bufferedReader.lines().forEach(line -> loadModule(module, line, preserveKeybinds));
                Main.fileManager.closeBufferedReader(bufferedReader);
            }
        }
        final File hudFolder = Main.fileManager.registerPathAndCreate(file + separator + "Hud");
        if (hudFolder.exists()) {
            try {
                for (HudComponent hudComponent : Main.hudManager.getHudComponents()) {
                    final File hudComponentFile = Main.fileManager.registerFileAndCreate(hudFolder + separator + hudComponent.getName() + ".txt");
                    if (hudComponentFile.exists()) {
                        final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(hudComponentFile);
                        bufferedReader.lines().forEach(line -> {
                            final String[] split = line.replace("\"", "").replace(" ", "").split(":");
                            final String type = split[0];
                            if (type.equals("Enabled")) {
                                hudComponent.setEnabled(Boolean.parseBoolean(split[1]));
                            }
                            if (type.equals("Position")) {
                                final String[] pos = split[1].replace("\"", "").replace(" ", "").split(",");
                                hudComponent.setPosition(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]));
                                hudComponent.loaded = true;
                            }
                        });
                        Main.fileManager.closeBufferedReader(bufferedReader);
                    }
                }
            } catch (Exception ignored) {
            }
        }
        saveActiveConfig(folder);
    }

    public void save(final String folder) {
        final File file = Main.fileManager.registerPathAndCreate(configFolder + separator + folder);
        for (Category category : Main.moduleManager.getCategories()) {
            final File categoryFolder = Main.fileManager.registerPathAndCreate(file + separator + category.toString() + separator);
            for (Module module : Main.moduleManager.getModulesInCategory(category)) {
                final File moduleFile = Main.fileManager.registerFileAndCreate(categoryFolder + separator + module.getName() + ".txt");
                final BufferedWriter bufferedWriter = Main.fileManager.createBufferedWriter(moduleFile);
                for (Setting<?> setting : module.getSettings()) {
                    if (setting instanceof ColorBox) {
                        saveValue(bufferedWriter, setting.getName(), String.valueOf(((ColorBox) setting).GetColor().getRGB()));
                        continue;
                    }
                    if (setting instanceof ComboBox) {
                        saveValue(bufferedWriter, setting.getName(), String.valueOf(((ComboBox) setting).GetCombo()));
                        continue;
                    }
                    if (setting instanceof Key) {
                        saveValue(bufferedWriter, setting.getName(), ((Key) setting).GetKey() + ":" + ((Key) setting).isHold());
                        continue;
                    }
                    if (setting instanceof Slider) {
                        saveValue(bufferedWriter, setting.getName(), String.valueOf(((Slider) setting).GetSlider()));
                        continue;
                    }
                    if (setting instanceof Switch) {
                        saveValue(bufferedWriter, setting.getName(), String.valueOf(((Switch) setting).GetSwitch()));
                    }
                }
                Main.fileManager.closeBufferedWriter(bufferedWriter);
            }
        }
        final File hudFolder = Main.fileManager.registerPathAndCreate(configFolder + separator + folder + separator + "Hud");
        for (HudComponent hudComponent : Main.hudManager.getHudComponents()) {
            final File hudComponentFile = Main.fileManager.registerFileAndCreate(hudFolder + separator + hudComponent.getName() + ".txt");
            final BufferedWriter bufferedWriter = Main.fileManager.createBufferedWriter(hudComponentFile);
            Main.fileManager.writeLine(bufferedWriter, "\"Enabled\": \"" + hudComponent.isEnabled() + "\"");
            Main.fileManager.writeLine(bufferedWriter, "\"Position\": \"" + hudComponent.getX() + "\", \"" + hudComponent.getY() + "\"");
            Main.fileManager.closeBufferedWriter(bufferedWriter);
        }
        if (!folder.equals("AutoSave")) {
            saveActiveConfig(folder);
        }
    }

    public void loadSingleCategory(File file, boolean preserveKeybinds) {
        for (Category category : Main.moduleManager.getCategories()) {
            if (category.toString().equals(file.getName())) {
                for (Module module : Main.moduleManager.getModulesInCategory(category)) {
                    System.out.println(module.getName());
                    final File file1 = new File(file + separator + module.getName() + ".txt");
                    if (file1.exists()) {
                        final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(file1);
                        bufferedReader.lines().forEach(line -> loadModule(module, line, preserveKeybinds));
                        Main.fileManager.closeBufferedReader(bufferedReader);
                    }
                }
            }
        }
    }

    public void loadSingleModule(File file, boolean preserveKeybinds) {
        for (Module module : Main.moduleManager.getModules()) {
            System.out.println(file.getName());
            if (module.getName().equals(file.getName().replace(".txt", ""))) {
                final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(file);
                bufferedReader.lines().forEach(line -> loadModule(module, line, preserveKeybinds));
                Main.fileManager.closeBufferedReader(bufferedReader);
            }
        }
    }

    public void saveValue(final BufferedWriter bufferedWriter, final String name, final String value) {
        Main.fileManager.writeLine(bufferedWriter, "\"" + name + "\": \"" + value + "\"");
    }

    public void loadModule(final Module module, final String line, final boolean preserveKeybinds) {
        final String[] finalLine = line.replace("\"", "").replace(" ", "").split(":");
        final String settingName = finalLine[0];
        final String value = finalLine[1];
        final Setting<?> setting = getSettingByNameAndModule(module, settingName);
        if (!preserveKeybinds || !(setting instanceof Key)) {
            setValueFromSetting(setting, value, settingName.equals("Enabled"), setting instanceof Key ? finalLine[2] : "");
        }
    }

    protected void setValueFromSetting(Setting<?> setting, String line, boolean enabled, String hold) {
        if (enabled) {
            final Module module = setting.getModule();
            if (line.equals("true") && !module.isEnabled()) {
                module.enableModule();
            } else if (line.equals("false") && module.isEnabled()) {
                module.disableModule();
            }
            return;
        }
        if (setting instanceof ColorBox) {
            ((ColorBox) setting).setValue(new Color(Integer.parseInt(line), true));
            return;
        }
        if (setting instanceof ComboBox) {
            ((ComboBox) setting).setValue(line);
            return;
        }
        if (setting instanceof Key) {
            ((Key) setting).setValue(Integer.parseInt(line));
            ((Key) setting).setHold(Boolean.parseBoolean(hold));
            return;
        }
        if (setting instanceof Slider) {
            ((Slider) setting).setValue(Float.parseFloat(line));
            return;
        }

        if (setting instanceof Switch) {
            ((Switch) setting).setValue(Boolean.parseBoolean(line));
        }
    }


    protected Setting<?> getSettingByNameAndModule(Module module, String name) {
        for (Setting<?> setting : module.getSettings()) {
            if (setting.getName().replace(" ", "").equals(name.replace(" ", ""))) {
                return setting;
            }
        }
        return null;
    }

    public File getConfigFolder() {
        return configFolder;
    }

    public String getSeparator() {
        return separator;
    }
}
