package dev.zprestige.fire.module;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.EventBus;
import dev.zprestige.fire.events.impl.ModuleToggleEvent;
import dev.zprestige.fire.settings.Setting;
import dev.zprestige.fire.settings.impl.Key;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.AnimationUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Module {
    public final Menu Menu = new Menu(this);
    protected float listX = 0.0f;
    protected final EventBus eventBus = Main.eventBus;
    protected final ArrayList<Setting> settings = new ArrayList<>();
    protected final Minecraft mc = Minecraft.getMinecraft();
    protected final Key keybind = Menu.Key("Keybind", Keyboard.KEY_NONE);
    protected final Switch enabled = Menu.Switch("Enabled", false);
    protected String name;
    protected Category category;

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void enableModule() {
        eventBus.register(this);
        postEnableEvent();
        setEnabled(true);
        onEnable();
    }

    public void disableModule() {
        eventBus.unregister(this);
        postDisableEvent();
        setEnabled(false);
        onDisable();
    }

    public void toggleModule() {
        if (isEnabled()){
            disableModule();
        } else {
            enableModule();
        }
    }

    protected void postEnableEvent(){
        final ModuleToggleEvent.Enable moduleToggleEvent = new ModuleToggleEvent.Enable(this);
        eventBus.post(moduleToggleEvent);
    }

    protected void postDisableEvent(){
        final ModuleToggleEvent.Disable moduleToggleEvent = new ModuleToggleEvent.Disable(this);
        eventBus.post(moduleToggleEvent);
    }

    public boolean isEnabled() {
        return enabled.GetSwitch();
    }

    public void setEnabled(boolean enabled) {
        this.enabled.setValue(enabled);
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public Integer getKeybind() {
        return keybind.GetKey();
    }

    public void setKeybind(Integer keybind) {
        this.keybind.setValue(keybind);
    }

    public Key getKeySetting(){
        return keybind;
    }

    public Module withSuper(String name, Category category){
        this.name = name;
        this.category = category;
        return this;
    }

    public void updateListPosition(){
        if (isEnabled()){
            listX = AnimationUtil.decreaseNumber(listX, 0, listX / 10);
        } else {
            listX = AnimationUtil.increaseNumber(listX, stringWidth(), (stringWidth() - listX) / 10);
        }
    }

    public float getListX() {
        return listX;
    }

    public boolean nullCheck() {
        return mc.world == null || mc.player == null;
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public String getData(){
        return "";
    }

    public float stringWidth(){
        final String name = getName();
        if (getData().equals("")){
            return Main.fontManager.getStringWidth(name);
        } else {
            return Main.fontManager.getStringWidth(name + "[" + getData() +"]");
        }
    }
}
