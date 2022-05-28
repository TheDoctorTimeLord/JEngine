package ru.jengine.battlemodule.core.state;

import java.util.Collection;
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
import ru.jengine.battlemodule.core.models.ObjectType;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.utils.CollectionUtils;

public class BattleStateBuilder {
    private final Map<Integer, BattleModel> battleModelById;
    private final Map<Point, List<Integer>> battleModelOnField;
    private final Collection<Integer> dynamicObjects;
    private final BattlefieldLimitersManager battlefieldLimitersManager = new BattlefieldLimitersManager();
    private Set<String> availableObjectTypeNames;
    private ItemContainersManager containersManager = new ItemContainersManager();

    public static BattleStateBuilder createByState(Map<Integer, BattleModel> battleModelById,
            Map<Point, List<Integer>> battleModelOnField, Collection<Integer> dynamicObjects)
    {
        return new BattleStateBuilder(battleModelById, battleModelOnField, dynamicObjects);
    }

    public static BattleStateBuilder createByModels(Collection<BattleModel> staticModels, Collection<BattleModel> dynamicModels)
    {
        Map<Integer, BattleModel> battleModelById = Stream.concat(staticModels.stream(), dynamicModels.stream())
                .collect(Collectors.toMap(BattleModel::getId, battleModel -> battleModel));
        List<BattleModel> modelsWithPosition = Stream.concat(staticModels.stream(), dynamicModels.stream())
                .filter(battleModel -> battleModel instanceof HasPosition)
                .filter(battleModel -> ((HasPosition)battleModel).hasPosition())
                .toList();
        Map<Point, List<Integer>> battleModelOnField =
                CollectionUtils.groupBy(modelsWithPosition, m -> ((HasPosition)m).getPosition())
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Entry::getKey,
                                e -> e.getValue().stream()
                                        .map(BattleModel::getId)
                                        .collect(Collectors.toList())
                        ));

        Set<Integer> dynamicObjects = dynamicModels.stream()
                .map(BattleModel::getId)
                .collect(Collectors.toSet());

        return new BattleStateBuilder(battleModelById, battleModelOnField, dynamicObjects);
    }

    private BattleStateBuilder(Map<Integer, BattleModel> battleModelById,
            Map<Point, List<Integer>> battleModelOnField, Collection<Integer> dynamicObjects) {
        this.battleModelById = battleModelById;
        this.battleModelOnField = battleModelOnField;
        this.dynamicObjects = dynamicObjects;
    }

    public BattleStateBuilder itemContainerManager(ItemContainersManager itemContainersManager) {
        this.containersManager = itemContainersManager;
        checkTypes(containersManager, getAvailableObjectTypeNames());

        return this;
    }

    public BattleStateBuilder battlefieldLimiters(BattlefieldLimiter... battlefieldLimiters) {
        this.battlefieldLimitersManager.setBattlefieldLimiters(battlefieldLimiters);

        for (Entry<Point, List<Integer>> entry : battleModelOnField.entrySet()) {
            Point point = entry.getKey();
            if (!battlefieldLimitersManager.inBattlefieldBound(point)) {
                String objects = String.join(", ", entry.getValue().stream()
                        .map(battleModelById::get)
                        .map(BattleModel::getBattleModelType)
                        .map(ObjectType::getObjectTypeName)
                        .toList());
                throw new BattleException("Objects [%s] are not in battlefield. Position [%s]"
                        .formatted(objects, point));
            }
        }

        return this;
    }

    public BattleStateBuilder availableObjectTypes(List<ObjectType> objectTypes) {
        this.availableObjectTypeNames = objectTypes.stream()
                .map(ObjectType::getObjectTypeName)
                .collect(Collectors.toSet());

        checkTypes(battleModelById.values(), availableObjectTypeNames);
        checkTypes(containersManager, availableObjectTypeNames);

        return this;
    }

    public BattleState build() {
        return new BattleState(battleModelById, battleModelOnField, dynamicObjects, getAvailableObjectTypeNames(),
                containersManager, battlefieldLimitersManager);
    }

    private Set<String> getAvailableObjectTypeNames() {
        if (availableObjectTypeNames == null) {
            availableObjectTypeNames =
                    Stream.concat(
                            battleModelById.values().stream().map(BattleModel::getBattleModelType),
                            containersManager.getContainedItems().stream()
                    )
                    .map(ObjectType::getObjectTypeName)
                    .collect(Collectors.toSet());
        }
        return availableObjectTypeNames;
    }

    private static void checkTypes(Collection<BattleModel> battleModels, Set<String> objectTypes)
    {
        List<String> startedObjectTypeNames = battleModels.stream()
                .map(battleModel -> battleModel.getBattleModelType().getObjectTypeName())
                .toList();

        checkTypes(startedObjectTypeNames, objectTypes);
    }

    private static void checkTypes(ItemContainersManager containersManager, Set<String> objectTypes)
    {
        List<String> startedObjectTypeNames = containersManager.getContainedItems().stream()
                .map(ObjectType::getObjectTypeName)
                .toList();

        checkTypes(startedObjectTypeNames, objectTypes);
    }

    private static void checkTypes(List<String> startedObjectTypeNames, Set<String> objectTypes) {
        for (String startedObjectTypeName : startedObjectTypeNames) {
            if (!objectTypes.contains(startedObjectTypeName)) {
                BattleState.throwTypeNotFoundException(startedObjectTypeName);
            }
        }
    }
}
