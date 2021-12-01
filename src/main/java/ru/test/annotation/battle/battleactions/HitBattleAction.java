package ru.test.annotation.battle.battleactions;

import ru.jengine.battlemodule.core.battlepresenter.BattleAction;

public class HitBattleAction implements BattleAction {
    private final int attacker;
    private final int target;
    private final int damagePoints;

    public HitBattleAction(int attacker, int target, int damagePoints) {
        this.attacker = attacker;
        this.target = target;
        this.damagePoints = damagePoints;
    }

    public int getAttacker() {
        return attacker;
    }

    public int getDamagePoints() {
        return damagePoints;
    }

    public int getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "Hit {" +
                "attacker=" + attacker +
                ", target=" + target +
                ", damagePoints=" + damagePoints +
                '}';
    }
}
