package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.Category;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.Setting;
import dev.zprestige.fire.settings.impl.*;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import dev.zprestige.fire.util.impl.Vector2D;

import java.awt.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigManager {
    protected final File configFolder = new File(Main.fileManager.getDirectory() + File.separator + "Configs");
    protected final String separator = File.separator;

    public void savePrefix() {
        final File file = Main.fileManager.registerFileAndCreate(configFolder + separator + "Prefix.txt");
        final BufferedWriter bufferedWriter = Main.fileManager.createBufferedWriter(file);
        Main.fileManager.writeLine(bufferedWriter, "\"" + Main.commandManager.getPrefix() + "\"");
        Main.fileManager.closeBufferedWriter(bufferedWriter);
    }

    public ConfigManager loadPrefix(){
        final File file = Main.fileManager.registerFileAndCreate(configFolder + separator + "Prefix.txt");
        final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(file);
        try {
            final String line = bufferedReader.readLine();
            if (line != null && !line.equalsIgnoreCase("")) {
                Main.commandManager.setPrefix(line.replace("\"", ""));
            }
        } catch (IOException ignored) {
        }
        Main.fileManager.closeBufferedReader(bufferedReader);
        return this;
    }

    public ConfigManager loadActiveConfig() {
        final String active = getActiveConfig();
        if (active == null || !active.equals("") || !new File(configFolder + separator + active).exists()) {
            return this;
        }
        load(active.replace("\"", ""), false);
        return this;
    }

    public String getActiveConfig() {
        final File file = Main.fileManager.registerFileAndCreate(configFolder + separator + "ActiveConfig.txt");
        if (!file.exists()) {
            return "";
        }
        final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(file);
        try {
            if (bufferedReader.readLine() != null) {
                return bufferedReader.readLine();
            }
        } catch (IOException ignored) {
            return "";
        }
        return "";
    }

    public ConfigManager loadSavedFriends(){
        final File file = Main.fileManager.registerFileAndCreate(configFolder + separator + "Friends.txt");
        final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(file);
        bufferedReader.lines().forEach(line -> Main.friendManager.addFriend(line.replace("\"", "")));
        Main.fileManager.closeBufferedReader(bufferedReader);
        return this;
    }

    protected void saveActiveConfig(final String folder) {
        final File file = Main.fileManager.registerFileAndCreate(configFolder + separator + "ActiveConfig.txt");
        final BufferedWriter bufferedWriter = Main.fileManager.createBufferedWriter(file);
        Main.fileManager.writeLine(bufferedWriter, "\"" + folder + "\"");
        Main.fileManager.closeBufferedWriter(bufferedWriter);
    }

    public boolean isConfigTheSame(final String folder, final Module module){
        final File file = new File(configFolder + separator + folder + separator + module.getCategory() + separator + module.getName() + ".txt");
        final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(file);
        AtomicBoolean same = new AtomicBoolean(true);
        bufferedReader.lines().forEach(line -> {
            final String[] finalLine = line.replace("\"", "").replace(" ", "").split(":");
            final String settingName = finalLine[0];
            final String value = finalLine[1];
            final Setting setting = isStringSetting(settingName, module);
            if (setting instanceof ColorBox){
                if (!String.valueOf(((ColorBox) setting).GetColor().getRGB()).equals(value)){
                    same.set(false);
                }
            }
            if (setting instanceof ComboBox) {
                if (!String.valueOf(((ComboBox) setting).GetCombo()).equals(value)){
                    same.set(false);
                }
            }
            if (setting instanceof Key) {
                if (!String.valueOf(((Key) setting).GetKey()).equals(value)){
                    same.set(false);
                }
                if (!String.valueOf(((Key) setting).isHold()).equals(finalLine[2])){
                    same.set(false);
                }
            }
            if (setting instanceof Slider) {
                if (!String.valueOf(((Slider) setting).GetSlider()).equals(value)){
                    same.set(false);
                }
            }
            if (setting instanceof Switch) {
                if (!String.valueOf(((Switch) setting).GetSwitch()).equals(value)){
                    same.set(false);
                }
            }
        });
        return same.get();
    }

    public boolean isConfigTheSame(final String folder){
        return Main.moduleManager.getCategories().stream().allMatch(category -> isCategoryTheSame(folder, category));
    }

    public boolean isCategoryTheSame(final String folder, final Category category){
        return Main.moduleManager.getModulesInCategory(category).stream().allMatch(module -> isConfigTheSame(folder, module));
    }

    public Category getCategoryByString(final String category){
        return Main.moduleManager.getCategories().stream().filter(category1 -> category1.toString().equals(category)).findFirst().orElse(null);
    }

    public Module getModuleByString(final String module){
        return Main.moduleManager.getModules().stream().filter(module1 -> module1.getName().equals(module.replace(".txt", ""))).findFirst().orElse(null);
    }

    protected Setting isStringSetting(final String name, final Module module){
        return module.getSettings().stream().filter(setting -> name.equals(setting.getName())).findFirst().orElse(null);
    }

    public void load(final String folder, boolean preserveKeybinds) {
        final File file = new File(configFolder + separator + folder);
        if (file.exists()){
            return;
        }
        for (Module module : Main.moduleManager.getModules()) {
            final File path = new File(file + separator + module.getCategory().toString());
            final File moduleFile = new File(path + separator + module.getName() + ".txt");
            final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(moduleFile);
            bufferedReader.lines().forEach(line -> loadModule(module, line, preserveKeybinds));
            Main.fileManager.closeBufferedReader(bufferedReader);
        }
        final File hudFolder = Main.fileManager.registerPathAndCreate(file + separator + "Hud");
        for (HudComponent hudComponent : Main.hudManager.getHudComponents()){
            final File hudComponentFile = Main.fileManager.registerFileAndCreate(hudFolder + separator + hudComponent.getName() + ".txt");
            final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(hudComponentFile);
            bufferedReader.lines().forEach(line -> {
                final String[] split = line.replace("\"", "").replace(" ", "").split(":");
                final String type = split[0];
                if (type.equals("Enabled")){
                    hudComponent.setEnabled(Boolean.parseBoolean(split[1]));
                }
                if (type.equals("Position")){
                    final String[] pos = split[1].replace("\"", "").replace(" ", "").split(",");
                    hudComponent.setPosition(new Vector2D(Float.parseFloat(pos[0]), Float.parseFloat(pos[1])));
                }
            });
            Main.fileManager.closeBufferedReader(bufferedReader);
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
                for (Setting setting : module.getSettings()) {
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
        for (HudComponent hudComponent : Main.hudManager.getHudComponents()){
            final File hudComponentFile = Main.fileManager.registerFileAndCreate(hudFolder + separator + hudComponent.getName() + ".txt");
            final BufferedWriter bufferedWriter = Main.fileManager.createBufferedWriter(hudComponentFile);
            Main.fileManager.writeLine(bufferedWriter, "\"Enabled\": \"" + hudComponent.isEnabled() + "\"");
            Main.fileManager.writeLine(bufferedWriter, "\"Position\": \"" + hudComponent.getPosition().getX() + "\", \"" + hudComponent.getPosition().getY() + "\"");
            Main.fileManager.closeBufferedWriter(bufferedWriter);
        }
        saveActiveConfig(folder);
    }

    public void loadSingleCategory(File file, boolean preserveKeybinds) {
        for (Category category : Main.moduleManager.getCategories()) {
            if (category.toString().equals(file.getName())) {
                for (Module module : Main.moduleManager.getModulesInCategory(category)) {
                    System.out.println(module.getName());
                    final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(new File(file + separator + module.getName() + ".txt"));
                    bufferedReader.lines().forEach(line -> loadModule(module, line, preserveKeybinds));
                    Main.fileManager.closeBufferedReader(bufferedReader);
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
        if (preserveKeybinds && settingName.equals("Keybind")) {
            return;
        }
        final String value = finalLine[1];
        final Setting setting = getSettingByNameAndModule(module, settingName);
        setValueFromSetting(setting, value, settingName.equals("Enabled"), setting instanceof Key ? finalLine[2] : "");
    }

    protected void setValueFromSetting(Setting setting, String line, boolean enabled, String hold) {
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


    protected Setting getSettingByNameAndModule(Module module, String name) {
        for (Setting setting : module.getSettings()) {
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
