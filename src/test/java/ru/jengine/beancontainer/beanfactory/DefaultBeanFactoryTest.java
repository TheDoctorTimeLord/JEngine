package ru.jengine.beancontainer.beanfactory;

import org.junit.Assert;
import org.junit.Test;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.BeanExtractor;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.intstructure.factory.*;

import java.util.Map;

import static ru.jengine.beancontainer.containercontext.ResolvedBeanData.NOT_RESOLVED;

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
        private final Map<Class<?>, ResolvedBeanData> mapping = Map.ofEntries(
                entry(A.class, a),
                entry(B.class, b),
                entry(C.class, c)
        );

        private Map.Entry<Class<?>, ResolvedBeanData> entry(Class<?> beanClass, Object bean) {
            return Map.entry(beanClass, new ResolvedBeanData(bean, beanClass));
        }

        @Override
        public ResolvedBeanData getBean(ResolvingProperties properties) {
            return mapping.getOrDefault(properties.getRequestedClass(), NOT_RESOLVED);
        }
    }
}
