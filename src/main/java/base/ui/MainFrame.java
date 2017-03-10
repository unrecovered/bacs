package base.ui;

import base.Settings;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Created by valera on 08.03.17.
 */
public class MainFrame extends JFrame {

    private final MainSettings settingsPane = new MainSettings();

    public MainFrame(String title, Settings settings, Canvas playField) {
        super(title);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

//        getContentPane().setLayout(new BorderLayout());

        JPanel canvasPanel = new JPanel(new BorderLayout(), true);
        canvasPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        canvasPanel.add(playField, BorderLayout.CENTER);
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(canvasPanel, BorderLayout.CENTER);
        contentPanel.add(settingsPane, BorderLayout.EAST);
        setContentPane(contentPanel);

        settingsPane.setSettings(settings);

        pack();
        setVisible(true);
    }
}
