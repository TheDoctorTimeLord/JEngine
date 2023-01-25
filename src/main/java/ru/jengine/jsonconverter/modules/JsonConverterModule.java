package ru.jengine.jsonconverter.modules;

import ru.jengine.beancontainer.Constants.Contexts;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.annotations.PackagesScan;
import ru.jengine.beancontainer.implementation.moduleimpls.AnnotationModule;
import ru.jengine.jsonconverter.JsonConverterImpl;

@ContainerModule(contextName = Contexts.JSON_CONVERTER_CONTEXT)
@PackagesScan({
        @PackageScan("ru.jengine.jsonconverter.formatting"),
        @PackageScan("ru.jengine.jsonconverter.linking"),
        @PackageScan("ru.jengine.jsonconverter.resources")
})
@Import(JsonConverterImpl.class)
public class JsonConverterModule extends AnnotationModule {
}
