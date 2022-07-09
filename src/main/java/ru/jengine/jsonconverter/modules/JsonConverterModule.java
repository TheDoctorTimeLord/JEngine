package ru.jengine.jsonconverter.modules;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.annotations.PackagesScan;
import ru.jengine.beancontainer.implementation.moduleimpls.AnnotationModule;
import ru.jengine.beancontainer.Constants.Contexts;
import ru.jengine.jsonconverter.JsonConverterImpl;

@ContainerModule(contextName = Contexts.JSON_CONVERTER_CONTEXT)
@PackagesScan({
        @PackageScan("ru.jengine.jsonconverter.converting"),
        @PackageScan("ru.jengine.jsonconverter.jsonformatting")
})
@Import(JsonConverterImpl.class)
public class JsonConverterModule extends AnnotationModule {
}
