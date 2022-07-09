package ru.jengine.jsonconverter.modules;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.implementation.moduleimpls.AnnotationModule;
import ru.jengine.beancontainer.Constants.Contexts;

@ContainerModule(contextName = Contexts.JSON_CONVERTER_CONTEXT)
@PackageScan("ru.jengine.jsonconverter.standardtools")
public class JsonStandardToolsModule extends AnnotationModule {
}
