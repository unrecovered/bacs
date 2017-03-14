package base.ui;

import base.BattleField;
import base.MoveIterator;
import base.Settings;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by valera on 08.03.17.
 */
public final class MainFrame extends JFrame {

    private final MainSettings settingsPane = new MainSettings();

    private final BattleField battleField;

    private final Canvas playField;

    private final Settings settings;

    private Timer timer;
    private boolean run;

    private final JButton start;

    private final JButton stop;

//     private String mytitle;

    public MainFrame(String title, Settings settings) {
        super(title);
//        this.mytitle = title;
        this.settings = settings;
        battleField = new BattleField(settings.dimension, settings.lumus);
        playField = new Canvas(battleField, settings.scale);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        timer = new Timer(40, e1 -> playField.repaint());

//        getContentPane().setLayout(new BorderLayout());

        JPanel canvasPanel = new JPanel(new BorderLayout(), true);
        canvasPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        canvasPanel.add(playField, BorderLayout.CENTER);
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(canvasPanel, BorderLayout.CENTER);
        contentPanel.add(settingsPane, BorderLayout.EAST);
        setContentPane(contentPanel);

        JPanel buttons = new JPanel(new FlowLayout());
        start = new JButton(new AbstractAction("Go") {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.start();
                run = true;
                start.setEnabled(false);
                stop.setEnabled(true);
                new Thread(() -> startAutoMove()).start();
            }
        });

        stop = new JButton(new AbstractAction("Stop") {
            @Override
            public void actionPerformed(ActionEvent e) {
                run = false;
                timer.stop();
                start.setEnabled(true);
                stop.setEnabled(false);
            }
        });
        buttons.add(start);
        buttons.add(stop);
        add(buttons, BorderLayout.SOUTH);
        stop.setEnabled(false);

        settingsPane.setSettings(settings);

        pack();
        setVisible(true);
    }

    private void startAutoMove() {
        battleField.init(50, "FF0000", 0, settings.strength, settings.mutagen, settings.end);
        MoveIterator moveIterator = new MoveIterator(battleField);
        int iternum = 0;
        while ((run) && (iternum < settings.maxIterations)) {
            iternum++;
//            System.out.println("> " + String.valueOf(iternum) + " <");
//            setTitle(mytitle + " MoveIterator " + iternum + " out of " + settings.maxIterations + "(" + iternum * 100
//                    / settings.maxIterations + "% done)");
            moveIterator.nextMove();
        }
    }
}
