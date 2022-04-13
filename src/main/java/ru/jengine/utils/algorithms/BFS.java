package ru.jengine.utils.algorithms;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import ru.jengine.utils.CollectionUtils;

/**
 * Алгоритм поиска в ширину на некотором динамически формируемом графе (не нужно передавать все вершины графа заранее).
 */
public class BFS {
    /**
     * Выполняет поиск в ширину на динамически формируемом графе (не нужно передавать все вершины графа заранее).
     * Может остановиться на одной из достигнутых вершин, если выполняется условие stopCondition. Если ни на одной
     * вершине stopCondition не выполнилось, возвращает null. Позволяет, как обрабатывать все достигнутые вершины
     * (vertexReachedHandler), так и обрабатывать все вершины, которые являются соседями достигнутой
     * (neighbourVertexHandler).
     *
     * <ol>
     * <b>Ход алгоритма:</b>
     * <li>Добавляем стартовую вершину в очередь на достижение вершин</li>
     * <li>Если очередь не пуста, достаём из неё очередную вершину, иначе переходим на шаг 10</li>
     * <li>Для извлечённой вершины выполняем vertexReachedHandler</li>
     * <li>Проверяем для вершины stopCondition. Если он вернул true, то останавливаем алгоритм и возвращаем вершину</li>
     * <li>Помечаем вершину, как достигнутую</li>
     * <li>Извлекаем всех соседей достигнутой вершины с помощью neighboursExtractor и для каждой из них:</li>
     * <li>Проверяем, посещали ли мы эту вершину, если да, то пропускаем её, если нет, то продолжаем</li>
     * <li>Выполняем neighbourVertexHandler для этой вершины, после чего добавляем её в очередь на достижение</li>
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
     * @param <V> Тип вершин графа, по которым выполняется обход в ширину. Вершины обязаны корректно определять
     *           методы {@link Object#equals(Object)} и {@link Object#hashCode()}
     * @return вершина, для которой было выполнено условие stopCondition, либо null, если ни для одной вершины это
     * условие выполнено не было
     */
    public static <V> V runAlgorithm(V startVertex, Function<V, Collection<V>> neighboursExtractor,
            @Nullable Function<V, Boolean> stopCondition, @Nullable Consumer<V> vertexReachedHandler,
            @Nullable BiConsumer<V, V> neighbourVertexHandler)
    {
        stopCondition = stopCondition != null ? stopCondition : v -> false;
        vertexReachedHandler = vertexReachedHandler != null ? vertexReachedHandler : v -> {};
        neighbourVertexHandler = neighbourVertexHandler != null ? neighbourVertexHandler : (v1, v2) -> {};

        Set<V> reachedVertexes = new HashSet<>();
        Queue<V> vertexQueue = new ArrayDeque<>();

        vertexQueue.add(startVertex);

        while (!vertexQueue.isEmpty()) {
            V reachedVertex = vertexQueue.poll();

            vertexReachedHandler.accept(reachedVertex);
            if (Boolean.TRUE.equals(stopCondition.apply(reachedVertex))) {
                return reachedVertex;
            }

            reachedVertexes.add(reachedVertex);

            for (V neighbour : CollectionUtils.getOrEmpty(neighboursExtractor.apply(reachedVertex))) {
                if (!reachedVertexes.contains(neighbour)) {
                    neighbourVertexHandler.accept(neighbour, reachedVertex);
                    vertexQueue.add(neighbour);
                }
            }
        }

        return null;
    }
}
