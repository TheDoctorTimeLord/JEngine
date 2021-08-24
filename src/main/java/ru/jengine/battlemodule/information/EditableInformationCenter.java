package ru.jengine.battlemodule.information;

import ru.jengine.battlemodule.information.personalinfo.EditableBattleModelInfo;

public interface EditableInformationCenter extends InformationCenter {
    EditableBattleModelInfo getPersonalInfo(int personalId);
}
