package ru.jengine.battlemodule.information.personalinfo;

import java.util.ArrayList;
import java.util.List;

public class EditableBattleModelInfo implements BattleModelInfo {
    private final List<BattleInfo> personalInfo = new ArrayList<>();

    @Override
    public List<BattleInfo> getPersonalInfos() {
        return new ArrayList<>(personalInfo);
    }

    public void addInfo(BattleInfo info) {
        personalInfo.add(info);
    }

    public void clearInfos() {
        personalInfo.clear();
    }
}
