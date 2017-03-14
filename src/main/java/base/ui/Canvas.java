package base.ui;

import base.BacUnit;
import base.BattleField;

import javax.swing.*;
import java.awt.*;

public class Canvas extends JComponent {

    private final int dimension;

    private final int scale;

    private final BattleField battleField;

    Canvas(BattleField battleField, int scale) {
        this.battleField = battleField;
        this.dimension = battleField.getDimension();
        this.scale = scale;
        Dimension d = new Dimension(dimension * scale, dimension * scale);
        setSize(d);
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
    }


   private Graphics2D g2d;

    @Override
    public void paintComponent(Graphics g) {

        g2d = (Graphics2D) g;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                BacUnit current = battleField.getCell(i, j);

                g2d.setPaint(Color.decode("#" + current.getColorCode()));

                if (scale > 3) {
                    g2d.drawRect(i * scale, j * scale, scale - 1, scale - 1);
                    g2d.fillRect(i * scale, j * scale, scale - 1, scale - 1);
                } else {
                    g2d.drawRect(i * scale, j * scale, scale - 1, scale - 1);
                }
                current.changed = false;
            }
        }
    }
}
