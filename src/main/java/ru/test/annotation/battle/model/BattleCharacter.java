package ru.test.annotation.battle.model;

import ru.jengine.battlemodule.standardfilling.model.DynamicModel;

public class BattleCharacter extends DynamicModel implements HasHealth {
    public BattleCharacter(int id, int maxHealth) {
        super(id);
        setProperty("health", maxHealth);
        setProperty("vision", true);
    }

    @Override
    public int getHealth() {
        return getProperty("health");
    }

    @Override
    public void damage(int damagePoints) {
        int health = getHealth();
        setProperty("health", health - damagePoints);
    }

    @Override
    public boolean isDead() {
        return getHealth() <= 0;
    }
}
