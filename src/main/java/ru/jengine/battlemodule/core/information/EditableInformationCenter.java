package ru.jengine.battlemodule.core.information;

import ru.jengine.battlemodule.core.information.personalinfo.EditableBattleModelInfo;

public interface EditableInformationCenter extends InformationCenter {
    EditableBattleModelInfo getPersonalInfo(int personalId);
}
