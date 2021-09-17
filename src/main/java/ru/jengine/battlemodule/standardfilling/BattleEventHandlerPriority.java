package ru.jengine.battlemodule.standardfilling;

public enum BattleEventHandlerPriority {
    INFORMATION(0),
    DATA_PREPARE(100),
    HANDLE(200),
    RESULT_INFORMATION(300);

    private final int priority;

    BattleEventHandlerPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
