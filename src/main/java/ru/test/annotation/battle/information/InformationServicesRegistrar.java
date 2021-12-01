package ru.test.annotation.battle.information;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.contentregistrars.AbstractContentRegistrar;

@BattleBeanPrototype
public class InformationServicesRegistrar extends AbstractContentRegistrar {
    @Override
    protected void registerInt() {
        registerInformationService(NextCellScannerService.class, new NextCellScannerService(battleContext.getBattleState()));
    }
}
