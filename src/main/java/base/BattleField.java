package base;

import java.util.HashMap;

/**
 * Игровое поле.
 * Created by valera on 08.03.17.
 */
public class BattleField {

    private final BacUnit[] cells;

    private final BacUnit[] nextGeneration;

    private final int dimension;

    private final boolean useLight;

    private final HashMap<Integer, Double> lightTable = new HashMap<>();

    public BattleField(int dimension, boolean useLight) {
        this.dimension = dimension;
        cells = new BacUnit[dimension * dimension];
        nextGeneration = new BacUnit[dimension * dimension];
        this.useLight = useLight;
    }

    /**
     * Инициализация игрового поля
     */
    public void init() {
        int centerRow = dimension / 2;
        int centerColumn = dimension / 2;

        for (int i = 0; i < dimension * dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                BacUnit bu = new BacUnit();
                bu.clr = "000000";
                cells[i] = bu;
            }
        }

        for (int i = 0; i < centerRow; i++)
            for (int j = 0; j < centerColumn; j++)
                lightTable.put(toNum(j, i), calculateLight(j, i, centerColumn, centerRow));

        int initial = toNum(centerColumn, centerRow);
        cells[initial].clr = "FF0000";
        cells[initial].direction = 0;
        cells[initial].str = 1;
        cells[initial].mut = 250;
        cells[initial].end = 100;
        cells[initial].energy = 50;
        cells[initial].behaviour[0] = Command.GAIN.getCode();
    }

    private int toNum(int x, int y) {
        if ((x < 0) || (x >= dimension))
            x = x % dimension;
        if ((y < 0) || (y >= dimension))
            y = y % dimension;
        return y * dimension + x;
    }

    public double getLight(int x, int y) {
        return lightTable.get(toNum(x - dimension / 2, y - dimension / 2));
    }

    private double calculateLight(int x, int y, int xc, int yc) {
        if (!useLight)
            return 1;
        int dx = xc - x;
        int dy = yc - y;
        double delta = Math.sqrt(dx * dx + dy * dy);
        double maxDistance = Math.sqrt(2) * dimension / 2;
        return (maxDistance - delta) / maxDistance;
    }
}