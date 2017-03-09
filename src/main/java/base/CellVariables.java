package base;

/**
 * Состояние клетки.
 * Created by valera on 09.03.17.
 */
public class CellVariables {
    float energy; // Энергия клетки. Когда падает до нуля, клетка умирает.
    CellStatus status;
    int strength;
    int end;
    int mut;
    String color;
    int direction;
    int actionNumber; // Номер отрабатываемой в данный момент команды

    public void setEnergy(float energy) {
        this.energy = energy;
        if (energy <= 0)
            this.status = CellStatus.EMPTY;
    }
}
