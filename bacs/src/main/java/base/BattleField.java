package base;

/**
 *  Игровое поле.
 * Created by valera on 08.03.17.
 */
public class BattleField {

    private final BacUnit[][] cells;

    private final int dimension;

    private final boolean useLight;
    
    public BattleField(int dimension, boolean useLight) {
        this.dimension = dimension;
        cells = new BacUnit[dimension][dimension];
        this.useLight = useLight;
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

    private int toNum(int x, int y) {
        return y * dimension + x;
    }

    public double getLight(int x, int y) {
        if (!useLight)
            return 1;
        int xc = dimension / 2;
        int yc = dimension / 2;
        int dx = xc - x;
        int dy = yc - y;
        double delta = Math.sqrt(dx*dx + dy*dy);
        double maxDistance = Math.sqrt(2) * dimension / 2;
        return (maxDistance - delta) / maxDistance;
    }
}
