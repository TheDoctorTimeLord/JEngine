package ru.jengine.taskscheduler;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Context;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.implementation.moduleimpls.AnnotationModule;
import ru.jengine.beancontainer.service.Constants.Contexts;

@ContainerModule
@Context(Contexts.DEFAULT_CONTEXT)
@Import(TaskSchedulerImpl.class)
public class TaskSchedulerModule extends AnnotationModule {
}
