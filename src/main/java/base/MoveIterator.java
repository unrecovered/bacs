package base;

import static base.utils.Random.*;

/**
 * Created by valera on 09.03.17.
 */
public class MoveIterator {


    private final BattleField battleField;

    private final Engine engine;

    private final int dimension;

    public MoveIterator(BattleField battleField, int dimension) {
        this.dimension = dimension;
        this.battleField = battleField;
        this.engine = new Engine(battleField);
    }

    public void next() {
        for (int i = 0; i < dimension * dimension; i++) {
            //System.out.println(i+" "+j+" act="+battlefield[i][j].actionPointer+" comm="+battlefield[i][j].behaviour[battlefield[i][j].actionPointer % 64]+" dir="+battlefield[i][j].direction+" nrg="+battlefield[i][j].energy);
            int x = getRandom(0, dimension - 1);
            int y = getRandom(0, dimension - 1);
            if ((battleField.getCell(x, y).clr != "000000") && (battleField.getCell(x, y).clr != "FFFFFF")) {
                engine.process(x, y);
            }
        }
    }
}
