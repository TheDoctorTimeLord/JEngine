package ru.jengine.beancontainer2;

import org.junit.Assert;
import org.junit.Test;
import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.intstructure.pac4.A;
import ru.jengine.beancontainer2.intstructure.pac4.B;
import ru.jengine.beancontainer2.intstructure.pac4.C;
import ru.jengine.beancontainer2.intstructure.pac4.StartModuleWithBeans;
import ru.jengine.beancontainer2.intstructure.pac5.D;
import ru.jengine.beancontainer2.intstructure.pac5.E;
import ru.jengine.beancontainer2.intstructure.pac5.StartModuleWithProcessors;
import ru.jengine.beancontainer2.intstructure.pac6.F;
import ru.jengine.beancontainer2.intstructure.pac6.G;
import ru.jengine.beancontainer2.intstructure.pac6.StartModuleWithImportBean;
import ru.jengine.beancontainer2.intstructure.pac7.H;
import ru.jengine.beancontainer2.intstructure.pac7.I;
import ru.jengine.beancontainer2.intstructure.pac7.J;
import ru.jengine.beancontainer2.intstructure.pac7.StartModuleWithExistingBeans;

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
}
