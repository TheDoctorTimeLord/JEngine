package ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.handlingconditions;

import java.util.List;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

/**
 * Условие, исходящее из кода атрибута и префикса в пути этого атрибута. Если код атрибута совпадает с заданным и
 * имеет заданный префикс пути, то такой атрибут можно обработать
 */
public class CodeWithPathPrefixCondition extends CodeCondition {
    private final List<String> expectedPathPrefix;

    public CodeWithPathPrefixCondition(String expectedCode, List<String> expectedPathPrefix) {
        super(expectedCode);
        this.expectedPathPrefix = expectedPathPrefix;
    }

    @Override
    public boolean canHandle(BattleAttribute attribute) {
        List<String> path = attribute.getPath();

        if (!super.canHandle(attribute) || expectedPathPrefix.size() > path.size()) {
            return false;
        }

        for (int i = 0; i < expectedPathPrefix.size(); i++) {
            String originalPathPart = path.get(i);
            String expectedPathPart = expectedPathPrefix.get(i);

            if (!originalPathPart.equals(expectedPathPart)) {
                return false;
            }
        }

        return true;
    }
}
