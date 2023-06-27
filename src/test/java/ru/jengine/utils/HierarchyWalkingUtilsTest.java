package ru.jengine.utils;

import org.junit.Assert;
import org.junit.Test;

import ru.jengine.utils.hierarchywalker.events.EventChild1;
import ru.jengine.utils.hierarchywalker.events.EventChild1Handler;
import ru.jengine.utils.hierarchywalker.events.EventHandler;

public class HierarchyWalkingUtilsTest {
    @Test
    public void testFindEventType() {
        EventChild1Handler eventChild1Handler = new EventChild1Handler();
        Assert.assertEquals(eventChild1Handler.getHandledEventType(), EventChild1.class);
    }

    @Test
    public void testUnnamedEventHandler() {
        EventHandler<EventChild1> eventChild1Handler = new EventHandler<EventChild1>() {
            @Override
            public void handle(EventChild1 event) { }
        };
        Assert.assertEquals(eventChild1Handler.getHandledEventType(), EventChild1.class);
    }

    @Test
    public void testLambdaEventHandler() {
        EventHandler<EventChild1> eventHandler = event -> {};
        Assert.assertNull(eventHandler.getHandledEventType());
    }
}
