package base;

/**
 * Created by valera on 09.03.17.
 */
public class Iteration extends Thread {

    @Override
    public void run() {
        while (Bacs.iternum < Bacs.settings.maxIterations) {
            int dimension = Bacs.settings.dimension;
            int x, y;
            for (int i = 0; i < dimension * dimension; i++) {
                //System.out.println(i+" "+j+" act="+battlefield[i][j].actionPointer+" comm="+battlefield[i][j].behaviour[battlefield[i][j].actionPointer % 64]+" dir="+battlefield[i][j].direction+" nrg="+battlefield[i][j].energy);
                x = Bacs.getRandom(0, dimension - 1);
                y = Bacs.getRandom(0, dimension - 1);
                if ((Bacs.battlefield[x][y].clr != "000000") && (Bacs.battlefield[x][y].clr != "FFFFFF")) {
                    Bacs.battlefield[x][y].act(x, y, getLight(x, y));
                }
            }
            Bacs.iternum++;
        }
    }

    private double getLight(int x, int y) {
        double light;
        if (Bacs.settings.lumus) {
            int tocenterx = Math.abs(Bacs.settings.dimension / 2 - x);
            int tocentery = Math.abs(Bacs.settings.dimension / 2 - y);
            double tocenter = Math.sqrt(tocenterx * tocenterx + tocentery * tocentery);
            double diag = Math.sqrt(Bacs.settings.dimension * Bacs.settings.dimension + Bacs.settings.dimension * Bacs.settings.dimension); //можно в статик...
            light = (diag / 2 - tocenter) / (diag / 2);

            light = light < 0 ? 0 : light;
        } else {
            light = 1;
        }
        return light;
    }
}
