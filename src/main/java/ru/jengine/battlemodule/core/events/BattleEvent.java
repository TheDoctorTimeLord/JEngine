package ru.jengine.battlemodule.core.events;

import ru.jengine.eventqueue.event.Event;

public abstract class BattleEvent implements Event {
    private String battleId;

    public String getBattleId() {
        return battleId;
    }

    public void setBattleId(String battleId) {
        this.battleId = battleId;
    }
}
