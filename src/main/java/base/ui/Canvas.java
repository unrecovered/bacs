package base.ui;

import base.Bacs;

import java.awt.*;

import javax.swing.JComponent;

public class Canvas extends JComponent {

    private final int dimension;

    private final int scale;

    public Canvas(int dimension, int scale) {
        this.dimension = dimension;
        this.scale = scale;
        setSize(dimension, dimension);
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Graphics2D g2d;

    public void paintComponent(Graphics g) {

        g2d = (Graphics2D) g;
        //g2d.setBackground(Color.BLACK);
        //g2d.clearRect(0, 0, Bacs.dimension-1, Bacs.dimension-1);

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                g2d.setPaint(Color.decode("#" + Bacs.battlefield[i][j].getColorCode()));
                //if(Bacs.battlefield[i][j].stats.clr != "#000000"){System.out.println(i+" "+j+" "+Bacs.battlefield[i][j].stats.clr);}
                if (scale > 3) {
                    g2d.drawRect(i * scale, j * scale, scale - 1, scale - 1);
                    g2d.fillRect(i * scale, j * scale, scale - 1, scale - 1);
                } else {
                    g2d.drawRect(i * scale, j * scale, scale - 1, scale - 1);
                }
            }
        }
    }
}
