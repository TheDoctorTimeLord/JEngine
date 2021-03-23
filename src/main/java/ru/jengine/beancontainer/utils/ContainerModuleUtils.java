package ru.jengine.beancontainer.utils;

import ru.jengine.beancontainer.BeanDefinition;
import ru.jengine.beancontainer.ClassFinder;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.annotations.ComponentScan;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Context;
import ru.jengine.beancontainer.dataclasses.ModuleContext;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.service.Constants;

import java.util.*;
import java.util.stream.Collectors;

public class ContainerModuleUtils {
    public static List<Module> scanClassPathAndFindModules(Class<?> mainModule, ClassFinder classFinder) {
        List<Module> result = new ArrayList<>();
        Set<Class<?>> moduleClasses = new HashSet<>();
        Queue<Class<?>> notHandledModules = new LinkedList<>();
        ModuleContext context = new ModuleContext(classFinder);

        notHandledModules.add(mainModule);

        while (!notHandledModules.isEmpty()) {
            Class<?> moduleClass = notHandledModules.remove();

            moduleClasses.add(moduleClass);
            Module module = ContainerModuleUtils.createModule(moduleClass, context);
            result.add(module);

            if (moduleClass.isAnnotationPresent(ComponentScan.class)) {
                String packageToScan = moduleClass.getAnnotation(ComponentScan.class).value();
                classFinder.scan(packageToScan);
                notHandledModules.addAll(classFinder.getAnnotatedClasses(ContainerModule.class).stream()
                        .filter(cls -> !moduleClasses.contains(cls))
                        .collect(Collectors.toList()));
            }
        }

        return result;
    }

    public static List<BeanDefinition> extractAllBeanDefinition(List<Module> modules) {
        return modules.stream()
                .flatMap(module -> module.getBeanDefinitionReaders().stream()
                        .flatMap(reader -> reader.readBeanDefinition().stream())
                )
                .collect(Collectors.toList());
    }

    public static String extractContextForModule(Module module) {
        Context annotation = module.getClass().getAnnotation(Context.class);
        if (annotation == null) {
            return Constants.DEFAULT_CONTEXT;
        }
        return annotation.value();
    }

    public static Module createModule(Class<?> moduleClass, ModuleContext moduleContext) {
        Module module;
        try {
            module = (Module) BeanUtils.createObjectWithDefaultConstructor(moduleClass);
        } catch (ContainerException e) {
            throw new ContainerException(String.format("Module '%s' must have default constructor", moduleClass), e);
        } catch (ClassCastException e) {
            throw new ContainerException(String.format("Module '%s' must implement interface Module", moduleClass), e);
        }

        module.configure(moduleContext);
        return module;
    }
}
