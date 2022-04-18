package dev.zprestige.fire.manager;

import dev.zprestige.fire.RegisteredClass;
import dev.zprestige.fire.module.Category;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.util.impl.ClassFinder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager extends RegisteredClass {
    protected ArrayList<Module> modules = new ArrayList<>();
    protected final Category[] categories = Category.values();

    public ModuleManager init(){
        modules = ClassFinder.getModules();
        return this;
    }
    public ArrayList<Module> getModulesInCategory(Category category){
        return modules.stream().filter(module -> module.getCategory().equals(category)).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Category> getCategories() {
        return Arrays.asList(categories);
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public Module getModuleByClass(final Class<?> c){
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(c.getName().split("\\.")[5])).findFirst().orElse(null);
    }
}
