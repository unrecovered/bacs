package base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static base.utils.Random.getRandomColorCode;
import static base.utils.Random.getRandom;
import static base.utils.Random.getRandomPercent;

/**
 * Основной класс движка
 * Created by valera on 10.03.17.
 */
public class Engine {
    private BattleField battleField;
//    private Canvas visualizer;
//    private int relsense;

    public Engine(BattleField battleField) {
        this.battleField = battleField;
    }

    public void process(int x, int y) {
        BacUnit target = battleField.getCell(x, y);
//        target.ticks++;
//        if (target.ticks > 10000) {
//            corpse(target);
//            return;
//        }

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
            nextMove = counter < target.actlim;
            target.energy -= 1 + (float) target.str / 10;
            if (target.energy <= 0) {
                nextMove = false;
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

    void observe(int x, int y, BacUnit observer) {
        BattleField.Coords dir = BattleField.lookup[observer.direction];
        BacUnit target = battleField.getCell(x + dir.x, y + dir.y);
        if (target.clr.equals("FFFFFF")) {
            observer.action += 4;
        } else if (!target.clr.equals("000000")) {
            observer.action += observer.clr.compareTo(target.clr) >= observer.relsense ? 2 : 3;
        } else {
            observer.action++;
        }
    }

    private void attack(int x, int y, BacUnit attacker) {
        BattleField.Coords dir = BattleField.lookup[attacker.direction];
        BacUnit defense = battleField.getCell(x + dir.x, y + dir.y);
        if (attacker.clr.compareTo(defense.clr) >= attacker.relsense)
            if (getRandom(0, attacker.str + defense.str) <= attacker.str) {
                corpse(defense);
            } else {
                if (getRandomPercent() > 50)
                    corpse(attacker);
            }
    }

    private void eat(int x, int y, BacUnit devourer) {
        BattleField.Coords dir = BattleField.lookup[devourer.direction];
        BacUnit victim = battleField.getCell(x + dir.x, y + dir.y);
        if (victim.clr.equals("FFFFFF")) {
            devourer.energy += victim.energy / 2;
            die(victim);
        }
    }

    private void move(int x, int y, BacUnit source) {
        BattleField.Coords dir = BattleField.lookup[source.direction];
        BacUnit dest = battleField.getCell(x + dir.x, y + dir.y);
        if (dest.clr.equals("000000")) {
            copy(source, dest);
            die(source);
        }
    }

    private void breed(int x, int y, BacUnit parent) {
        List<BacUnit> emptyCells = new ArrayList<>(8);
        for (BattleField.Coords dd : BattleField.lookup) {
            BacUnit cell = battleField.getCell(x + dd.x, y + dd.y);
            if (cell.clr.equals("000000")) //todo метка занятости клетки
                emptyCells.add(cell);
        }
        if (emptyCells.isEmpty()) {
            corpse(parent);
            return;
        }
        int pos = getRandom(0, emptyCells.size() - 1);
        BacUnit newCell = emptyCells.get(pos);
        parent.energy = parent.energy / 2;
        copy(parent, newCell);
//        newCell.clr = parent.clr;
        newCell.direction = getRandom(0, 7);
        newCell.action = 0;
        newCell.ticks = 0;
//        newCell.behaviour = Arrays.copyOf(parent.behaviour, 0);
        if (getRandom(0, 1000) < newCell.mut)
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
        dst.behaviour = Arrays.copyOf(src.behaviour, src.behaviour.length);
    }

    private void mutate(BacUnit cell) {
//        System.out.println("--> Mutation!!! <--");
        cell.str += cell.str > 1 ? getRandom(-1, 1) : getRandom(0, 1);
        cell.end += getRandom(-1, 1);

        cell.clr = getRandomColorCode();
        cell.mut += getRandom(-1, 1);

        //мутация поведения
        int mutnum = getRandom(0, cell.actlim - 1);
        cell.behaviour[mutnum] = getRandom(0, 26);
    }

    private void die(BacUnit target) {
        target.clr = "000000";
    }

    private void corpse(BacUnit target) {
        target.clr = "FFFFFF";
    }
}
