package ru.jengine.beancontainer2.beanfactory;

import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import ru.jengine.beancontainer2.containercontext.BeanExtractor;
import ru.jengine.beancontainer2.containercontext.ResolvingProperties;
import ru.jengine.beancontainer2.exceptions.ContainerException;
import ru.jengine.beancontainer2.intstructure.factory.*;

import java.util.Map;

public class DefaultBeanFactoryTest {
    private static final SpecialBeanExtractor specialBeanExtractor = new SpecialBeanExtractor();
    private static final BeanFactory beanFactory = new DefaultBeanFactory(specialBeanExtractor);

    @Test
    public void testCreateBeanWithParameters() {
        TestedWithParameters actual = (TestedWithParameters) beanFactory.buildBean(TestedWithParameters.class);

        Assert.assertEquals(specialBeanExtractor.a, actual.getA());
        Assert.assertEquals(specialBeanExtractor.b, actual.getB());
        Assert.assertEquals(specialBeanExtractor.c, actual.getC());
    }

    @Test
    public void testCreateBeanWithoutInject() {
        TestedWithoutInject actual = (TestedWithoutInject) beanFactory.buildBean(TestedWithoutInject.class);

        Assert.assertEquals(specialBeanExtractor.a, actual.getA());
    }

    @Test
    public void testCreateBeanWithDefaultConstructed() {
        beanFactory.buildBean(TestedWithDefaultConstructed.class);
    }

    @Test(expected = ContainerException.class)
    public void testCreateBeanWithIncorrectInject() {
        beanFactory.buildBean(TestedWithIncorrectInject.class);
    }

    private static class SpecialBeanExtractor implements BeanExtractor {
        private final A a = new A();
        private final B b = new B();
        private final C c = new C();
        private final Map<Class<?>, Object> mapping = Map.ofEntries(
                Map.entry(A.class, a),
                Map.entry(B.class, b),
                Map.entry(C.class, c)
        );

        @Nullable
        @Override
        public Object getBean(ResolvingProperties properties) {
            return mapping.getOrDefault(properties.getRequestedClass(), NOT_RESOLVED);
        }
    }
}
