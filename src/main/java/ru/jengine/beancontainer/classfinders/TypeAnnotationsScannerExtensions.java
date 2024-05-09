package ru.jengine.beancontainer.classfinders;

import org.reflections.scanners.TypeAnnotationsScanner;
import ru.jengine.utils.AnnotationUtils;

public class TypeAnnotationsScannerExtensions extends TypeAnnotationsScanner {
    public static final String NAME = "TypeAnnotationsScannerExtensions";

    @Override
    public void scan(Object cls) {
        super.scan(cls);

        //TODO подумать, как не конвертировать классы в аннотации
        //Добавляем аннотации аннотаций к явным аннотациям
        final String className = getMetadataAdapter().getClassName(cls);
        AnnotationUtils.resolveNotSystemAnnotation(className).stream()
                .map(annotation -> annotation.annotationType().getName())
                .forEach(annotation -> getStore().put(annotation, className));
    }
}
