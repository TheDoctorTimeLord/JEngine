package ru.jengine.battlemodule.core.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.jengine.battlemodule.core.containers.ItemContainersManager;
import ru.jengine.battlemodule.core.exceptions.BattleException;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.models.HasPosition;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.utils.CollectionUtils;

/**
 * Хранит всю необходимую информацию о текущем состоянии боя
 */
public class BattleState {
    private final Map<Integer, BattleModel> battleModelById;
    private final Map<Point, List<Integer>> battleModelOnField;
    private final Set<Integer> dynamicObjects;
    private final ItemContainersManager containersManager;
    private final BattlefieldLimiter[] battlefieldLimiters;

    /**
     * @param battleModelById сопоставление всех объектов в бою с их ID
     * @param battleModelOnField расположение всех объектов на поле боя (объекты представлены их ID)
     * @param dynamicObjects список ID объектов, которые являются динамическими (могут совершать действия)
     * @param containersManager руководитель всех контейнеров в бою
     * @param battlefieldLimiters ограничители поля боя
     */
    public BattleState(Map<Integer, BattleModel> battleModelById,
            Map<Point, List<Integer>> battleModelOnField, Collection<Integer> dynamicObjects,
            ItemContainersManager containersManager, BattlefieldLimiter... battlefieldLimiters)
    {
        this.battleModelById = battleModelById;
        this.battleModelOnField = battleModelOnField;
        this.dynamicObjects = new HashSet<>(dynamicObjects);
        this.containersManager = containersManager;
        this.battlefieldLimiters = battlefieldLimiters;
    }

    /**
     * @param battleModelById сопоставление всех объектов в бою с их ID
     * @param battleModelOnField расположение всех объектов на поле боя (объекты представлены их ID)
     * @param dynamicObjects список ID объектов, которые являются динамическими (могут совершать действия)
     * @param battlefieldLimiters ограничители поля боя
     */
    public BattleState(Map<Integer, BattleModel> battleModelById, Map<Point, List<Integer>> battleModelOnField,
            Collection<Integer> dynamicObjects, BattlefieldLimiter... battlefieldLimiters)
    {
        this(battleModelById, battleModelOnField, dynamicObjects, new ItemContainersManager(), battlefieldLimiters);
    }

    /**
     * @param staticModels все модели статических (тех, кто НЕ может влиять на мир) объектов
     * @param dynamicModels все модели статических (тех, кто влиять на мир) объектов
     * @param battlefieldLimiters ограничители поля боя
     */
    public BattleState(Collection<BattleModel> staticModels, Collection<BattleModel> dynamicModels,
            BattlefieldLimiter... battlefieldLimiters)
    {
        this(staticModels, dynamicModels, new ItemContainersManager(), battlefieldLimiters);
    }

    /**
     * @param staticModels все модели статических (тех, кто НЕ может влиять на мир) объектов
     * @param dynamicModels все модели статических (тех, кто влиять на мир) объектов
     * @param containersManager руководитель всех контейнеров в бою
     * @param battlefieldLimiters ограничители поля боя
     */
    public BattleState(Collection<BattleModel> staticModels, Collection<BattleModel> dynamicModels,
            ItemContainersManager containersManager, BattlefieldLimiter... battlefieldLimiters)
    {
        this.battleModelById = Stream.concat(staticModels.stream(), dynamicModels.stream())
                .collect(Collectors.toMap(BattleModel::getId, battleModel -> battleModel));
        List<BattleModel> modelsWithPosition = Stream.concat(staticModels.stream(), dynamicModels.stream())
                .filter(battleModel -> battleModel instanceof HasPosition)
                .filter(battleModel -> ((HasPosition)battleModel).hasPosition())
                .toList();
        this.battleModelOnField = CollectionUtils.groupBy(modelsWithPosition, m -> ((HasPosition)m).getPosition())
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Entry::getKey,
                        e -> e.getValue().stream()
                                .map(BattleModel::getId)
                                .collect(Collectors.toList())
                ));
        this.dynamicObjects = dynamicModels.stream()
                .map(BattleModel::getId)
                .collect(Collectors.toSet());
        this.containersManager = containersManager;
        this.battlefieldLimiters = battlefieldLimiters;
    }

    /**
     * Все объекты в бою (как статические, так и динамические)
     */
    public Collection<BattleModel> getModelsInBattle() {
        return battleModelById.values();
    }

    /**
     * Возвращает объект, соответствующий переданному ID
     * @param id ID объекта
     * @throws BattleException если объекта, по переданному ID не было найдено
     */
    public BattleModel resolveId(int id) {
        BattleModel model = battleModelById.get(id);

        if (model == null) {
            throw new BattleException("Battle model with id [" + id + "] was not found");
        }

        return model;
    }

    /**
     * Возвращает объект, соответствующих переданном ID
     * @param ids список ID объектов
     * @return список объектов в порядке переданных ID
     * @throws BattleException если хотя бы один ID не был найден
     */
    public List<BattleModel> resolveIds(List<Integer> ids) {
        return ids.stream()
                .map(this::resolveId)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает все объекты на поля боя. Данные хранятся в виде "Клетка поля боя -> список ID объектов на этой клетке"
     */
    public Map<Point, List<Integer>> getMapFilling() {
        return battleModelOnField;
    }

    /**
     * Возвращает список ID объектов, которые находятся на указанной клетке поля боя
     * @param point координаты клетки на поле боя
     * @return список ID объектов на указанной клетке (если объектов на клетке нет, то список будет пустой)
     */
    public List<Integer> getOnPosition(Point point) {
        List<Integer> models = battleModelOnField.get(point);

        if (models != null && models.isEmpty()) {
            battleModelOnField.remove(point);
        }
        if (models == null || models.isEmpty()) {
            return Collections.emptyList();
        }

        return new ArrayList<>(models);
    }

    /**
     * Получение списка боевых объектов в указанной точке поля боя
     * @param point точка, из которой будут получены объекты
     * @return Список боевых объектов. Если объектов на позиции нет, то вернётся пустой список
     */
    public List<BattleModel> getModelsOnPosition(Point point) {
        return resolveIds(getOnPosition(point));
    }

    /**
     * Получение главного менеджера всех контейнеров в бою
     */
    public ItemContainersManager getContainersManager() {
        return containersManager;
    }

    /**
     * Возвращает все клетки в пределах поля боя.
     */
    public Set<Point> getBattlefieldCells() {
        Set<Point> battlefield = new HashSet<>();

        for (BattlefieldLimiter limiter : battlefieldLimiters) {
            battlefield.addAll(limiter.getPointsInBound());
        }

        return battlefield;
    }

    /**
     * Проверяет, находится ли точка в пределах поля боя
     * @param point проверяемая точка
     * @return true - если точка находится в пределах поля боя, false - иначе
     */
    public boolean inBattlefieldBound(Point point) {
        for (BattlefieldLimiter limiter : battlefieldLimiters) {
            if (limiter.inBound(point)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Удаляет объект с переданной клетки поля боя
     * @param position координаты клетки поля боя
     * @param id ID удаляемого объекта
     */
    public void removeFromPosition(Point position, int id) {
        List<Integer> onCell = battleModelOnField.get(position);

        if (onCell == null) {
            throw new BattleException("Battle model with id [" + id + "] can not stay on [" + position + "]");
        }

        onCell.remove((Integer)id);

        if (onCell.isEmpty()) {
            battleModelOnField.remove(position);
        }
    }

    /**
     * Добавляет объект на переданную позицию поля боя
     * @param position координаты клетки поля боя
     * @param id ID добавляемого объекта
     */
    public void setToPosition(Point position, int id) {
        getOnPositionEditable(position).add(id);
    }

    private List<Integer> getOnPositionEditable(Point point) {
        return battleModelOnField.computeIfAbsent(point, p -> new ArrayList<>());
    }

    public List<BattleModel> getDynamicObjects() {
        return dynamicObjects.stream()
                .map(this::resolveId)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает ID всех динамических объектов в бою
     */
    public List<Integer> getDynamicObjectIds() {
        return new ArrayList<>(dynamicObjects);
    }

    /**
     * Добавляет динамический объект в бой
     * @param battleModel добавляемый объект
     */
    public void addDynamicObject(BattleModel battleModel) {
        int id = battleModel.getId();

        dynamicObjects.add(id);
        battleModelById.put(id, battleModel);

        Point position;
        if (battleModel instanceof HasPosition && (position = ((HasPosition)battleModel).getPosition()) != null) {
            setToPosition(position, id);
        }
    }

    /**
     * Удаляет объект из боя
     * @param id ID объекта
     */
    public void removeObject(int id) {
        dynamicObjects.remove(id);
        BattleModel model = battleModelById.remove(id);

        if (model instanceof HasPosition hasPosition) {
            Point position = hasPosition.getPosition();
            if (position != null) {
                removeFromPosition(position, id);
            }
        }
    }
}
