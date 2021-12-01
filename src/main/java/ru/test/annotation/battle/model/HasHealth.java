package ru.test.annotation.battle.model;

public interface HasHealth {
    int getHealth();
    void damage(int damagePoints);
    boolean isDead();
}
