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
 * Алгоритм поиска в ширину на некотором динамически формируемом графе (не нужно передавать все вершины графа
 * заранее). Допускает появление рёбер веса 0 и 1.
 */
public class ZeroOneBFS {
    /**
     * Выполняет 0-1 поиск в ширину на динамически формируемом графе (не нужно передавать все вершины графа заранее).
     * Рёбра графа могут иметь веса 0 или 1. Алгоритм будет пытаться обрабатывать сначала вершины, достижимыепо рёбрам
     * веса 0, когда таковые закончатся, произойдёт переход по ребру веса 1. Может остановиться на одной из
     * достигнутых вершин, если выполняется условие stopCondition. Если ни на одной вершине stopCondition не
     * выполнилось, возвращает null. Позволяет, как обрабатывать все достигнутые вершины (vertexReachedHandler), так
     * и обрабатывать все вершины, которые являются соседями достигнутой (neighbourVertexHandler).
     *
     * <ol>
     * <b>Ход алгоритма:</b>
     * <li>Добавляем стартовую вершину в очередь на достижение вершин</li>
     * <li>Если очередь не пуста, достаём из неё очередную вершину, иначе переходим на шаг 10</li>
     * <li>Для извлечённой вершины выполняем vertexReachedHandler</li>
     * <li>Проверяем для вершины stopCondition. Если он вернул true, то останавливаем алгоритм и возвращаем вершину</li>
     * <li>Помечаем вершину, как достигнутую</li>
     * <li>Извлекаем всех соседей достигнутой вершины с помощью neighboursExtractor и для каждой из них:</li>
     * <li>Проверяем, встречали ли мы эту вершину ранее, если да, то пропускаем её, если нет, то продолжаем</li>
     * <li>Выполняем neighbourVertexHandler для этой вершины, после чего добавляем её в начало очереди, если она
     * достижима по ребру веса 0, в противном случае в конец очереди</li>
     * <li>После того, как мы прошли по всем соседним вершинам, переходим на шаг 2</li>
     * <li>Завершаем работу алгоритма и возвращаем null (ни для одной вершины не выполнилось stopCondition == true)</li>
     * </ol>
     *
     * @param startVertex вершина, с которой начинается работа алгоритма
     * @param neighboursExtractor функция извлечения всех соседей достигнутой вершины с указанием веса (0 или 1)
     * @param stopCondition условие остановки алгоритма (может быть пропущено)
     * @param vertexReachedHandler обработчик, применяемый для каждой вершины, когда она помечается как достигнутая
     *                             (может быть пропущен)
     * @param neighbourVertexHandler обработчик, который выполняется для вершины, соседней с достигнутой вершиной
     *                               (1-й аргумент — соседняя вершина, 2-й аргумент — достигнутая вершина)
     *                               (может быть пропущен)
     * @param <V> Тип вершин графа, по которым выполняется обход в ширину. Вершины обязаны корректно определять
     *           методы {@link Object#equals(Object)} и {@link Object#hashCode()}
     * @return вершина, для которой было выполнено условие stopCondition, либо null, если ни для одной вершины это
     * условие выполнено не было
     */
    public static <V> V runAlgorithm(V startVertex, Function<V, Collection<Egge<V>>> neighboursExtractor,
            @Nullable Function<V, Boolean> stopCondition, @Nullable Consumer<V> vertexReachedHandler,
            @Nullable BiConsumer<V, V> neighbourVertexHandler)
    {
        stopCondition = stopCondition != null ? stopCondition : v -> false;
        vertexReachedHandler = vertexReachedHandler != null ? vertexReachedHandler : v -> {};
        neighbourVertexHandler = neighbourVertexHandler != null ? neighbourVertexHandler : (v1, v2) -> {};

        Set<V> reachedVertexes = new HashSet<>();
        Deque<V> vertexQueue = new ArrayDeque<>();

        vertexQueue.add(startVertex);
        reachedVertexes.add(startVertex);

        while (!vertexQueue.isEmpty()) {
            V reachedVertex = vertexQueue.poll();

            vertexReachedHandler.accept(reachedVertex);
            if (Boolean.TRUE.equals(stopCondition.apply(reachedVertex))) {
                return reachedVertex;
            }

            for (Egge<V> neighbour : CollectionUtils.getOrEmpty(neighboursExtractor.apply(reachedVertex))) {
                if (!reachedVertexes.contains(neighbour.vertex)) {
                    reachedVertexes.add(neighbour.vertex);
                    neighbourVertexHandler.accept(neighbour.vertex, reachedVertex);

                    if (neighbour.weight == Weight.ZERO) {
                        vertexQueue.addFirst(neighbour.vertex);
                    }
                    else {
                        vertexQueue.addLast(neighbour.vertex);
                    }
                }
            }
        }

        return null;
    }

    public static class Egge<V> {
        private final V vertex;
        private final Weight weight;

        private Egge(V vertex, Weight weight) {
            this.vertex = vertex;
            this.weight = weight;
        }

        public static <V> Egge<V> zeroEgge(V vertex) {
            return new Egge<>(vertex, Weight.ZERO);
        }

        public static <V> Egge<V> oneEgge(V vertex) {
            return new Egge<>(vertex, Weight.ONE);
        }
    }

    private enum Weight {
        ZERO, ONE
    }
}
