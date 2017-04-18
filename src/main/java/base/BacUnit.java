package base;

public class BacUnit {
    static int actlim = 20; //maximum number of actions
    static int comnum = 6; //number of commands available
    static int relsense = 5; //how much relative can differ
    static int gainbase = 5; //energy gained with gain ability

    public boolean changed;
    // Параметры клетки
    int str;
    int end;
    int mut;
    String clr;

    float energy;
    double light; // Освещеннойсть клетки
    int direction; // Направление перемещения или атаки
    int action = 0; // Номер отрабатываемой команды

    int ticks; //Время жизни клетки в ходах
    int[] behaviour = new int[23]; // последовательность действий клетки


    BacUnit() {
        this.clr = "000000";
    }

    int getMyAction() {
        action %= actlim;
        int actionCode = behaviour[action];
        action++;
        return actionCode;
    }

    void gain() {
        energy += gainbase * light;
    }

    void turn(int dir) {
        if (dir % 2 == 0) {
            direction = (direction + 1) % 8;
        } else {
            direction = (direction + 7) % 8;
        }
    }

    public String getColorCode() {
        return clr;
    }

}