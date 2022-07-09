package ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive;

import static ru.jengine.utils.CollectionUtils.addLast;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.core.containers.ItemContainer;
import ru.jengine.battlemodule.core.containers.ItemRuntimeException;
import ru.jengine.battlemodule.core.containers.ItemStack;
import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.battlemodule.core.modelattributes.AttributeStore;
import ru.jengine.battlemodule.core.modelattributes.AttributesContainer;
import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.battleattributes.events.PutAttributeNotification;

/**
 * Реализация {@link UpdatableItemContainerService}
 */
public class ItemContainerServiceImpl implements UpdatableItemContainerService {
    private final Map<Integer, ModelContainerData> modelsContainerData = new HashMap<>();
    private final BattleState state;
    private final DispatcherBattleWrapper dispatcher;

    public ItemContainerServiceImpl(BattleState state, DispatcherBattleWrapper dispatcher) {
        this.state = state;
        this.dispatcher = dispatcher;
    }

    @Override
    public Collection<ItemAggregator> getEquippedItems(int modelId) {
        AttributesContainer container = state.resolveId(modelId).getAttributes();
        return modelsContainerData.get(modelId).equippedItems.values().stream()
                .map(path -> (ItemAggregator)container.getAttributeByPath(path))
                .filter(Objects::nonNull)
                .filter(itemAggregator -> itemAggregator.getItemData() != null)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemContainerAggregator> getEquippedContainers(int modelId) {
        AttributesContainer container = state.resolveId(modelId).getAttributes();
        return modelsContainerData.get(modelId).equippedContainers.values().stream()
                .map(path -> (ItemContainerAggregator)container.getAttributeByPath(path))
                .filter(Objects::nonNull)
                .filter(itemContainerAggregator -> itemContainerAggregator.getItemContainerData() != null)
                .collect(Collectors.toList());
    }

    @Override
    public void initialize(BattleState preparedBattleState) { //TODO не уверен, что это должно быть тут
        for (BattleModel model : preparedBattleState.getModelsInBattle()) {
            for (BattleAttribute attribute : model.getAttributes().getAttributes()) {
                analyzeAttributeRecursively(model.getId(), attribute);
            }
        }
    }

    private void analyzeAttributeRecursively(int modelId, BattleAttribute attribute) {
        if (attribute instanceof ItemAggregator itemAggregator) {
            getContainerData(modelId).equippedItems
                    .put(
                            itemAggregator.getItemData(),
                            addLast(itemAggregator.getPath(), itemAggregator.getCode())
                    );
        }
        else if (attribute instanceof ItemContainerAggregator itemContainerAggregator) {
            getContainerData(modelId).equippedContainers
                    .put(
                            itemContainerAggregator.getItemContainerData(),
                            addLast(itemContainerAggregator.getPath(), itemContainerAggregator.getCode())
                    );
        }
        else if (attribute instanceof AttributeStore<?> store) {
            for (BattleAttribute innerAttribute : store.getAttributes()) {
                analyzeAttributeRecursively(modelId, innerAttribute);
            }
        }
    }

    @Override
    public void equipItem(int modelId, String itemPlaceName, AttributeStore<?> equippedPlace, String containerCode,
            int itemIndex)
    {
        ModelContainerData personData = getContainerData(modelId);
        ItemContainer container = getItemContainer(containerCode);
        String itemType = extractItemStack(container, itemIndex).getItem().getItemType();

        ItemData itemData = new ItemData(containerCode, itemIndex, itemType);
        ItemAggregator itemAggregator = getOrCreateItemAggregator(equippedPlace, itemPlaceName);
        itemAggregator.setItemData(itemData);

        personData.equippedItems.put(itemData, addLast(itemAggregator.getPath(), itemAggregator.getCode()));

        dispatcher.handle(new PutAttributeNotification(modelId, itemAggregator));
    }

    private static ItemAggregator getOrCreateItemAggregator(AttributeStore<?> equippedPlace, String itemPlaceName) {
        ItemAggregator itemAggregator = equippedPlace.get(itemPlaceName);

        if (itemAggregator == null) {
            itemAggregator = new ItemAggregator(itemPlaceName, null);
            equippedPlace.add(itemAggregator);
        }

        return itemAggregator;
    }

    @Nullable
    @Override
    public ItemAggregator takeOffItem(int modelId, String containerCode, int itemIndex) {
        ModelContainerData containerData = getContainerData(modelId);
        ItemContainer container = getItemContainer(containerCode);
        String itemType = extractItemStack(container, itemIndex).getItem().getItemType();
        List<String> pathToItem = containerData.equippedItems.remove(new ItemData(containerCode, itemIndex, itemType));

        if (pathToItem != null) {
            AttributesContainer attributesContainer = state.resolveId(modelId).getAttributes();
            ItemAggregator itemAggregator = attributesContainer.getAttributeByPath(pathToItem);

            if (itemAggregator != null) {
                itemAggregator.setItemData(null);
                dispatcher.handle(new PutAttributeNotification(modelId, itemAggregator));
                return itemAggregator;
            }
        }

        return null;
    }

    @Override
    public void equipItemContainer(int modelId, String containerPlaceName, String containerCode,
            AttributeStore<?> equippedPlace) //TODO объединить с equipItem?
    {
        ModelContainerData personData = getContainerData(modelId);

        ItemContainerData containerData = new ItemContainerData(containerCode);
        ItemContainerAggregator containerAggregator = getOrCreateItemContainerAggregator(equippedPlace, containerPlaceName);
        containerAggregator.setItemContainerData(containerData);

        personData.equippedContainers.put(containerData, addLast(containerAggregator.getPath(), containerAggregator.getCode()));

        dispatcher.handle(new PutAttributeNotification(modelId, containerAggregator));
    }

    private static ItemContainerAggregator getOrCreateItemContainerAggregator(AttributeStore<?> equippedPlace,
            String itemPlaceName)
    {
        ItemContainerAggregator itemContainerAggregator = equippedPlace.get(itemPlaceName);

        if (itemContainerAggregator == null) {
            itemContainerAggregator = new ItemContainerAggregator(itemPlaceName, null);
            equippedPlace.add(itemContainerAggregator);
        }

        return itemContainerAggregator;
    }

    @Nullable
    @Override
    public ItemContainerAggregator takeOffItemContainer(int modelId, String containerCode) {
        ModelContainerData containerData = getContainerData(modelId);
        List<String> pathToItem = containerData.equippedContainers.remove(new ItemContainerData(containerCode));

        if (pathToItem != null) {
            AttributesContainer attributesContainer = state.resolveId(modelId).getAttributes();
            ItemContainerAggregator containerAggregator = attributesContainer.getAttributeByPath(pathToItem);

            if (containerAggregator != null) {
                containerAggregator.setItemContainerData(null);
                dispatcher.handle(new PutAttributeNotification(modelId, containerAggregator));
                return containerAggregator;
            }
        }

        return null;
    }

    private ItemContainer getItemContainer(String containerCode) {
        ItemContainer container = state.getContainersManager().getContainer(containerCode);

        if (container == null) {
            throw new ItemRuntimeException("Container with code [%s] does not exist".formatted(containerCode));
        }

        return container;
    }

    private ModelContainerData getContainerData(int modelId) {
        return modelsContainerData.computeIfAbsent(modelId, id -> new ModelContainerData());
    }

    private static ItemStack extractItemStack(ItemContainer container, int itemIndex) {
        ItemStack itemStack = container.getItems(itemIndex);

        if (itemStack == null) {
            throw new ItemRuntimeException("Container [%s] doesn't have item with index [%s]"
                    .formatted(container, itemIndex));
        }

        return itemStack;
    }

    private static class ModelContainerData {
        private final Map<ItemData, List<String>> equippedItems = new HashMap<>();
        private final Map<ItemContainerData, List<String>> equippedContainers = new HashMap<>();
    }
}
