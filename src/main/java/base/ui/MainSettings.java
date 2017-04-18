package base.ui;

import base.Settings;

import javax.swing.*;
import java.awt.*;

/**
 *  Панель настроек начальной конфигурации.
 * Created by ilychevva
 */
public class MainSettings extends JPanel {
    private Settings settings;

    private final JTextField cores = new JTextField(8);

    private final JTextField dimension = new JTextField(8);

    private final JTextField scale = new JTextField(8);

    private final JTextField maxIterations = new JTextField(8);

    private final JCheckBox lumus = new JCheckBox("Равномерное освещение");

    private final JTextField gainBase = new JTextField(8);

    private final JTextField relSense = new JTextField(8);

    private final JTextField actLim = new JTextField(8);

    private final JTextField strength = new JTextField(8);

    private final JTextField mutagen = new JTextField(8);

    private final JTextField end = new JTextField(8);


    MainSettings() {
        super();

        cores.setEnabled(false);
        gainBase.setEnabled(false);
        relSense.setEnabled(false);
        actLim.setEnabled(false);

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        add(new JLabel("Число ядер процессора:"), getLabelGBC(0, 0));
        add(cores, getFieldGBC(1, 0));
        add(new JLabel("Размер игрового поля"), getLabelGBC(0, 1));
        add(dimension, getFieldGBC(1,1));
        add(new JLabel("Размер клетки:"), getLabelGBC(0, 2));
        add(scale, getFieldGBC(1, 2));
        add(new JLabel("Число итераций:"), getFieldGBC(0, 3));
        add(maxIterations, getFieldGBC(1, 3));
        add(lumus, getFieldGBC(0, 4));
        add(new JLabel("Базовое поглощение энергии:"), getLabelGBC(0,5));
        add(gainBase, getFieldGBC(1, 5));
        add(new JLabel("Коэффициент родства:"), getLabelGBC(0, 6));
        add(relSense, getFieldGBC(1, 6));
        add(new JLabel("Длина цепочки действий:"), getLabelGBC(0, 7));
        add(actLim, getFieldGBC(1, 7));
        add(new JLabel("Базовая сила:"), getLabelGBC(0, 8));
        add(strength, getFieldGBC(1, 8));
        add(new JLabel("Мутагенность:"), getLabelGBC(0, 9));
        add(mutagen, getFieldGBC(1, 9));
        add(new JLabel("Порог энергии для размножения:"), getLabelGBC(0, 10));
        add(end, getFieldGBC(1, 10));
    }

    void setSettings(Settings settings) {
        this.settings = settings;
        cores.setText(String.valueOf(settings.cores));
        dimension.setText(String.valueOf(settings.dimension));
        scale.setText(String.valueOf(settings.scale));
        lumus.setSelected(!settings.lumus);
        maxIterations.setText(String.valueOf(settings.maxIterations));
        gainBase.setText(String.valueOf(settings.gainBase));
        relSense.setText(String.valueOf(settings.relSense));
        actLim.setText(String.valueOf(settings.actLim));
        strength.setText(String.valueOf(settings.strength));
        mutagen.setText(String.valueOf(settings.mutagen));
        end.setText(String.valueOf(settings.end));
    }

    Settings getSettings() {
        Settings settings = new Settings();
        settings.cores = Integer.valueOf(cores.getText());
        settings.dimension = Integer.valueOf(dimension.getText());
        settings.scale = Integer.valueOf(scale.getText());
        settings.lumus = ! lumus.isSelected();
        settings.maxIterations = Integer.valueOf(maxIterations.getText());
        settings.gainBase = Integer.valueOf(gainBase.getText());
        settings.actLim = Integer.valueOf(gainBase.getText());
        settings.strength = Integer.valueOf(strength.getText());
        settings.mutagen = Integer.valueOf(mutagen.getText());
        settings.end = Integer.valueOf(end.getText());

        return settings;

    }

    private static GridBagConstraints getLabelGBC(int x, int y) {
        return new GridBagConstraints(
                x, y, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 0, 5),
                0, 5);
    }

    private static GridBagConstraints getFieldGBC(int x, int y) {
        return new GridBagConstraints(x, y, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 0, 5),
                0, 5);
    }
}
