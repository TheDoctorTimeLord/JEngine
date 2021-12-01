package ru.test.annotation.battle.commands.rotate;

import java.util.Set;

import ru.jengine.battlemodule.core.commands.CommandExecutionParameters;

import com.google.common.collect.Sets;

public class RotateParameters implements CommandExecutionParameters {
    private final Set<RotateOption> options = Sets.newHashSet(RotateOption.LEFT, RotateOption.RIGHT);
    private RotateOption answer;

    public enum RotateOption {
        LEFT, RIGHT
    }

    public RotateOption getAnswer() {
        return answer;
    }

    public Set<RotateOption> getAvailableOptions() {
        return options;
    }

    public RotateParameters setAnswer(RotateOption answer) {
        this.answer = answer;
        return this;
    }
}
