package base;

import base.ui.MainFrame;

import javax.swing.*;
import java.io.IOException;

public class Bacs {
    private static String title = "Bacs v1.12";

    static Settings settings;

    private static long start = System.nanoTime();

    private static MainFrame window;



    public static void main(String[] args) throws IOException {
        settings = Settings.fromProperties("conf.properties");
        SwingUtilities.invokeLater(() -> window = new MainFrame(title, settings));
    }
}