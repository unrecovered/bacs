package base;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by valera on 08.03.17.
 */
public class Settings {
    public int cores;
    public int dimension;
    public int scale;
    public int maxIterations;
    public int actLim;
    public int relSense;
    public int gainBase;
    public boolean lumus;

    public int strength;
    public int mutagen;
    public int end;

    String behaviour;

    public static Settings fromProperties(String name) throws IOException {
        Settings settings = new Settings();
        Properties props = new Properties();
        props.load(new FileReader(name));

        settings.cores = Integer.valueOf(props.getProperty("settings.threads", "1"));
        if (settings.cores == 0)
            settings.cores = Runtime.getRuntime().availableProcessors() / 2;

        settings.dimension = Integer.valueOf(props.getProperty("settings.dimension", "150"));
        settings.scale = Integer.valueOf(props.getProperty("settings.scale", "1"));
        settings.maxIterations = Integer.valueOf(props.getProperty("settings.iterations", "10000"));
        settings.lumus = Boolean.valueOf(props.getProperty("settings.light", "true"));

        settings.actLim = Integer.valueOf(props.getProperty("backunit.actlim", "20"));
        settings.relSense = Integer.valueOf(props.getProperty("backunit.relsence", "5"));
        settings.gainBase = Integer.valueOf(props.getProperty("backunit.gainbase", "5"));

        settings.strength = Integer.valueOf(props.getProperty("stats.str", "1"));
        settings.mutagen = Integer.valueOf(props.getProperty("stats.mutstat", "50"));
        settings.end = Integer.valueOf(props.getProperty("stats.end", "100"));

        String beh = props.getProperty("behaviour", "gain:");

        beh = beh.replace("move", Integer.toString(settings.actLim));
        beh = beh.replace("turn", Integer.toString(settings.actLim + 1));
        beh = beh.replace("eat", Integer.toString(settings.actLim + 2));
        beh = beh.replace("gain", Integer.toString(settings.actLim + 3));
        beh = beh.replace("attack", Integer.toString(settings.actLim + 4));
        beh = beh.replace("observe", Integer.toString(settings.actLim + 5));

        settings.behaviour = beh;


        return settings;
    }
}
