package ru.jengine.battlemodule.core.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ru.jengine.battlemodule.core.containers.ItemContainersManager;
import ru.jengine.battlemodule.core.exceptions.BattleException;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.models.HasPosition;
import ru.jengine.battlemodule.core.models.ObjectType;
import ru.jengine.battlemodule.core.serviceclasses.Point;

/**
 * Хранит всю необходимую информацию о текущем состоянии боя
 */
public class BattleState {
    private final Map<Integer, BattleModel> battleModelById;
    private final Map<Point, List<Integer>> battleModelOnField;
    private final Set<Integer> dynamicObjects;
    private final Set<String> availableObjectTypeNames;
    private final ItemContainersManager containersManager;
    private final BattlefieldLimitersManager battlefieldLimitersManager;

    /**
     * @param battleModelById сопоставление всех объектов в бою с их ID
     * @param battleModelOnField расположение всех объектов на поле боя (объекты представлены их ID)
     * @param dynamicObjects список ID объектов, которые являются динамическими (могут совершать действия)
     * @param availableObjectTypes Все доступные типы объектов в бою. Этот список обязан содержать типы всех
     *                             переданных объектов в этот конструктор (включая предметы). Может содержать
     *                             больше типов, если они будут использованы в бою
     * @param containersManager руководитель всех контейнеров в бою
     * @param battlefieldLimitersManager ограничители поля боя
     */
    BattleState(Map<Integer, BattleModel> battleModelById,
            Map<Point, List<Integer>> battleModelOnField, Collection<Integer> dynamicObjects,
            Set<String> availableObjectTypes, ItemContainersManager containersManager,
            BattlefieldLimitersManager battlefieldLimitersManager)
    {
        this.battleModelById = battleModelById;
        this.battleModelOnField = battleModelOnField;
        this.dynamicObjects = new HashSet<>(dynamicObjects);
        this.availableObjectTypeNames = availableObjectTypes;
        this.containersManager = containersManager;
        this.battlefieldLimitersManager = battlefieldLimitersManager;
    }

    /**
     * Регистрирует тип объекта в бою
     * @param objectType общий тип, характеризующий группу объектов в бою
     */
    public void registerAvailableObjectType(ObjectType objectType) {
        availableObjectTypeNames.add(objectType.getObjectTypeName());
    }

    /**
     * Имена доступных типов объектов в бою. В бою не может появиться объекта, тип которого не зарегистрирован в
     * {@link BattleState}
     */
    public Set<String> getAvailableObjectTypeNames() {
        return availableObjectTypeNames;
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
        return battlefieldLimitersManager.getBattlefieldCells();
    }

    /**
     * Проверяет, находится ли точка в пределах поля боя
     * @param point проверяемая точка
     * @return true - если точка находится в пределах поля боя, false - иначе
     */
    public boolean inBattlefieldBound(Point point) {
        return battlefieldLimitersManager.inBattlefieldBound(point);
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
        checkType(battleModel.getBattleModelType());

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

    private void checkType(ObjectType objectType) {
        String typeName = objectType.getObjectTypeName();
        if (!availableObjectTypeNames.contains(typeName)) {
            throwTypeNotFoundException(typeName);
        }
    }

    static void throwTypeNotFoundException(String startedObjectTypeName) {
        throw new BattleException(("Type [%s] doesn't contain in available object types, but it bind to some object")
                .formatted(startedObjectTypeName));
    }
}
