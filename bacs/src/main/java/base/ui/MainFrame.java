package base.ui;

import javax.swing.*;

/**
 * Created by valera on 08.03.17.
 */
public class MainFrame extends JFrame {

    public MainFrame(String title, int dimension, int scale) {
        super(title);
        setSize(dimension * scale + 15, dimension * scale +38);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
