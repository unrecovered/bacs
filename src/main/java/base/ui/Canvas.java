package base.ui;

import base.BacUnit;
import base.BattleField;

import javax.swing.*;
import java.awt.*;

public class Canvas extends JComponent {

    private int dimension;

    private int scale;

    private Dimension size;

    private BattleField battleField;

    Canvas(BattleField battleField, int scale) {
        this.battleField = battleField;
        this.dimension = battleField.getDimension();
        this.scale = scale;
        size = new Dimension(dimension * scale, dimension * scale);
        setSize(size);
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

    @Override
    public Dimension getMinimumSize() {
        return size;
    }

    @Override
    public Dimension getPreferredSize() {
        return size;
    }

    @Override
    public Dimension getMaximumSize() {
        return size;
    }

    void setScale(int scale) {
        this.scale = scale;
        this.dimension = battleField.getDimension();
        this.size = new Dimension(dimension * scale, dimension * scale);
        setSize(size);
    }

    void setBattleField(BattleField battleField) {
        this.battleField = battleField;
        this.dimension = battleField.getDimension();
        this.size = new Dimension(dimension * scale, dimension * scale);
        setSize(size);
    }
}
