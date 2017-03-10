package base;

/**
 * Игровое поле.
 * Created by valera on 08.03.17.
 */
public class BattleField {

    private final BacUnit[][] cells;

    static final Coords[] lookup = new Coords[]{
            new Coords(0, -1),
            new Coords(1, -1),
            new Coords(1, 0),
            new Coords(1, 1),
            new Coords(0, 1),
            new Coords(-1, 1),
            new Coords(-1, 0),
            new Coords(-1, -1)
    };

    static class Coords {
        int x;
        int y;

        Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private final int dimension;
    private final int halfSize;

    public BattleField(int dimension, boolean lumus) {
        this.dimension = dimension;
        this.halfSize = dimension / 2;

        //Инициализация игрового поля
        cells = new BacUnit[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                BacUnit bu = new BacUnit();
                bu.clr = "000000";
                if (lumus) {
                    bu.light = calculateLight(i, j, halfSize, halfSize);
                } else {
                    bu.light = 1;
                }
                bu.behaviour = new int[23];
                cells[i][j] = bu;
                cells[i][j] = bu;
            }
        }
    }

    /**
     * Инициализация стартовой позиции
     */
    public void init(int energy, String color, int direction, int strength, int mutagen, int end) {
        BacUnit initial = cells[halfSize][halfSize];
        initial.clr = color;
        initial.direction = direction;
        initial.str = strength;
        initial.mut = mutagen;
        initial.end = end;
        initial.energy = energy;
        initial.behaviour[0] = Command.GAIN.getCode();
    }

    private int toNum(int x, int y) {
        if ((x < 0) || (x >= dimension))
            x = x % dimension;
        if ((y < 0) || (y >= dimension))
            y = y % dimension;
        return y * dimension + x;
    }

    private Coords normalize(int x, int y) {
        return new Coords((x + dimension) % dimension, (y + dimension) % dimension);
    }

    public BacUnit getCell(int x, int y) {
        Coords req = normalize(x, y);
        return cells[req.x][req.y];
    }
//    public double getLight(int x, int y) {
//        return lightTable.get(toNum(x - dimension / 2, y - dimension / 2));
//    }

    /**
     * Расчет коэффициента освещенности клетки поля.
     *
     * @param x  координата клетки по оси x
     * @param y  координата клетки по оси y
     * @param xc x-координата источника освещения
     * @param yc y-координата источника освещения
     * @return рассчитанный коэффициент клетки поля
     */
    private static double calculateLight(int x, int y, int xc, int yc) {
        int dx = xc - x;
        int dy = yc - y;
        double delta = Math.sqrt(dx * dx + dy * dy);
        double maxDistance = Math.sqrt(2) * xc;
        return (maxDistance - delta) / maxDistance;
    }
}
