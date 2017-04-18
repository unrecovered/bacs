package base;

import java.util.HashSet;
import java.util.Set;

import static base.utils.Random.*;

/**
 * Created by valera on 09.03.17.
 */
public class MoveIterator {
    private final BattleField battleField;

    private final Engine engine;

    private final int dimension;

    private final int totalCell;

    public MoveIterator(BattleField battleField) {
        this.dimension = battleField.getDimension();
        this.battleField = battleField;
        this.engine = new Engine(battleField);
        totalCell = dimension * dimension;
    }

    public void nextMove() {
        Set<Integer> processed = new HashSet<>(totalCell);
        while (processed.size() < totalCell) {
            int x = getRandom(0, dimension - 1);
            int y = getRandom(0, dimension - 1);
            processed.add(y * dimension + x);
            if ((! battleField.getCell(x, y).clr.equals("000000")) && (!battleField.getCell(x, y).clr.equals("FFFFFF"))) {
                engine.process(x, y);
            }
        }
    }
}
