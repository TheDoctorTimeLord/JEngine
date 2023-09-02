package ru.jengine.beancontainer2.operations;

import org.junit.Assert;
import org.junit.Test;
import ru.jengine.beancontainer2.ContainerState;
import ru.jengine.beancontainer2.JEngineContainer;
import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.intstructure.pac0.StartModule;
import ru.jengine.beancontainer2.intstructure.pac1.Module1;
import ru.jengine.beancontainer2.intstructure.pac1.Module2;
import ru.jengine.beancontainer2.intstructure.pac1.Module3;
import ru.jengine.beancontainer2.intstructure.pac2.ModuleInOtherPackage;
import ru.jengine.beancontainer2.intstructure.pac3.ModuleByFinder1;
import ru.jengine.beancontainer2.intstructure.pac3.ModuleByFinder2;
import ru.jengine.beancontainer2.modules.Module;
import ru.jengine.beancontainer2.operations.defaultimpl.ModuleFinderOperation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModuleFinderOperationTest {
    @Test
    public void testModuleFinding() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModule.class).build());
        container.executeOperations(new ModuleFinderOperation(), new ContainerOperation() {
            @Override
            public void apply(OperationResult result, ContainerState state) {
                Map<String, List<Module>> modulesByContext = poolResult(result, ResultConstants.MODULES_BY_CONTEXT, Map.class);

                Assert.assertEquals(3, modulesByContext.size());

                equals(modulesByContext, "1", StartModule.class, Module1.class, Module3.class);
                equals(modulesByContext, "2", Module2.class, ModuleInOtherPackage.class, ModuleByFinder1.class);
                equals(modulesByContext, "3", ModuleByFinder2.class);
            }

            private static void equals(Map<String, List<Module>> modulesByContext, String contextName, Class<?>... expected) {
                List<Class<?>> actual = modulesByContext.get(contextName).stream()
                        .map(Object::getClass)
                        .collect(Collectors.toList());
                Assert.assertEquals(expected.length, actual.size());

                for (Class<?> expectedModuleClass : expected) {
                    Assert.assertTrue("Expected [%s] in %s".formatted(expectedModuleClass, actual), actual.contains(expectedModuleClass));
                }
            }
        });
    }
}
