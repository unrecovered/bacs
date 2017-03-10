package base;

import static base.utils.Random.*;
/**
 * Created by valera on 09.03.17.
 */
public class Iteration extends Thread {

    private final Settings settings;

    private final BattleField battleField;

    private final Engine engine;

    public Iteration(Settings settings, BattleField battleField) {
        this.settings = settings;
        this.battleField = battleField;
        this.engine = new Engine(battleField);
    }

    @Override
    public void run() {
        while (Bacs.iternum < settings.maxIterations) {
            int dimension = settings.dimension;
            int x, y;
            for (int i = 0; i < dimension * dimension; i++) {
                //System.out.println(i+" "+j+" act="+battlefield[i][j].actionPointer+" comm="+battlefield[i][j].behaviour[battlefield[i][j].actionPointer % 64]+" dir="+battlefield[i][j].direction+" nrg="+battlefield[i][j].energy);
                x = getRandom(0, dimension - 1);
                y = getRandom(0, dimension - 1);
                if ((battleField.getCell(x, y).clr != "000000") && (battleField.getCell(x, y).clr != "FFFFFF")) {
                    engine.process(x, y);
                }
            }
            Bacs.iternum++;
        }
    }
//
//    private double getLight(int x, int y) {
//        double light;
//        if (settings.lumus) {
//            int tocenterx = Math.abs(settings.dimension / 2 - x);
//            int tocentery = Math.abs(settings.dimension / 2 - y);
//            double tocenter = Math.sqrt(tocenterx * tocenterx + tocentery * tocentery);
//            double diag = Math.sqrt(2) * settings.dimension; //можно в статик...
//            light = (diag / 2 - tocenter) / (diag / 2);
//
//            light = light < 0 ? 0 : light;
//        } else {
//            light = 1;
//        }
//        return light;
//    }
}
