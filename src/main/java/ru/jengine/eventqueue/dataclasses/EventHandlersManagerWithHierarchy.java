package ru.jengine.eventqueue.dataclasses;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.event.PostHandler;
import ru.jengine.eventqueue.event.PreHandler;
import ru.jengine.utils.hierarchywalker.HierarchyElement;
import ru.jengine.utils.hierarchywalker.WalkerIterator;
import ru.jengine.utils.hierarchywalker.WalkerIteratorBuilder;
import ru.jengine.utils.serviceclasses.SortedMultiset;

public class EventHandlersManagerWithHierarchy implements EventHandlersManager {
    //TODO подготовить для многопоточки
    //TODO протестировать прототип
    private final TreeElement root;

    public EventHandlersManagerWithHierarchy(List<PreHandler<?>> preHandlers) {
        this.root = new TreeElement();
        this.root.addChildNode(Event.class, new TreeElement());

        for (PreHandler<?> preHandler : preHandlers) {
            registerPreHandler(preHandler);
        }
    }

    private void registerPreHandler(PreHandler<?> preHandler) {
        walkWithAppendHierarchy(preHandler.getHandlingEventType(), node -> node.addPreHandler(preHandler));
    }

    @Override
    public void registerPostHandler(PostHandler<?> postHandler) {
        walkWithAppendHierarchy(postHandler.getHandlingEventType(), node -> node.addPostHandler(postHandler));
    }

    @Override
    public void removePostHandler(PostHandler<?> postHandler) {
        walkWithAppendHierarchy(postHandler.getHandlingEventType(), node -> node.removePostHandler(postHandler));
    }

    @Override
    public List<PreHandler<Event>> getPreHandlers(Event event) {
        List<PreHandler<Event>> preHandlers = new ArrayList<>();
        walkAroundHierarchy(event.getClass(), node -> preHandlers.addAll((Collection<? extends PreHandler<Event>>)node.preHandlers));
        return preHandlers;
    }

    @Override
    public List<PostHandler<Event>> getPostHandlers(Event event) {
        List<PostHandler<Event>> postHandlers = new ArrayList<>();
        walkAroundHierarchy(event.getClass(), node -> postHandlers.addAll((Collection<? extends PostHandler<Event>>)node.postHandlers.getSortedElements()));
        return postHandlers;
    }

    private void walkWithAppendHierarchy(Class<?> targetEventType, Consumer<TreeElement> onLastHierarchyNode) {
        TreeElement currentNode = root;
        for (Class<?> eventType : getHierarchyFromRoot(targetEventType)) {
            currentNode = currentNode.computeChildNode(eventType);
        }
        onLastHierarchyNode.accept(currentNode);
    }

    private void walkAroundHierarchy(Class<?> targetEventType, Consumer<TreeElement> onEachHierarchyNode) {
        TreeElement currentNode = root;
        for (Class<?> eventType : getHierarchyFromRoot(targetEventType)) {
            currentNode = currentNode.computeChildNode(eventType);
            onEachHierarchyNode.accept(currentNode);
        }
    }

    private static List<Class<?>> getHierarchyFromRoot(Class<?> targetEventType) {
        Deque<Class<?>> hierarchyStack = new ArrayDeque<>();

        WalkerIterator iterator = WalkerIteratorBuilder.builder(targetEventType)
                .withTypeChecking(false)
                .withGenericMapping(false)
                .onGoBackByStarted(hierarchyElement -> hierarchyStack.pollLast())
                .build();

        while (iterator.hasNext()) {
            HierarchyElement nextElement = iterator.next();

            if (nextElement.getCurrentElement().equals(Event.class)) {
                break;
            }

            hierarchyStack.addLast(nextElement.getCurrentElement());
        }

        List<Class<?>> hierarchy = new ArrayList<>();
        while (!hierarchyStack.isEmpty()) {
            hierarchy.add(hierarchyStack.pollLast());
        }
        return hierarchy;
    }

    private static class TreeElement {
        private final List<PreHandler<?>> preHandlers = new ArrayList<>();
        private final SortedMultiset<PostHandler<?>> postHandlers = new SortedMultiset<>();
        private final Map<Class<?>, TreeElement> hierarchy = new HashMap<>();

        public void addPreHandler(PreHandler<?> preHandler) {
            preHandlers.add(preHandler);
        }

        public void addPostHandler(PostHandler<?> postHandler) {
            postHandlers.add(postHandler);
        }

        public void removePostHandler(PostHandler<?> postHandler) {
            postHandlers.remove(postHandler);
        }

        public void addChildNode(Class<?> childClass, TreeElement child) {
            hierarchy.put(childClass, child);
        }

        public TreeElement computeChildNode(Class<?> childClass) {
            return hierarchy.computeIfAbsent(childClass, c -> new TreeElement());
        }
    }
}
