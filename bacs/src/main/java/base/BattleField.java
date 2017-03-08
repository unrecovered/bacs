package base;

/**
 * Created by valera on 08.03.17.
 */
public class BattleField {

    private final BacUnit[][] cells;

    private final int dimension;
    
    public BattleField(int dimension) {
        this.dimension = dimension;
        cells = new BacUnit[dimension][dimension];
    }

    /**
     * Инициализация игорового поля
     */
    public void init() {
        int centerRow = dimension / 2;
        int centerColumn = dimension / 2;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                BacUnit bu = new BacUnit();
                bu.stats.clr = "000000";
                cells[i][j] = bu;
            }
        }

        cells[centerRow][centerColumn].stats.clr = "FF0000";
        cells[centerRow][centerColumn].direction = 0;
        cells[centerRow][centerColumn].stats.str = 1;
        cells[centerRow][centerColumn].stats.mut = 250;
        cells[centerRow][centerColumn].stats.end = 100;
        cells[centerRow][centerColumn].energy = 50;
        cells[centerRow][centerColumn].behaviour[0] = BacUnit.actlim + 3;
    }
}
