package ru.jengine.utils.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.base.Objects;

public class ElementByFeaturesFinder<Classified, Feature, Element> {
    private final Set<ElementContext<Element, Feature>> conditionedElements = new HashSet<>();
    private final List<Element> commonElements = new ArrayList<>();

    private final Function<Element, Set<Feature>> featuresFromElementExtractor;
    private final Function<Classified, Set<Feature>> featuresFromClassifiedExtractor;
    private final Function<Collection<Element>, Element> elementSelector;
    private final Comparator<Element> elementSorter;

    public ElementByFeaturesFinder(
            Function<Element, Set<Feature>> featuresFromElementExtractor,
            Function<Classified, Set<Feature>> featuresFromClassifiedExtractor)
    {
        this(featuresFromElementExtractor, featuresFromClassifiedExtractor,
                elements -> elements.size() == 1 ? elements.iterator().next() : null,
                (o1, o2) -> 0);
    }

    public ElementByFeaturesFinder(
            Function<Element, Set<Feature>> featuresFromElementExtractor,
            Function<Classified, Set<Feature>> featuresFromClassifiedExtractor,
            Comparator<Element> elementSorter)
    {
        this(featuresFromElementExtractor, featuresFromClassifiedExtractor,
                elements -> elements.size() == 1 ? elements.iterator().next() : null,
                elementSorter);
    }

    public ElementByFeaturesFinder(
            Function<Element, Set<Feature>> featuresFromElementExtractor,
            Function<Classified, Set<Feature>> featuresFromClassifiedExtractor,
            Function<Collection<Element>, Element> elementSelector, Comparator<Element> elementSorter)
    {
        this.featuresFromElementExtractor = featuresFromElementExtractor;
        this.featuresFromClassifiedExtractor = featuresFromClassifiedExtractor;
        this.elementSelector = elementSelector;
        this.elementSorter = elementSorter;
    }

    public void addElement(Element element) {
        Set<Feature> features = featuresFromElementExtractor.apply(element);
        if (features == null || features.isEmpty()) {
            commonElements.add(element);
            return;
        }

        conditionedElements.add(new ElementContext<>(element, features));
    }

    @Nullable
    public Element findElement(Classified classified) {
        return elementSelector.apply(findAvailableElements(classified));
    }

    public Collection<Element> findAvailableElements(Classified classified) {
        Set<Feature> classifiedFeatures = featuresFromClassifiedExtractor.apply(classified);

        Stream<Element> foundedElements = conditionedElements.stream()
                .filter(element -> checkContainsRequiredFeatures(element.requiredFeatures, classifiedFeatures))
                .map(ElementContext::element);

        return Stream.concat(foundedElements, commonElements.stream())
                .sorted(elementSorter)
                .toList();
    }

    private boolean checkContainsRequiredFeatures(Set<Feature> requiredFeatures, Set<Feature> allFeatures) {
        for (Feature requiredFeature : requiredFeatures) {
            if (!allFeatures.contains(requiredFeature)) {
                return false;
            }
        }
        return true;
    }

    private record ElementContext<Element, Feature>(Element element, Set<Feature> requiredFeatures) {
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof ElementContext<?, ?> that))
                return false;
            return Objects.equal(element, that.element) && Objects.equal(requiredFeatures, that.requiredFeatures);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(element, requiredFeatures);
        }
    }
}
