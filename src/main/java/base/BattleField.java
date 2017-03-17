package base;

/**
 * Игровое поле.
 * Created by valera on 08.03.17.
 */
public class BattleField {

    private final BacUnit[] cells;

    private boolean lumus;

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
        cells = new BacUnit[dimension * dimension];
        this.lumus = lumus;
    }

    /**
     * Инициализация стартовой позиции
     */
    public void init(int energy, String color, int direction, int strength, int mutagen, int end) {

        for (int i = 0; i < dimension * dimension; i++) {
                BacUnit bu = new BacUnit();
                bu.clr = "000000";
                if (lumus) {
                    bu.light = calculateLight(i, halfSize, halfSize);
                } else {
                    bu.light = 1;
                }
                bu.behaviour = new int[23];
                bu.changed = true;
            cells[i] = bu;
        }

        BacUnit initial = getCell(halfSize, halfSize);
        initial.clr = color;
        initial.direction = direction;
        initial.str = strength;
        initial.mut = mutagen;
        initial.end = end;
        initial.energy = energy;
        initial.behaviour[0] = Command.GAIN.getCode();
        initial.changed = true;
    }


    private Coords normalize(int x, int y) {
        return new Coords((x + dimension) % dimension, (y + dimension) % dimension);
    }

    public BacUnit getCell(int x, int y) {
        Coords req = normalize(x, y);
        return cells[req.x + req.y * dimension];
    }

    public int getDimension() {
        return dimension;
    }

    /**
     * Расчет коэффициента освещенности клетки поля.
     *
     * @param i  индекс клетки
     * @param xc x-координата источника освещения
     * @param yc y-координата источника освещения
     * @return рассчитанный коэффициент клетки поля
     */
    private double calculateLight(int i, int xc, int yc) {
        int x = i % dimension;
        int y = i / dimension;
        int dx = xc - x;
        int dy = yc - y;
        double delta = Math.sqrt(dx * dx + dy * dy);
        double maxDistance = Math.sqrt(2) * xc;
        return (maxDistance - delta) / maxDistance;
    }
}
