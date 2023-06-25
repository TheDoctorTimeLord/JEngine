package ru.jengine.utils.algorithms;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import ru.jengine.utils.CollectionUtils;

/**
 * Алгоритм поиска в глубину на некотором динамически формируемом графе (не нужно передавать все вершины графа заранее).
 */
public class DFS {
    /**
     * Выполняет поиск в глубину на динамически формируемом графе (не нужно передавать все вершины графа заранее).
     * Может остановиться на одной из достигнутых вершин, если выполняется условие stopCondition. Если ни на одной
     * вершине stopCondition не выполнилось, возвращает null. Позволяет, как обрабатывать все достигнутые вершины
     * (vertexReachedHandler), так и обрабатывать все вершины, которые являются соседями достигнутой
     * (neighbourVertexHandler).
     *
     * <ol>
     * <b>Ход алгоритма:</b>
     * <li>Добавляем стартовую вершину в стек на достижение вершин</li>
     * <li>Если стек не пуст, достаём из него очередную вершину, иначе переходим на шаг 10</li>
     * <li>Для извлечённой вершины выполняем vertexReachedHandler</li>
     * <li>Проверяем для вершины stopCondition. Если он вернул true, то останавливаем алгоритм и возвращаем вершину</li>
     * <li>Помечаем вершину, как достигнутую</li>
     * <li>Извлекаем всех соседей достигнутой вершины с помощью neighboursExtractor и для каждой из них:</li>
     * <li>Проверяем, посещали ли мы эту вершину, если да, то пропускаем её, если нет, то продолжаем</li>
     * <li>Выполняем neighbourVertexHandler для этой вершины, после чего добавляем её в стек на достижение</li>
     * <li>После того, как мы прошли по всем соседним вершинам, переходим на шаг 2</li>
     * <li>Завершаем работу алгоритма и возвращаем null (ни для одной вершины не выполнилось stopCondition == true)</li>
     * </ol>
     *
     * @param startVertex вершина, с которой начинается работа алгоритма
     * @param neighboursExtractor функция извлечения всех соседей достигнутой вершины
     * @param stopCondition условие остановки алгоритма (может быть пропущено)
     * @param vertexReachedHandler обработчик, применяемый для каждой вершины, когда она помечается как достигнутая
     *                             (может быть пропущен)
     * @param neighbourVertexHandler обработчик, который выполняется для вершины, соседней с достигнутой вершиной
     *                               (1-й аргумент — соседняя вершина, 2-й аргумент — достигнутая вершина)
     *                               (может быть пропущен)
     * @param backFromVertexHandler обработчик, вызываемый для вершины, у которой все соседи уже были обойдены
     * @param <V> Тип вершин графа, по которым выполняется обход в глубину. Вершины обязаны корректно определять
     *           методы {@link Object#equals(Object)} и {@link Object#hashCode()}
     * @return вершина, для которой было выполнено условие stopCondition, либо null, если ни для одной вершины это
     * условие выполнено не было
     */
    public static <V> V runAlgorithm(V startVertex, Function<V, Collection<V>> neighboursExtractor,
            @Nullable Function<V, Boolean> stopCondition, @Nullable Consumer<V> vertexReachedHandler,
            @Nullable BiConsumer<V, V> neighbourVertexHandler, @Nullable BiConsumer<V, Collection<V>> backFromVertexHandler)
    {
        stopCondition = stopCondition != null ? stopCondition : v -> false;
        vertexReachedHandler = vertexReachedHandler != null ? vertexReachedHandler : v -> {};
        neighbourVertexHandler = neighbourVertexHandler != null ? neighbourVertexHandler : (v1, v2) -> {};

        Deque<CommonVertex<V>> vertexStack = new ArrayDeque<>();
        Set<V> reachedVertexes = new HashSet<>();

        vertexStack.add(new CommonVertex<>(startVertex));

        while (!vertexStack.isEmpty()) {
            CommonVertex<V> reachedVertex = vertexStack.pollLast();
            V vertex = reachedVertex.vertex;

            if (backFromVertexHandler != null && reachedVertex instanceof BackFromDfsVertex<V> backFromDfs) {
                backFromVertexHandler.accept(vertex, backFromDfs.neighbours);
                continue;
            }

            if (reachedVertexes.contains(vertex)) {
                continue;
            }

            vertexReachedHandler.accept(vertex);
            if (Boolean.TRUE.equals(stopCondition.apply(vertex))) {
                return vertex;
            }

            reachedVertexes.add(vertex);

            Collection<V> neighbours = CollectionUtils.getOrEmpty(neighboursExtractor.apply(vertex));

            if (backFromVertexHandler != null) {
                vertexStack.addLast(new BackFromDfsVertex<>(vertex, neighbours));
            }

            for (V neighbour : neighbours) {
                if (!reachedVertexes.contains(neighbour)) {
                    neighbourVertexHandler.accept(neighbour, vertex);
                    vertexStack.addLast(new CommonVertex<>(neighbour));
                }
            }
        }

        return null;
    }

    private static class CommonVertex<V> {
        private final V vertex;

        private CommonVertex(V vertex) {
            this.vertex = vertex;
        }
    }

    private static class BackFromDfsVertex<V> extends CommonVertex<V> {
        private final Collection<V> neighbours;

        private BackFromDfsVertex(V vertex, Collection<V> neighbours) {
            super(vertex);
            this.neighbours = neighbours;
        }
    }
}
