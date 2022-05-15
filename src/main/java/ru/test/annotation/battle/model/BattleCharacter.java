package ru.test.annotation.battle.model;

import ru.jengine.battlemodule.standardfilling.dynamicmodel.DynamicModel;
import ru.jengine.battlemodule.standardfilling.visible.HasVision;

public class BattleCharacter extends DynamicModel implements HasHealth, HasVision {
    private int health;
    private int visionDistance;

    public BattleCharacter(int id, int maxHealth) {
        super(id, new BattleCharacterTypeStub());
        this.health = maxHealth;
        this.visionDistance = 5;
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
    public int getVisionDistance() {
        return visionDistance;
    }

    @Override
    public boolean hasVision() {
        return getVisionDistance() > 0;
    }

    @Override
    public void setVisionDistance(int visionDistance) {
        this.visionDistance = visionDistance;
    }

    @Override
    public boolean isDead() {
        return getHealth() <= 0;
    }
}
