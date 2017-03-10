package base.ui;

import base.Bacs;
import base.BattleField;

import java.awt.*;

import javax.naming.spi.DirectoryManager;
import javax.swing.JComponent;

public class Canvas extends JComponent {

    private final int dimension;

    private final int scale;

    private final BattleField battleField;

    public Canvas(int dimension, int scale, BattleField battleField) {
        this.dimension = dimension;
        this.scale = scale;
        this.battleField = battleField;
        Dimension d = new Dimension(dimension * scale, dimension * scale);
        setSize(d);
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
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
                g2d.setPaint(Color.decode("#" + battleField.getCell(i, j).getColorCode()));
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
