package ru.test.annotation.battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import ru.jengine.battlemodule.EnableBattleCoreWithStandardFilling;
import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.BattleGenerator;
import ru.jengine.battlemodule.core.BattleMasterImpl;
import ru.jengine.battlemodule.core.IdGenerator;
import ru.jengine.battlemodule.core.battlepresenter.BattleAction;
import ru.jengine.battlemodule.core.battlepresenter.BattleActionPresenter;
import ru.jengine.battlemodule.core.battlepresenter.BattlePresenterActionSubscriber;
import ru.jengine.battlemodule.core.battlepresenter.SubscribeStrategy;
import ru.jengine.battlemodule.core.battlepresenter.SubscribeType;
import ru.jengine.battlemodule.core.behaviors.BehaviorObjectsManager;
import ru.jengine.battlemodule.core.commands.BattleCommandRegistrar;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.serviceclasses.Direction;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.core.state.BattlefieldLimiter;
import ru.jengine.battlemodule.standardfilling.movement.CanMoved;
import ru.jengine.beancontainer.BeanContainer;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Context;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.dataclasses.ContainerConfiguration;
import ru.jengine.beancontainer.implementation.JEngineContainer;
import ru.jengine.beancontainer.implementation.moduleimpls.AnnotationModule;
import ru.jengine.beancontainer.service.Constants.Contexts;
import ru.jengine.utils.CollectionUtils;
import ru.test.annotation.battle.model.BattleCharacter;
import ru.test.annotation.battle.model.HasHealth;

@ContainerModule
@Context(Contexts.BATTLE_CONTEXT)
@PackageScan("ru.test.annotation.battle")
@EnableBattleCoreWithStandardFilling
public class TestBattle extends AnnotationModule {
    public static final int MAP_SIZE = 5;

    public static void main(String[] args) {
        BeanContainer container = new JEngineContainer();
        container.initializeCommonContexts(ContainerConfiguration.build(TestBattle.class).addAdditionalBean(container));

        BattleMasterContext battle1 = createBattle(container);
        BattleMasterContext battle2 = createBattle(container);
        BattleMasterContext battle3 = createBattle(container);

        BattleMasterContext[] battles = new BattleMasterContext[] { battle1, battle2, battle3 };

        drawBattles(Arrays.stream(battles)
                .map(BattleMasterContext::getBattleMaster)
                .toArray(BattleMasterImpl[]::new));

        AtomicInteger battleCounter = new AtomicInteger(battles.length);

        while (battleCounter.get() > 0) {
//            Thread.sleep(2000);
            turnBattles(battles);

            Arrays.stream(battles)
                    .filter(BattleMasterContext::isRunning)
                    .filter(b -> b.getBattleMaster().getContext().getBattleDynamicObjectsManager().getAllCharacters().size() <= 1)
                    .forEach(b -> {
                        battleCounter.getAndDecrement();
                        b.getBattleMaster().stopBattle();
                        b.setCompleted();
                    });
        }

        System.out.println("Average turns: " + Arrays.stream(battles)
                .mapToDouble(b -> b.getBattleLog().size() - 1)
                .average()
                .orElse(0)
        );

        container.stop();
    }

    private static BattleMasterContext createBattle(BeanContainer container) {
        BattleMasterImpl battleMaster = container.getBean(BattleMasterImpl.class);
        BattleCommandRegistrar registrar = container.getBean(BattleCommandRegistrar.class);
        BehaviorObjectsManager behaviorsManager = container.getBean(BehaviorObjectsManager.class);

        battleMaster.prepareBattle(new SimpleBattleGenerator(), registrar, behaviorsManager);

        return new BattleMasterContext(battleMaster);
    }

    private static void turnBattles(BattleMasterContext... battles) {
        for (BattleMasterContext battle : battles) {
            if (battle.isRunning()) {
                battle.getBattleMaster().takeTurn();
            }
        }
        drawBattles(Arrays.stream(battles)
                .filter(BattleMasterContext::isRunning)
                .map(BattleMasterContext::getBattleMaster)
                .toArray(BattleMasterImpl[]::new));
    }

    private static void drawBattles(BattleMasterImpl... battleMasters) {
        for (int i = 0; i < battleMasters.length; i++) {
            draw(battleMasters[i].getContext());

            if (i < battleMasters.length - 1) {
                System.out.println("--- and ---");
            }
        }
        System.out.println("-------------------------------------------");
    }

    private static void draw(BattleContext context) {
        String[][] map = new String[MAP_SIZE][MAP_SIZE];
        String[] lines = new String[MAP_SIZE];

        fill(map);
        prepareMap(map, context);
        drawMaps(lines, map);
        drawHp(lines, context);

        for (String line : lines) {
            System.out.println(line);
        }
    }

    private static void drawHp(String[] lines, BattleContext context) {
        List<Integer> ids = context.getBattleState().getDynamicObjectIds();

        for (int i = 0; i < ids.size(); i++) {
            int id = ids.get(i);
            HasHealth hasHealth = (HasHealth)context.getBattleState().resolveId(id);
            lines[i] += "    " + id + " : " + hasHealth.getHealth() + " hp. " + (hasHealth.isDead() ? "DIE" : "ALIVE");
        }
    }

    private static void drawMaps(String[] lines, String[][] map) {
        for (int i = 0; i < map.length; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < map[i].length; j++) {
                builder.append(map[j][map.length - i - 1]);
                builder.append(" ");
            }
            lines[i] = builder.toString();
        }
    }

    private static void prepareMap(String[][] map, BattleContext context) {
        BattleState battleState = context.getBattleState();

        for (Map.Entry<Point, List<Integer>> entry : context.getBattleState().getMapFilling().entrySet()) {
            Point position = entry.getKey();
            List<Integer> modelIds = entry.getValue();

            if (modelIds.size() > 1) {
                System.out.println("Collision at " + position + ". For models: " +
                        modelIds.stream().map(Object::toString).collect(Collectors.joining(", ")));
            } else if (modelIds.size() < 1) {
                System.out.println("Not delete empty cell: " + position);
            } else {
                CanMoved moved = (CanMoved)battleState.resolveId(modelIds.get(0));
                String symb = moved.getDirection().name().substring(0, 1);
                map[position.getX()][position.getY()] = symb;
            }
        }
    }

    private static void fill(String[][] map) {
        for (String[] chars : map) {
            Arrays.fill(chars, ".");
        }
    }
}

class BattleMasterContext implements BattlePresenterActionSubscriber {
    private final BattleMasterImpl battleMaster;
    private final List<List<BattleAction>> actionsLog = new ArrayList<>();
    private boolean isRunning = true;

    public BattleMasterContext(BattleMasterImpl battleMaster) {
        this.battleMaster = battleMaster;

        battleMaster.getBattlePresenter().subscribeBattleActions(
                SubscribeType.INITIALIZATION,
                SubscribeStrategy.ALL_ACTIONS_SUBSCRIBE_STRATEGY,
                this
        );
        battleMaster.getBattlePresenter().subscribeBattleActions(
                SubscribeType.PHASE,
                SubscribeStrategy.ALL_ACTIONS_SUBSCRIBE_STRATEGY,
                this
        );
        battleMaster.getBattlePresenter().subscribeBattleActions(
                SubscribeType.TURN,
                SubscribeStrategy.INFORMATION_ABOUT_SUBSCRIBE,
                this
        );
        battleMaster.informationAboutInitialize();
    }

    @Override
    public void subscribe(SubscribeType subscribeType, Collection<BattleAction> actions) {
        if (SubscribeType.INITIALIZATION.equals(subscribeType)) {
            actionsLog.add(new ArrayList<>(actions));
            actionsLog.add(new ArrayList<>());
        } else if (SubscribeType.PHASE.equals(subscribeType)) {
            if (actionsLog.isEmpty()) {
                actionsLog.add(new ArrayList<>());
            }
            actionsLog.get(actionsLog.size() - 1).addAll(CollectionUtils.concat(actions, new EndPhase()));
        } else {
            actionsLog.add(new ArrayList<>());
        }
    }

    public BattleMasterImpl getBattleMaster() {
        return battleMaster;
    }

    public List<List<BattleAction>> getBattleLog() {
        return actionsLog;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setCompleted() {
        isRunning = false;

        BattleActionPresenter presenter = battleMaster.getBattlePresenter();
        presenter.unsubscribeBattleActions(SubscribeType.PHASE, this);
        presenter.unsubscribeBattleActions(SubscribeType.TURN, this);
    }

    public static class EndPhase implements BattleAction { }
}

class SimpleBattleGenerator implements BattleGenerator {
    private IdGenerator idGenerator;

    @Override
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public BattleState generate() {
        Map<Integer, BattleModel> battleModelById = new HashMap<>();
        Map<Point, List<Integer>> mapPosition = new HashMap<>();
        List<Integer> dynamicModels = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            BattleCharacter model = new BattleCharacter(idGenerator.generateId(), 10);
            Point position = new Point(random.nextInt(TestBattle.MAP_SIZE), random.nextInt(TestBattle.MAP_SIZE));

            battleModelById.put(model.getId(), model);
            mapPosition.computeIfAbsent(position, p -> new ArrayList<>()).add(model.getId());
            model.setPosition(position);
            model.setDirection(Direction.values()[random.nextInt(Direction.values().length)]);
            dynamicModels.add(model.getId());
        }

        SquareBattleFieldLimiter battleFieldLimiter = new SquareBattleFieldLimiter(new Point(0, 0), TestBattle.MAP_SIZE);

        return new BattleState(battleModelById, mapPosition, dynamicModels, battleFieldLimiter);
    }

    private static class SquareBattleFieldLimiter implements BattlefieldLimiter {
        private final Point leftTopVertex;
        private final int side;

        private SquareBattleFieldLimiter(Point leftTopVertex, int side) {
            this.leftTopVertex = leftTopVertex;
            this.side = side;
        }

        @Override
        public boolean inBound(Point point) {
            Point dist = point.sub(leftTopVertex);
            return 0 <= dist.getX() && dist.getX() < side && 0 <= dist.getY() && dist.getY() < side;
        }
    }
}
