package base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static base.utils.Random.getRandom;
import static base.utils.Random.getRandomColorCode;

/**
 * Основной класс движка
 * Created by valera on 10.03.17.
 */
final class Engine {
    private final String EMPTY_CELL = "000000";
    private final String CORPSE_CELL = "FFFFFF";

    private BattleField battleField;

    Engine(BattleField battleField) {
        this.battleField = battleField;
    }

    void process(int x, int y) {
        BacUnit target = battleField.getCell(x, y);

        if (target.energy >= target.end) {
            breed(x, y, target);
            return;
        }

        if (target.energy <= 0) {
            die(target);
            return;
        }

        //Выполняем другие действия
        boolean nextMove = true;
        int counter = 0;
        while (nextMove) {
            nextMove = counter < BacUnit.actlim;

            target.energy -= 1 + (float) target.str / 10;
            if (target.energy <= 0) {
                return;
            }
            counter++;
            int commandCode = target.getMyAction();
            switch (commandCode) {
                case 20:
                    move(x, y, target);
                    nextMove = false;
                    break;
                case 21:
                    target.turn(target.getMyAction());
                    break;
                case 22:
                    eat(x, y, target);
                    nextMove = false;
                    break;
                case 23:
                    target.gain();
                    nextMove = false;
                    break;
                case 24:
                    attack(x, y, target);
                    nextMove = false;
                    break;
                case 25:
                    observe(x, y, target);
                    break;
                default:
                    target.action = commandCode;
                    break;
            }
        }
    }

    private void observe(int x, int y, BacUnit observer) {
        BattleField.Coords dir = BattleField.lookup[observer.direction];
        BacUnit target = battleField.getCell(x + dir.x, y + dir.y);
        if (target.clr.equals(CORPSE_CELL)) {
            eat(x, y, observer);
        } else if (!target.clr.equals(EMPTY_CELL)) {
            if (observer.clr.compareTo(target.clr) >= BacUnit.relsense) {
                attack(x, y, observer);
            } else {
                observer.gain();
            }
        } else {
            move(x, y, observer);
        }
    }

    private void attack(int x, int y, BacUnit attacker) {
        BattleField.Coords dir = BattleField.lookup[attacker.direction];
        BacUnit defense = battleField.getCell(x + dir.x, y + dir.y);
        if (attacker.clr.compareTo(defense.clr) >= BacUnit.relsense)
            if (getRandom(0, attacker.str + defense.str) <= attacker.str) {
                corpse(defense);
            } else {
                corpse(attacker);
            }
    }

    private void eat(int x, int y, BacUnit devourer) {
        List<BacUnit> victims = findNeighbors(x, y, CORPSE_CELL);
        if (victims.size() > 0) {
            BacUnit victim = victims.get(getRandom(0, victims.size() - 1));
            devourer.energy += victim.energy / 2;
            die(victim);
        }
    }

    private void move(int x, int y, BacUnit source) {
        List<BacUnit> targets = findNeighbors(x, y, EMPTY_CELL);
        if (targets.size() > 0) {
            BacUnit dest = targets.get(getRandom(0, targets.size() - 1));
            copy(source, dest);
            dest.changed = true;
            die(source);
        }
    }

    private void breed(int x, int y, BacUnit parent) {
        List<BacUnit> emptyCells = findNeighbors(x, y, EMPTY_CELL);

        if (emptyCells.isEmpty()) {
            corpse(parent);
            return;
        }
        int pos = getRandom(0, emptyCells.size() - 1);
        BacUnit newCell = emptyCells.get(pos);
        parent.energy = parent.energy / 2;
        copy(parent, newCell);

        newCell.direction = getRandom(0, 7);
        newCell.ticks = 0;
        newCell.changed = true;
        if (getRandom(0, 1000) < parent.mut)
            mutate(newCell);
    }

    private void copy(BacUnit src, BacUnit dst) {
        dst.energy = src.energy;
        dst.str = src.str;
        dst.end = src.end;
        dst.mut = src.mut;
        dst.clr = src.clr;
        dst.direction = src.direction;
        dst.action = src.action;
        dst.behaviour = Arrays.copyOf(src.behaviour, BacUnit.actlim);
    }

    private void mutate(BacUnit cell) {
        cell.str += cell.str > 1 ? getRandom(-1, 1) : getRandom(0, 1);
        cell.end += getRandom(-1, 1);

        cell.clr = getRandomColorCode();
        cell.mut += getRandom(-1, 1);

        //мутация поведения
        int mutnum = getRandom(0, BacUnit.actlim - 1);
        cell.behaviour[mutnum] = getRandom(0, BacUnit.actlim + 5);
    }

    private void die(BacUnit target) {
        target.clr = "000000";
        target.changed = true;
    }

    private void corpse(BacUnit target) {
        target.clr = "FFFFFF";
        target.changed = true;
    }

    private List<BacUnit> findNeighbors(int x, int y, String code) {
        List<BacUnit> cells = new ArrayList<>();
        for (BattleField.Coords dd : BattleField.lookup) {
            BacUnit cell = battleField.getCell(x + dd.x, y + dd.y);
            if (cell.clr.equals(code)) //todo метка занятости клетки
                cells.add(cell);
        }
        return cells;
    }
}