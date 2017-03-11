package base;

public class BacUnit {
    static int actlim = 20; //maximum number of actions
    static int comnum = 6; //number of commands available
    static int relsense = 5; //how much relative can differ
    static int gainbase = 5; //energy gained with gain ability

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

    public int getMyAction() {
        action %= actlim;
        int actionCode = behaviour[action];
        action++;
        return actionCode;
    }

//    public void act(int x, int y, double light) {
//        boolean done = false;
//        this.x = x;
//        this.y = y;
//        this.light = light;

//        if (lumus) {
//            int tocenterx = Math.abs(Bacs.settings.dimension / 2 - x);
//            int tocentery = Math.abs(Bacs.settings.dimension / 2 - y);
//            double tocenter = Math.sqrt(tocenterx * tocenterx + tocentery * tocentery);
//            double diag = Math.sqrt(Bacs.settings.dimension * Bacs.settings.dimension + Bacs.settings.dimension * Bacs.settings.dimension); //можно в статик...
//            light = (diag / 2 - tocenter) / (diag / 2);
//
//            light = light < 0 ? 0 : light;
//        } else {
//            light = 1;
//        }
        //System.out.println(x+" "+y+" "+tocenter+" "+light);

//        if (energy < end) { //энергии не хватает на размножение - действие
//            if (energy > 0) { //энергия меньше нуля - смерть
//                energy -= 1 + (float) Math.abs(str) / 10; //энергопотребление
//                for (int i = 0; (i < actlim) && (!done); i++) {
//                    action = action % actlim;
//
//                    if (behaviour[action] == Command.MOVE.getCode()) { //тут был красивый свитч, но в силу причин он был выпилен...
//                        move();
//                        done = true;
//                        action++;
//                    } else if (behaviour[action] == Command.TURN_LEFT.getCode()) {
//                        turn(behaviour[(action + actlim + 1) % actlim]);
//                        action += 2;
//                    } else if (behaviour[action] == Command.EAT.getCode()) {
//                        action++;
//                        eat();
//                        done = true;
//                    } else if (behaviour[action] == Command.GAIN.getCode()) {
//                        action++;
//                        gain();
//                        done = true;
//                    } else if (behaviour[action] == Command.ATTACK.getCode()) {
//                        action++;
//                        attack();
//                        done = true;
//                    } else if (behaviour[action] == Command.OBSERVE.getCode()) {
//                        action += (observe() + actlim) % actlim;
//                    } else {
//                        action = behaviour[action];
//                    }
//                }
//            } else {
//                die(false);
//            }
//        } else {
//            if (!breed()) {//попытка размножения неудачна - смерть
//                die(true);
//            }
//        }
//    }

    boolean gain() {
        energy += gainbase * light;
        return true;
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
