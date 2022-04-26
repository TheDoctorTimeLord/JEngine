package ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.handlingconditions;

import java.util.List;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

public class CodeWithPathSuffixCondition extends CodeCondition {
    private final List<String> expectedPathSuffix;

    public CodeWithPathSuffixCondition(String expectedCode, List<String> expectedPathSuffix) {
        super(expectedCode);
        this.expectedPathSuffix = expectedPathSuffix;
    }

    @Override
    public boolean canHandle(BattleAttribute attribute) {
        List<String> path = attribute.getPath();

        if (!super.canHandle(attribute) || expectedPathSuffix.size() > path.size()) {
            return false;
        }

        for (int i = 0; i < expectedPathSuffix.size(); i++) {
            String originalPathPart = path.get(path.size() - i - 1);
            String expectedPathPart = expectedPathSuffix.get(expectedPathSuffix.size() - i - 1);

            if (!originalPathPart.equals(expectedPathPart)) {
                return false;
            }
        }

        return true;
    }
}
