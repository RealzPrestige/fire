package dev.zprestige.fire.util.impl;

import com.google.common.reflect.ClassPath;
import dev.zprestige.fire.module.Category;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import dev.zprestige.fire.util.Utils;
import net.minecraft.launchwrapper.Launch;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClassFinder implements Utils {
    protected static final Category[] categories = Category.values();

    public static ArrayList<Module> getModules() {
        final ArrayList<Module> modules = new ArrayList<>();
        for (Category category : categories) {
            try {
                List<Class<?>> classes = ClassFinder.from("dev.zprestige.fire.module." + category.toString().toLowerCase());
                if (classes == null)
                    return new ArrayList<>();
                for (Class<?> clazz : classes) {
                    if (!Modifier.isAbstract(clazz.getModifiers()) && Module.class.isAssignableFrom(clazz)) {
                        for (Constructor<?> constructor : clazz.getConstructors()) {
                            final String moduleName = clazz.getName().split("\\.")[5];
                            final Module instance = ((Module) constructor.newInstance()).withSuper(moduleName, getCategoryByString(category.toString()));
                            Arrays.stream(instance.getClass().getDeclaredFields()).filter(field -> !field.isAccessible()).forEach(field -> field.setAccessible(true));
                            modules.add(instance);
                        }
                    }
                }

            } catch (Exception ignored) {
            }
        }
        return modules;
    }

    public static Category getCategoryByString(String category){
        return Arrays.stream(categories).filter(category1 -> category1.toString().equalsIgnoreCase(category)).findFirst().orElse(null);
    }

    public static ArrayList<HudComponent> hudComponentArrayList() {
        final ArrayList<HudComponent> hudComponents = new ArrayList<>();
        try {
            List<Class<?>> classes = ClassFinder.from("dev.zprestige.fire.ui.hudeditor.components.impl");
            if (classes != null) {
                for (Class<?> clazz : classes) {
                    if (!Modifier.isAbstract(clazz.getModifiers()) && HudComponent.class.isAssignableFrom(clazz)) {
                        for (Constructor<?> constructor : clazz.getConstructors()) {
                            final HudComponent instance = ((HudComponent) constructor.newInstance());
                            Arrays.stream(instance.getClass().getDeclaredFields()).filter(field -> !field.isAccessible()).forEach(field -> field.setAccessible(true));
                            hudComponents.add(instance);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return hudComponents;
    }

    @SuppressWarnings("ALL")
    public static List<Class<?>> from(String packageName) {
        try {
            return ClassPath.from(Launch.classLoader).getAllClasses().stream().filter(info -> info.getName().startsWith(packageName)).map(ClassPath.ClassInfo::load).collect(Collectors.toList());
        } catch (Exception ignored) {
            return null;
        }
    }

}
