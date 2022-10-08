package ru.jengine.utils.algorithms;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

public class ElementByFeaturesFinder<Classified, Feature, Element> {
    private final Map<Feature, Set<ElementContext<Element, Feature>>> elementsByFeatures = new HashMap<>();
    private final Function<Element, Collection<Feature>> featuresFromElementExtractor;
    private final Function<Classified, Set<Feature>> featuresFromClassifiedExtractor;
    private final Function<Collection<Element>, Element> elementSelector;

    public ElementByFeaturesFinder(
            Function<Element, Collection<Feature>> featuresFromElementExtractor,
            Function<Classified, Set<Feature>> featuresFromClassifiedExtractor)
    {
        this(featuresFromElementExtractor, featuresFromClassifiedExtractor,
                elements -> elements.size() == 1 ? elements.iterator().next() : null);
    }

    public ElementByFeaturesFinder(
            Function<Element, Collection<Feature>> featuresFromElementExtractor,
            Function<Classified, Set<Feature>> featuresFromClassifiedExtractor,
            Function<Collection<Element>, Element> elementSelector)
    {
        this.featuresFromElementExtractor = featuresFromElementExtractor;
        this.featuresFromClassifiedExtractor = featuresFromClassifiedExtractor;
        this.elementSelector = elementSelector;
    }

    public void addElement(Element element) {
        addElement(element, feature -> false);
    }

    public void addElement(Element element, Predicate<Feature> isRequiredFeaturePredicate) {
        Collection<Feature> features = featuresFromElementExtractor.apply(element);
        ElementContext<Element, Feature> elementContext = new ElementContext<>(element, features.stream()
                .filter(isRequiredFeaturePredicate)
                .collect(Collectors.toSet()));

        for (Feature feature : features) {
            elementsByFeatures.computeIfAbsent(feature, f -> new HashSet<>()).add(elementContext);
        }
    }

    @Nullable
    public Element findElement(Classified classified) {
        return elementSelector.apply(findAvailableElements(classified));
    }

    public Collection<Element> findAvailableElements(Classified classified) {
        Set<Feature> classifiedFeatures = featuresFromClassifiedExtractor.apply(classified);
        Set<ElementContext<Element, Feature>> availableElements = Collections.emptySet();

        for (Feature classifiedFeature : classifiedFeatures) {
            Set<ElementContext<Element, Feature>> availableElementsByFeature =
                    elementsByFeatures.getOrDefault(classifiedFeature, Collections.emptySet());

            if (availableElements.isEmpty()) {
                availableElements = availableElementsByFeature;
            } else {
                availableElements = Sets.intersection(availableElements, availableElementsByFeature);
            }

            if (availableElements.isEmpty()) {
                break;
            }
        }

        return availableElements.stream()
                .filter(element -> checkContainsRequiredFeatures(element.requiredFeatures, classifiedFeatures))
                .map(ElementContext::element)
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
