package ru.jengine.beancontainer.implementation.classfinders;

import java.util.List;

import org.reflections.scanners.TypeAnnotationsScanner;

import ru.jengine.beancontainer.utils.AnnotationUtils;

public class TypeAnnotationsScannerExtensions extends TypeAnnotationsScanner {
    public static final String NAME = "TypeAnnotationsScannerExtensions";

    @Override
    public void scan(Object cls) {
        super.scan(cls);

        //TODO подумать, как не конвертировать классы в аннотации
        //Добавляем аннотации аннотаций к явным аннотациям
        final String className = getMetadataAdapter().getClassName(cls);
        ((List<String>)getMetadataAdapter().getClassAnnotationNames(cls)).stream()
                .map(AnnotationUtils::resolveNotSystemAnnotation)
                .flatMap(annotations -> annotations.stream().map(annotation -> annotation.annotationType().getName()))
                .forEach(annotation -> getStore().put(annotation, className));
    }
}
