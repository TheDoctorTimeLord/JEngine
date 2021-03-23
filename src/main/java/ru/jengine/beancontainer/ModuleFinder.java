package ru.jengine.beancontainer;

import ru.jengine.beancontainer.dataclasses.ModuleContext;
import ru.jengine.beancontainer.dataclasses.ModuleFindContext;

import java.util.List;

public interface ModuleFinder {
    List<Module> find(ModuleFindContext context);

    default ModuleContext mapContext(ModuleFindContext context) {
        return new ModuleContext(context.getClassFinder());
    }
}
