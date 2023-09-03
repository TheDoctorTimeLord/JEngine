package ru.jengine.taskscheduler;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.modules.AnnotationModule;
import ru.jengine.beancontainer.Constants.Contexts;

@ContainerModule(contextName = Contexts.DEFAULT_CONTEXT)
@Import(TaskSchedulerImpl.class)
public class TaskSchedulerModule extends AnnotationModule {
}
