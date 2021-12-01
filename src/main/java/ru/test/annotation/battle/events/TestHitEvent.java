package ru.test.annotation.battle.events;

import ru.jengine.battlemodule.core.events.BattleEvent;

public class TestHitEvent extends BattleEvent {
    private final int attacker;
    private final int target;
    private final int damagePoint;

    public TestHitEvent(int attacker, int target, int damagePoint) {
        this.attacker = attacker;
        this.target = target;
        this.damagePoint = damagePoint;
    }

    public int getDamagePoint() {
        return damagePoint;
    }

    public int getAttacker() {
        return attacker;
    }

    public int getTarget() {
        return target;
    }
}
