package base;

import java.util.Arrays;

import static base.utils.Random.*;

public class BacUnit implements Cloneable {
    static int actlim = 20; //maximum number of actions
    static int comnum = 6; //number of commands available
    static int relsense = 5; //how much relative can differ
    static int gainbase = 5; //energy gained with gain ability
    static boolean lumus = true; //light mechanics
//    final static int[] dirx = {0, 1, 1, 1, 0, -1, -1, -1};
//    final static int[] diry = {-1, -1, 0, 1, 1, 1, 0, -1};
//    int light;


    // Параметры клетки
    int str;
    int end;
    int mut;
    String clr;


    float energy;
    double light; // Освещеннойсть клетки
    int direction; // Направление перемещения или атаки
    int action = 0; // Номер отрабатываемой команды

//    int x, y, dx, dy;

//    public int[] quirks = new int[10];

    int[] behaviour = new int[23]; // последовательность действий клетки

//    private final Settings settings;

    BacUnit() {
//        this.settings = Bacs.settings; //todo Remove that shit!
        this.clr = "000000";
    }

    public int getMyAction() {
        action = action % actlim;
        while (behaviour[action] == 0) {
            action++;
            action = action % actlim;
        }
        int actionCode = behaviour[action];
        action++;
        return actionCode;
    }

    public int getNextAction() {
        action++;
        action %= actlim;
        int code = behaviour[action];
        action++;
        return code;
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

//    void setd(int direction) {
//        dx = (x + dirx[direction] + settings.dimension) % settings.dimension;
//        dy = (y + diry[direction] + settings.dimension) % settings.dimension;
//    }

//    String lookup() {
//        setd(this.direction);
//        BacUnit that = Bacs.battlefield[dx][dy];
//        if (that.clr == "FFFFFF") {
//            return "corpse";
//        } else if (that.clr == "000000") {
//            return "empty";
//        } else {
//            if (Math.abs(this.clr.compareTo(that.clr)) < relsence) {
//                return "relative";
//            } else {
//                return "other";
//            }
//        }
//    }

//    int observe() { //дополнительно: видеть силу противника
//        switch (lookup()) {
//            case "relative":
//                return 2;
//            case "other":
//                return 3;
//            case "corpse":
//                return 4;
//            default:
//                return 1;
//        }
//    }

//    boolean eat() {
//        if (lookup() == "corpse") {
//            this.energy += Bacs.battlefield[dx][dy].energy / 2;
//            Bacs.battlefield[dx][dy] = new BacUnit();
//            //Bacs.battlefield[dx][dy].stats.clr = "000000";
//            return true;
//        } else {
//            return false;
//        }
//    }

    boolean gain() {
        energy += gainbase * light;
        return true;
    }

//    boolean attack() {
//        if (lookup() == "other") {
//            if (getRandom(0, this.str + Bacs.battlefield[dx][dy].str) <= this.str) {
//                Bacs.battlefield[dx][dy].die(true);
//            }
//            return true;
//        }
//        return false;
//    }

//    boolean breed() {
//        int[] freespace;
//        freespace = new int[8]; //выбираем произвольное свободное место для размножения
//        int point = 0;
//        int direction = this.direction; //ява не поддерживает дефолтные значения для параметров... посмотреть потом
//        for (int i = 0; i < 8; i++) { //вероятно есть способ получше
//            this.direction = i;
//            if (lookup() == "empty") {
//                freespace[point] = i;
//                point++;
//            }
//        }
//        this.direction = direction;
//
//        if (point > 0) {
//            int spacenum = getRandom(0, point - 1);
//            spacenum = freespace[spacenum];
//
//            setd(spacenum);
//
//            try {
//                this.energy = this.energy / 2;
//                BacUnit clone = this.clone();
//                Bacs.battlefield[dx][dy] = clone;
//                Bacs.battlefield[dx][dy].action = 0;
//                Bacs.battlefield[dx][dy].direction = getRandom(0, 7);
//                BacUnit that = Bacs.battlefield[dx][dy];
//
//                if (getRandom(0, 1000) < this.mut) {// пока что единый шанс мутации для всего
//                    //мутация статов
//                    if (that.str > 1) {
//                        that.str += getRandom(-1, 1);
//                    } else {
//                        that.str += getRandom(0, 1);
//                    }
//                    that.end += getRandom(-1, 1);
//                    //int intcolor = (int) Integer.parseInt(that.clr, 16) + Bacs.getRandom(0, 3);
//                    //that.clr = Integer.toHexString(intcolor);
//                    that.clr = Integer.toHexString(getRandom(1, 16777214));
//                    that.mut += getRandom(-1, 1);
//
//                    //мутация поведения
//                    int mutnum = getRandom(0, actlim - 1);
//                    that.behaviour[mutnum] = getRandom(0, actlim + comnum - 1);
//                    //System.out.println(that.x+" "+that.y+" number="+mutnum+" command="+that.behaviour[mutnum]);
//                    //System.out.println("str="+str+" end="+end+" clr="+clr+" mut="+mut+" Iteration="+Bacs.iternum);
//                    //for(int i=0; i<10; i++){System.out.print(that.behaviour[i]+" ");}
//                    //System.out.println();
//                }
//            } catch (CloneNotSupportedException e) {
//                System.out.println("Объект не может быть клонированным.");
//                e.printStackTrace();
//            }
//            return true;
//        } else {
//            return false;
//        }
//    }

//    boolean move() {
//
//        if (lookup() == "empty") {
////            BacUnit swap = new BacUnit();
//            BacUnit swap = Bacs.battlefield[dx][dy];
//            Bacs.battlefield[dx][dy] = Bacs.battlefield[x][y];
//            Bacs.battlefield[x][y] = swap;
//            return true;
//        } else {
//            return false;
//        }
//    }

    void turn(int dir) {
        if (dir % 2 == 0) {
            direction = (direction + 1) % 8;
        } else {
            direction = (direction + 7) % 8;
        }
    }

//    void die(boolean corpse) {
//        if (corpse) {
//            this.clr = "FFFFFF";
//        } else {
//            Bacs.battlefield[x][y] = new BacUnit();
//            //Bacs.battlefield[x][y].clr = "000000";
//        }
//        //System.out.println(x+" "+y+" died");
//    }

    public BacUnit clone() throws CloneNotSupportedException {
        BacUnit unit = (BacUnit) super.clone();
//        unit.stats = (Statlist) clone();
        unit.behaviour = new int[actlim];
        unit.behaviour = Arrays.copyOf(behaviour, actlim);
        return unit;
    }

    public String getColorCode() {
        return clr;
    }

}
