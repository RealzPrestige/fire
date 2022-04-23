package dev.zprestige.fire.manager.modulemanager;

import dev.zprestige.fire.module.Category;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.util.impl.ClassFinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    protected ArrayList<Module> modules;
    protected final Category[] categories = Category.values();

    public ModuleManager() {
        modules = ClassFinder.getModules();
    }

    public ArrayList<Module> getModulesInCategory(Category category) {
        return modules.stream().filter(module -> module.getCategory().equals(category)).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Category> getCategories() {
        return Arrays.asList(categories);
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public Module getModuleByClass(final Class<?> c) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(c.getName().split("\\.")[6])).findFirst().orElse(null);
    }
}
