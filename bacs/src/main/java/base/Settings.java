package base;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

/**
 * Created by valera on 08.03.17.
 */
public class Settings {
    int cores;
    int dimension;
    int scale;
    int maxIterations;
    int actLim;
    int relSense;
    int gainBase;
    boolean lumus;

    int strength;
    int mutagen;
    int end;

    String behaviour;

    int cmdMove;
    int cmdTurn;
    int cmdEat;
    int cmdGain;
    int cmdAttack;
    int cmdObserve;
    
    public static Settings fromFile(String name) throws IOException {
        Settings settings = new Settings();

        Wini ini = new Wini(new File(name));
        //setting
        settings.cores = ini.get("settings", "threads", int.class);
        if(settings.cores == 0)
            settings.cores = Runtime.getRuntime().availableProcessors() /2;
        

        settings.dimension = ini.get("settings", "dimension", int.class);
        settings.scale = ini.get("settings", "scale", int.class);
        settings.maxIterations = ini.get("settings", "iterations", int.class);
        settings.lumus = ini.get("settings", "light", boolean.class);
        //settings
        settings.actLim = ini.get("BacUnit", "actlim", int.class);
        settings.relSense = ini.get("BacUnit", "relsence", int.class);
        settings.gainBase = ini.get("BacUnit", "gainbase", int.class);

        settings.strength = ini.get("BacUnit", "str", int.class);
        settings.mutagen = ini.get("BacUnit", "mut", int.class);
        settings.end = ini.get("BacUnit", "end", int.class);

        String beh = ini.get("BacUnit", "behaviour", String.class);

        beh = beh.replace("move", Integer.toString(settings.actLim));
        beh = beh.replace("turn", Integer.toString(settings.actLim+1));
        beh = beh.replace("eat", Integer.toString(settings.actLim+2));
        beh = beh.replace("gain", Integer.toString(settings.actLim+3));
        beh = beh.replace("attack", Integer.toString(settings.actLim+4));
        beh = beh.replace("observe", Integer.toString(settings.actLim+5));

        settings.behaviour = beh;

        return settings;
    }
}
