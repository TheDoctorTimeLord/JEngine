package ru.jengine.utils.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import ru.jengine.utils.CollectionUtils;

/**
 * Алгоритм топологической сортировки некоторого графа, основанный на обходе в глубину
 */
public class TopSort {
    /**
     * Выполняет топологическую сортировку бесконтурной сети (ориентированном графе, не имеющем циклов), заданном
     * набором вершин, получаемых с помощью graphSupplier и набором переходов из каждой вершины в её соседей
     * (иначе говоря, "зависимостей" вершины), получаемом с помощью dependenciesExtractor. Результатом выполнения
     * является список вершин графа, расположенных таким образом, что слева от любой вершины в списке находятся все её
     * вершины зависимости. Очевидно, что набор вершин слева от некоторой вершины ВКЛЮЧАЕТ все её зависимости, но не
     * ПОЛНОСТЬЮ состоит из них.
     * <pre>
     * Пример возможных топологических сортировок графа, заданного следующим списком смежности:
     *
     * 1 -> { 2, 3 }
     * 2 -> { 3, 4 }
     * 3 -> { }
     * 4 -> { }
     *
     * Возможные результаты топологической сортировки:
     * 1) 4 3 2 1
     * 2) 3 4 2 1
     * </pre>
     *
     * Позволяет обрабатывать вершины, когда они были добавлены в результат топологической сортировки
     * (sortedVertexHandler)
     *
     * @param graphSupplier функция, возвращающая полный набор вершин, сортируемого графа
     * @param dependenciesExtractor функция, извлекающая все зависимости вершины от других вершин графа
     * @param sortedVertexHandler обработчик вершины, вызывающийся после добавления вершины в результат сортировки
     * @param <V> тип вершин в графе
     */
    public static <V> List<V> runAlgorithm(Supplier<Set<V>> graphSupplier, Function<V,
            Collection<V>> dependenciesExtractor, Consumer<V> sortedVertexHandler)
    {
        List<V> sortedGraph = new ArrayList<>();
        Set<V> unhandledVertexes = new HashSet<>(graphSupplier.get());

        while (!unhandledVertexes.isEmpty()) {
            V currentRoot = unhandledVertexes.iterator().next();
            DFS.runAlgorithm(
                    currentRoot,
                    vertex -> extractNeighbours(vertex, dependenciesExtractor, unhandledVertexes),
                    null,
                    unhandledVertexes::remove,
                    null,
                    (vertex, neighbours) -> TopSort.handleBackFormDependedVertexes(sortedGraph, vertex, sortedVertexHandler)
            );
        }

        return sortedGraph;
    }

    private static <V> void handleBackFormDependedVertexes(List<V> sortedGraph, V vertex,
            Consumer<V> sortedVertexHandler)
    {
        sortedGraph.add(vertex);
        sortedVertexHandler.accept(vertex);
    }

    private static <V> List<V> extractNeighbours(V vertex, Function<V, Collection<V>> dependenciesExtractor,
            Set<V> unhandledVertexes)
    {
        return CollectionUtils.getOrEmpty(dependenciesExtractor.apply(vertex))
                .stream()
                .filter(unhandledVertexes::contains)
                .toList();
    }
}
