package ru.test.annotation.battle.model;

import ru.jengine.battlemodule.standardfilling.model.DynamicModel;

public class BattleCharacter extends DynamicModel implements HasHealth {
    private int health;

    public BattleCharacter(int id, int maxHealth) {
        super(id);
        this.health = maxHealth;
        setVision(true);
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void damage(int damagePoints) {
        int health = getHealth();
        this.health = health - damagePoints;
    }

    @Override
    public boolean isDead() {
        return getHealth() <= 0;
    }
}
