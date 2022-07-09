package ru.jengine.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nullable;

public class CollectionUtils {
    /**
     * Отфильтровывает элементы из коллекции в соответствии с преданным предикатом. ВАЖНО: отфильтрованные элементы
     * удаляются из переданной коллекции. В результате возвращается та же коллекция, что и была передана на вход
     *
     * @param elements фильтруемая коллекция
     * @param filter условие фильтрации элементов
     * @param <E> тип элементов в коллекции
     * @return та же коллекция, что передана на вход
     */
    public static <E> List<E> filter(List<E> elements, Predicate<E> filter) {
        List<E> filtered = new ArrayList<>();

        elements.removeIf(module -> {
            if (filter.test(module)) {
                filtered.add(module);
                return true;
            }
            return false;
        });

        return filtered;
    }

    /**
     * Группирует элементы списка по некоторому признаку
     *
     * @param elements группируемый список
     * @param keyExtractor функция, определяющая к какому из признаков относится конкретный элемент
     * @param <K> тип признака, по которому была выделена группа элементов
     * @param <V> тип элементов в списке
     * @return Отображение из признака в группу элементов, имеющую этот признак. Один элемент коллекции может быть
     *         отнесён только к одному признаку
     */
    public static <K, V> Map<K, List<V>> groupBy(List<V> elements, Function<V, K> keyExtractor) {
        Map<K, List<V>> result = new HashMap<>();

        for (V element : elements) {
            K key = keyExtractor.apply(element);
            if (!result.containsKey(key)) {
                result.put(key, new ArrayList<>());
            }
            result.get(key).add(element);
        }

        return result;
    }

    /**
     * Объединяет элементы в один список в том порядке, в котором переданы элементы. В качестве элемента может быть
     * передана коллекция, в этом случае все её элементы станут отдельными элементами итогового списка
     *
     * @param elements конкатенируемые элементы
     * @param <V> тип, к которому будут прикастованы все элементы итоговой коллекции
     * @return новая коллекция, содержащая все конкатенируемые элементы.
     */
    public static <V> List<V> concat(Object... elements) {
        List<V> result = new ArrayList<>();

        for (Object element : elements) {
            if (element instanceof Collection) {
                result.addAll((Collection<? extends V>)element);
            }
            else {
                result.add((V)element);
            }
        }

        return result;
    }

    /**
     * Добавляет элемент в конец списка
     *
     * @param collection список, в который добавляется элемент
     * @param element добавляемый элемент
     * @param <T> тип элементов в списке
     * @return изменённая переданная на вход коллекция
     */
    public static <T> List<T> addLast(List<T> collection, T element) {
        collection.add(element);
        return collection;
    }

    /**
     * Возвращает последний элемент списка, сохраняя целостность списка
     *
     * @param elements список, в котором извлекается последний элемент
     * @param <V> тип элементов в списке
     * @return последний элемент коллекции или null, если переданная коллекция пуста
     */
    @Nullable
    public static <V> V getLast(List<V> elements) {
        return elements.isEmpty() ? null : elements.get(getLastIndex(elements));
    }

    /**
     * Возвращает последний элемент списка, удаляя его из списка
     *
     * @param elements список, в котором извлекается последний элемент
     * @param <V> тип элементов в списке
     * @return последний элемент коллекции или null, если переданная коллекция пуста
     */
    @Nullable
    public static <V> V popLast(List<V> elements) {
        int lastIndex = getLastIndex(elements);
        return lastIndex == -1 ? null : elements.remove(lastIndex);
    }

    private static int getLastIndex(List<?> list) {
        return list.isEmpty() ? -1 : list.size() - 1;
    }

    /**
     * Возвращает исходную коллекцию, либо пустую коллекцию, если исходная - null
     *
     * @param collection преобразуемая коллекция
     * @param <V> тип элементов коллекции
     */
    public static <V> Collection<V> getOrEmpty(@Nullable Collection<V> collection) {
        return collection == null ? new ArrayList<>() : collection;
    }
}
