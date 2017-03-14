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

//    private final BattleField battleField;

    private final Canvas playField;

    private final Settings settings;

    private Timer timer;
    private boolean run;

    private final JButton start;

    private final JButton stop;

    private final JProgressBar progress;

    public MainFrame(String title, Settings settings) {
        super(title);
        this.settings = settings;
        BattleField battleField = new BattleField(settings.dimension, settings.lumus);
        battleField.init(50, "FF0000", 0, settings.strength, settings.mutagen, settings.end);
        playField = new Canvas(battleField, settings.scale);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        progress = new JProgressBar(JProgressBar.VERTICAL);

        JPanel canvasPanel = new JPanel(new BorderLayout(), true);
        canvasPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        canvasPanel.add(playField, BorderLayout.CENTER);
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(canvasPanel, BorderLayout.CENTER);
        contentPanel.add(settingsPane, BorderLayout.EAST);
        contentPanel.add(progress, BorderLayout.WEST);
        setContentPane(contentPanel);

        JPanel buttons = new JPanel(new FlowLayout());
        start = new JButton(new AbstractAction("Go") {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.start();
                run = true;
                start.setEnabled(false);
                stop.setEnabled(true);
//                battleField.init(50, "FF0000", 0, settings.strength, settings.mutagen, settings.end);
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

        timer = new Timer(40, e1 -> playField.repaint());
        settingsPane.setSettings(settings);

        pack();
        setVisible(true);
    }

    private void startAutoMove() {
        Settings settings = settingsPane.getSettings();
        BattleField battleField = new BattleField(settings.dimension, settings.lumus);
        battleField.init(50, "FF0000", 0, settings.strength, settings.mutagen, settings.end);
        playField.setBattleField(battleField);
        playField.setScale(settings.scale);
        MoveIterator moveIterator = new MoveIterator(battleField);

        progress.setMinimum(0);
        progress.setMaximum(settings.maxIterations);
        progress.setStringPainted(true);

        int iternum = 0;
        while ((run) && (iternum < settings.maxIterations)) {
            iternum++;
            progress.setValue(iternum);
            moveIterator.nextMove();
        }
    }
}
