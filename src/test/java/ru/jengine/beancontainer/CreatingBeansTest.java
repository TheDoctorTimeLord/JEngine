package ru.jengine.beancontainer;

import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;
import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.intstructure.pac4.A;
import ru.jengine.beancontainer.intstructure.pac4.B;
import ru.jengine.beancontainer.intstructure.pac4.C;
import ru.jengine.beancontainer.intstructure.pac4.StartModuleWithBeans;
import ru.jengine.beancontainer.intstructure.pac5.D;
import ru.jengine.beancontainer.intstructure.pac5.E;
import ru.jengine.beancontainer.intstructure.pac5.StartModuleWithProcessors;
import ru.jengine.beancontainer.intstructure.pac6.F;
import ru.jengine.beancontainer.intstructure.pac6.G;
import ru.jengine.beancontainer.intstructure.pac6.StartModuleWithImportBean;
import ru.jengine.beancontainer.intstructure.pac7.H;
import ru.jengine.beancontainer.intstructure.pac7.I;
import ru.jengine.beancontainer.intstructure.pac7.J;
import ru.jengine.beancontainer.intstructure.pac7.StartModuleWithExistingBeans;
import ru.jengine.beancontainer.intstructure.pac8.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;

public class CreatingBeansTest {
    @Test
    public void testCreatingBeans() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithBeans.class).build());
        container.initializeContainerByDefault();

        A actualA = container.getBean(A.class);
        B actualB = container.getBean(B.class);
        C actualC = container.getBean(C.class);

        Assert.assertNotNull(actualA);
        Assert.assertNotNull(actualB);
        Assert.assertNotNull(actualC);

        Assert.assertSame(actualA, actualB.getA());
        Assert.assertSame(actualA, actualC.getA());
        Assert.assertSame(actualB.getA(), actualC.getA());
        Assert.assertSame(actualB, actualC.getB());
    }

    @Test
    public void testHandleBeanProcessors() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithProcessors.class).build());
        container.initializeContainerByDefault();

        D actualD = container.getBean(D.class);
        E actualE = container.getBean(E.class);

        Assert.assertTrue(actualD.isGlobalTest());
        Assert.assertTrue(actualE.isTest());
    }

    @Test
    public void testImportBeans() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithImportBean.class).build());
        container.initializeContainerByDefault();

        F actualF = container.getBean(F.class);
        G actualG = container.getBean(G.class);

        Assert.assertNotNull(actualF);
        Assert.assertNotNull(actualG);

        Assert.assertSame(actualF, actualG.getF());
    }

    @Test
    public void testExistingBeans() {
        H externalH = new H();
        I externalI = new I(externalH);

        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithExistingBeans.class)
                .addBeans(externalH)
                .addBeans(Constants.Contexts.DEFAULT_CONTEXT, externalI)
                .build());
        container.initializeContainerByDefault(); //TODO реализовать систему перезатираний BeanDefinition или выбора кандидатов

        H actualH = container.getBean(H.class);
        I actualI = container.getBean(Constants.Contexts.DEFAULT_CONTEXT, I.class);
        J actualJ = container.getBean(J.class);

        Assert.assertNotNull(actualH);
        Assert.assertNotNull(actualI);
        Assert.assertNotNull(actualJ);

        Assert.assertSame(externalH, actualH);
        Assert.assertSame(externalI, actualI);

        Assert.assertSame(externalH, actualJ.getH());
        Assert.assertSame(externalI, actualJ.getI());
    }

    @Test
    public void testImplementationOfInterface() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithImplementations.class).build());
        container.initializeContainerByDefault();

        K actualK = container.getBean(K.class);
        L actualL = container.getBean(L.class);
        M actualM = container.getBean(M.class);
        N actualN = container.getBean(N.class);

        List<Int> actualImplementations = container.getBean(Int.class, List.class);

        Assert.assertEquals(4, actualImplementations.size());

        MatcherAssert.assertThat(actualImplementations, hasItem(actualK));
        MatcherAssert.assertThat(actualImplementations, hasItem(actualL));
        MatcherAssert.assertThat(actualImplementations, hasItem(actualN));
        MatcherAssert.assertThat(actualImplementations, not(hasItem(actualM)));

        List<Class<?>> actualClasses = actualImplementations.stream()
                .map(Object::getClass)
                .collect(Collectors.toList());
        MatcherAssert.assertThat(actualClasses, hasItem(M.class));
    }
}
