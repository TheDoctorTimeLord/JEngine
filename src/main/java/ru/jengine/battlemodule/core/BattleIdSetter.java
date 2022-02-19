package ru.jengine.battlemodule.core;

/**
 * Устанавливает для ID боя. Нужен для различия боёв в пределах одной игровой сессии
 */
public interface BattleIdSetter {
    /**
     * Устанавливает ID для боя
     * @param battleId ID боя
     */
    void setBattleId(String battleId);
}
