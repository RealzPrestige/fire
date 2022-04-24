package dev.zprestige.fire.ui.hudeditor.components.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.module.client.clickgui.ClickGui;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ModuleList extends HudComponent {
    protected boolean aligned;

    public ModuleList() {
        super("ModuleList", new Vector2D(0, 0), new Vector2D(0, 0));
    }

    @Override
    public void render() {
        final ArrayList<Module> modules = Main.moduleManager.getModules().stream().filter(module -> module.isEnabled() || module.getListX() < module.stringWidth() - 1).sorted(Comparator.comparing(Module::stringWidth).reversed()).collect(Collectors.toCollection(ArrayList::new));
        final float delta = Main.fontManager.getFontHeight() + 2;
        float listY = position.getY();
        Main.moduleManager.getModules().forEach(Module::updateListPosition);
        for (Module module : modules) {
            if (!module.getDrawn()) {
                continue;
            }
            final String data = module.getData();
            final boolean hadData = !data.equals("");
            final float moduleX = position.getX() + size.getX() - module.stringWidth() + module.getListX();
            if (hadData) {
                Main.fontManager.drawStringWithShadow(module.getName() + "[" + ChatFormatting.WHITE + data + ChatFormatting.RESET + "]", new Vector2D(moduleX, listY), ClickGui.Instance.color.GetColor().getRGB());
            } else {
                Main.fontManager.drawStringWithShadow(module.getName(), new Vector2D(moduleX, listY), ClickGui.Instance.color.GetColor().getRGB());
            }
            listY += delta - (module.getListX() / delta);
        }
        if (!aligned) {
            setPosition(new Vector2D(new ScaledResolution(mc).getScaledWidth() - 100, 0));
            aligned = true;
        }
        setWidth(100);
        setHeight(listY + Main.fontManager.getFontHeight() + 1 - position.getY());
    }
}
