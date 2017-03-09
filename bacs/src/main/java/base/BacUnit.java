package base;

import java.util.Arrays;

public class BacUnit implements Cloneable {
    static int actlim = 20; //maximum number of actions
    static int comnum = 6; //number of commands available
    static int relsence = 5; //how much relative can differ
    static int gainbase = 5; //energy gained with gain ability
    static boolean lumus = true; //light mechanics
    final static int[] dirx = {0, 1, 1, 1, 0, -1, -1, -1};
    final static int[] diry = {-1, -1, 0, 1, 1, 1, 0, -1};

    public class Statlist implements Cloneable {
        public String clr;
        int str;
        int end;
        int mut;

        public Statlist clone() throws CloneNotSupportedException {
            return (Statlist) super.clone();
        }
    }

    int str;
    int end;
    int mut;
    public String clr;

    public Statlist stats = new Statlist();

    float energy;
    double light;
    int direction;
    int action = 0;

    int x, y, dx, dy;

    public int[] quirks = new int[10];

    public int[] behaviour = new int[23];

    BacUnit() {
        this.stats.clr = "000000";
        this.clr = "000000";
    }

    public void act(int x, int y) {
        boolean done = false;
        this.x = x;
        this.y = y;

        if (lumus) {
            int tocenterx = Math.abs(Bacs.settings.dimension / 2 - x);
            int tocentery = Math.abs(Bacs.settings.dimension / 2 - y);
            double tocenter = Math.sqrt(tocenterx * tocenterx + tocentery * tocentery);
            double diag = Math.sqrt(Bacs.settings.dimension * Bacs.settings.dimension + Bacs.settings.dimension * Bacs.settings.dimension); //можно в статик...
            light = (diag / 2 - tocenter) / (diag / 2);

            light = light < 0 ? 0 : light;
        } else {
            light = 1;
        }
        //System.out.println(x+" "+y+" "+tocenter+" "+light);

        if (energy < stats.end) { //энергии не хватает на размножение - действие
            if (energy > 0) { //энергия меньше нуля - смерть
                energy -= 1 + (float) Math.abs(stats.str) / 10; //энергопотребление
                for (int i = 0; (i < actlim) && (!done); i++) {
                    action = action % actlim;

                    if (behaviour[action] == actlim) { //тут был красивый свитч, но в силу причин он был выпилен...
                        move();
                        done = true;
                        action++;
                    } else if (behaviour[action] == actlim + 1) {
                        turn(behaviour[(action + actlim + 1) % actlim]);
                        action += 2;
                    } else if (behaviour[action] == actlim + 2) {
                        action++;
                        eat();
                        done = true;
                    } else if (behaviour[action] == actlim + 3) {
                        action++;
                        gain();
                        done = true;
                    } else if (behaviour[action] == actlim + 4) {
                        action++;
                        attack();
                        done = true;
                    } else if (behaviour[action] == actlim + 5) {
                        action += (observe() + actlim) % actlim;
                    } else {
                        action = behaviour[action];
                    }
                }
            } else {
                die(false);
            }
        } else {
            if (!breed()) {//попытка размножения неудачна - смерть
                die(true);
            }
        }
    }

    void setd(int direction) {
        dx = (x + dirx[direction] + Bacs.settings.dimension) % Bacs.settings.dimension;
        dy = (y + diry[direction] + Bacs.settings.dimension) % Bacs.settings.dimension;
    }

    String lookup() {
        setd(this.direction);
        BacUnit that = Bacs.battlefield[dx][dy];
        if (that.stats.clr == "FFFFFF") {
            return "corpse";
        } else if (that.stats.clr == "000000") {
            return "empty";
        } else {
            if (Math.abs(this.stats.clr.compareTo(that.stats.clr)) < relsence) {
                return "relative";
            } else {
                return "other";
            }
        }
    }

    int observe() { //дополнительно: видеть силу противника
        switch (lookup()) {
            case "relative":
                return 2;
            case "other":
                return 3;
            case "corpse":
                return 4;
            default:
                return 1;
        }
    }

    boolean eat() {
        if (lookup() == "corpse") {
            this.energy += Bacs.battlefield[dx][dy].energy / 2;
            Bacs.battlefield[dx][dy] = new BacUnit();
            //Bacs.battlefield[dx][dy].stats.clr = "000000";
            return true;
        } else {
            return false;
        }
    }

    boolean gain() {
        energy += gainbase * light;
        return true;
    }

    boolean attack() {
        if (lookup() == "other") {
            if (Bacs.getRandom(0, this.stats.str + Bacs.battlefield[dx][dy].stats.str) <= this.stats.str) {
                Bacs.battlefield[dx][dy].die(true);
            }
            return true;
        }
        return false;
    }

    boolean breed() {
        int[] freespace;
        freespace = new int[8]; //выбираем произвольное свободное место для размножения
        int point = 0;
        int direction = this.direction; //ява не поддерживает дефолтные значения для параметров... посмотреть потом
        for (int i = 0; i < 8; i++) { //вероятно есть способ получше
            this.direction = i;
            if (lookup() == "empty") {
                freespace[point] = i;
                point++;
            }
        }
        this.direction = direction;

        if (point > 0) {
            int spacenum = Bacs.getRandom(0, point - 1);
            spacenum = freespace[spacenum];

            setd(spacenum);

            try {
                this.energy = this.energy / 2;
                BacUnit clone = this.clone();
                Bacs.battlefield[dx][dy] = clone;
                Bacs.battlefield[dx][dy].action = 0;
                Bacs.battlefield[dx][dy].direction = Bacs.getRandom(0, 7);
                BacUnit that = Bacs.battlefield[dx][dy];

                if (Bacs.getRandom(0, 1000) < this.stats.mut) {// пока что единый шанс мутации для всего
                    //мутация статов
                    if (that.stats.str > 1) {
                        that.stats.str += Bacs.getRandom(-1, 1);
                    } else {
                        that.stats.str += Bacs.getRandom(0, 1);
                    }
                    that.stats.end += Bacs.getRandom(-1, 1);
                    //int intcolor = (int) Integer.parseInt(that.stats.clr, 16) + Bacs.getRandom(0, 3);
                    //that.stats.clr = Integer.toHexString(intcolor);
                    that.stats.clr = Integer.toHexString(Bacs.getRandom(1, 16777214));
                    that.stats.mut += Bacs.getRandom(-1, 1);

                    //мутация поведения
                    int mutnum = Bacs.getRandom(0, actlim - 1);
                    that.behaviour[mutnum] = Bacs.getRandom(0, actlim + comnum - 1);
                    //System.out.println(that.x+" "+that.y+" number="+mutnum+" command="+that.behaviour[mutnum]);
                    //System.out.println("str="+stats.str+" end="+stats.end+" clr="+stats.clr+" mut="+stats.mut+" Iteration="+Bacs.iternum);
                    //for(int i=0; i<10; i++){System.out.print(that.behaviour[i]+" ");}
                    //System.out.println();
                }
            } catch (CloneNotSupportedException e) {
                System.out.println("Объект не может быть клонированным.");
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    boolean move() {

        if (lookup() == "empty") {
            BacUnit swap = new BacUnit();
            swap = Bacs.battlefield[dx][dy];
            Bacs.battlefield[dx][dy] = Bacs.battlefield[x][y];
            Bacs.battlefield[x][y] = swap;
            return true;
        } else {
            return false;
        }
    }

    void turn(int dir) {
        if (dir % 2 == 0) {
            direction = (direction + 1) % 8;
        } else {
            direction = (direction + 7) % 8;
        }
    }

    void die(boolean corpse) {
        if (corpse) {
            this.stats.clr = "FFFFFF";
        } else {
            Bacs.battlefield[x][y] = new BacUnit();
            //Bacs.battlefield[x][y].stats.clr = "000000";
        }
        //System.out.println(x+" "+y+" died");
    }

    public BacUnit clone() throws CloneNotSupportedException {
        BacUnit unit = (BacUnit) super.clone();
        unit.stats = (Statlist) stats.clone();
        unit.behaviour = new int[actlim];
        unit.behaviour = Arrays.copyOf(behaviour, actlim);
        return unit;
    }

    public String getColorCode() {
        return stats.clr;
    }
}
